package com.example.loginsignupscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    lateinit var databaseUsersReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //underlining text of tvAlreadyRegstered
        val tvAlreadyUser = findViewById<TextView>(R.id.tvAlreadyUser)
        val spannableString = SpannableString(tvAlreadyUser.text)
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
        tvAlreadyUser.text = spannableString


        val etUsername = findViewById<TextInputEditText>(R.id.eTUsername)
        val eTName = findViewById<TextInputEditText>(R.id.eTName)
        val eTEmail = findViewById<TextInputEditText>(R.id.eTEmail)
        val eTPassword = findViewById<TextInputEditText>(R.id.eTPassword)
        val btnSignup = findViewById<Button>(R.id.btnSignUp)

        btnSignup.setOnClickListener {
            val username = etUsername.text.toString()
            val name = eTName.text.toString()
            val email = eTEmail.text.toString()
            val password = eTPassword.text.toString()

            if (username.isBlank() || name.isBlank() || email.isBlank() || password.isEmpty()) {
                Toast.makeText(this, "Please Fill all the details.", Toast.LENGTH_SHORT).show()
            } else {
                val user: User = User(username, name, email, password)
                signUpUser(user)
            }
        }

        tvAlreadyUser.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }

    private fun signUpUser(user: User) {
        val etUsername = findViewById<TextInputEditText>(R.id.eTUsername)
        val eTName = findViewById<TextInputEditText>(R.id.eTName)
        val eTEmail = findViewById<TextInputEditText>(R.id.eTEmail)
        val eTPassword = findViewById<TextInputEditText>(R.id.eTPassword)

        databaseUsersReference = FirebaseDatabase.getInstance().getReference("Users")
        //provided path to database. In which node you want to store your data(i.e. Users node)
        databaseUsersReference.child(user.username).setValue(user).addOnSuccessListener {
            etUsername.text?.clear()
            eTEmail.text?.clear()
            eTName.text?.clear()
            eTPassword.text?.clear()

            Toast.makeText(this, "Resgistered Successfully", Toast.LENGTH_SHORT).show()
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }.addOnFailureListener {
            Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
        }
    }
}