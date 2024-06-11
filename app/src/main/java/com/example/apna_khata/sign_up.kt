package com.example.apna_khata

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class sign_up : AppCompatActivity() {
    lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val name = findViewById<EditText>(R.id.Name_sign_Up)
        val email = findViewById<EditText>(R.id.Email_sign_up)
        val pass = findViewById<EditText>(R.id.password_sign_up)
        val btn = findViewById<Button>(R.id.Sign_up_btn)
        val login = findViewById<TextView>(R.id.login)
        login.setOnClickListener {
            startActivity(Intent(this,Login_ac::class.java))
        }
        auth = FirebaseAuth.getInstance()
        btn.setOnClickListener {
            val nametxt = name.text.toString()
            val emailtxt = email.text.toString()
            val passtxt = pass.text.toString()
            if(nametxt.isNotEmpty() && emailtxt.isNotEmpty() && passtxt.isNotEmpty()){
                auth.createUserWithEmailAndPassword(emailtxt,passtxt).addOnSuccessListener {
                    startActivity(Intent(this,main_screen::class.java))
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this,"Failed to signUp",Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this,"Please enter the full details",Toast.LENGTH_LONG).show()
            }

        }
    }
}