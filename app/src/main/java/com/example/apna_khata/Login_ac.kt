package com.example.apna_khata

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.sign

class Login_ac : AppCompatActivity() {
    lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()
        val email = findViewById<TextInputEditText>(R.id.email)
        val pass = findViewById<TextInputEditText>(R.id.password)
        val btn = findViewById<CardView>(R.id.login)
        val sign_up_ = findViewById<TextView>(R.id.sign_up_)
        val forget = findViewById<TextView>(R.id.forget)
        forget.setOnClickListener {
            startActivity(Intent(this,Forget_password::class.java))
        }
        sign_up_.setOnClickListener {
            startActivity(Intent(this, sign_up::class.java))
        }
        btn.setOnClickListener {
            val em = email.text.toString()
            val pa = pass.text.toString()
            if(em.isNotEmpty() && pa.isNotEmpty()){
                auth.signInWithEmailAndPassword(em,pa).addOnSuccessListener {
                    startActivity(Intent(this,main_screen::class.java))
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this,"User does not exit",Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this,"Please enter the full details",Toast.LENGTH_LONG).show()
            }
        }
    }
}