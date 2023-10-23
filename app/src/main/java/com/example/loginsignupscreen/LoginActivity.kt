package com.example.loginsignupscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    private var databaseUsersReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
    companion object {
        const val NAME = "com.example.loginsignupscreen.NAME"
        const val EMAIL = "com.example.loginsignupscreen.EMAIL"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val eTUsername = findViewById<TextInputEditText>(R.id.eTUsername)
        val eTPassword = findViewById<TextInputEditText>(R.id.eTPassword)


        btnLogin.setOnClickListener{
            val username = eTUsername.text.toString()
            val password = eTPassword.text.toString()
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please Fill all the details.", Toast.LENGTH_SHORT).show()
            } else {
                checkUser(username,password){success ->
                    if(success){
                        val homeIntent = Intent(this,HomeActivity::class.java)
                        databaseUsersReference.child(username).get().addOnSuccessListener { user ->
                            val name = user.child("name").value.toString()
                            val email = user.child("email").value.toString()
                            Log.i("putExtra", "got name: "+name)
                            Log.i("putExtra", "got email: "+email)
                            homeIntent.putExtra(NAME, name)
                            homeIntent.putExtra(EMAIL, email)
                            startActivity(homeIntent)
                            //An important point to note here. Check important points.
                        }
                    } else { //user doesn't exist
                        val signUpIntent = Intent(this,SignUpActivity::class.java)
                        startActivity(signUpIntent)
                    }
                }
            }
        }
    }

    private fun checkUser(username: String, password: String, callback: (Boolean) -> Unit) {

//        databaseUsersReference =

        databaseUsersReference.child(username).get().addOnSuccessListener { user ->
            if(user.exists()){ //child with given username exists
                if((user.child("password").value.toString()) == password){
                    //password matched
                    callback(true)
                } else {
                    //wrong password
                    Toast.makeText(this, "Wrong Password", Toast.LENGTH_SHORT).show()
                }
            } else { //child doesn't exist
                Toast.makeText(this, "User doesn't exist", Toast.LENGTH_SHORT).show()
                callback(false)
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() { //clearing text fields on resume
        //i.e. whenever user gets back to this activity from backstack
        super.onResume()
        val eTUsername = findViewById<TextInputEditText>(R.id.eTUsername)
        val eTPassword = findViewById<TextInputEditText>(R.id.eTPassword)

        eTUsername.text?.clear()
        eTPassword.text?.clear()
    }
}