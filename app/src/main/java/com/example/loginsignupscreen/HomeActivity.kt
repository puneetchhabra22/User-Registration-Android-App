package com.example.loginsignupscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import org.w3c.dom.Text

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val userName = intent.getStringExtra(LoginActivity.NAME)
        val userEmail = intent.getStringExtra(LoginActivity.EMAIL)

        Log.i("putExtra", "gotHome name: "+userName)
        Log.i("putExtra", "gotHome email: "+userEmail)

        val tvName = findViewById<TextView>(R.id.tvName)
        val tvEmail = findViewById<TextView>(R.id.tvEmail)
        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)

        tvWelcome.text = "Welcome, $userName"
        tvName.text = "Name: $userName"
        tvEmail.text = "Email: $userEmail"
    }
}