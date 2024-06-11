package com.example.apna_khata

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class Forget_password : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forget_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()
        val email_for_reset = findViewById<TextInputEditText>(R.id.email_for_reset)
        val reset = findViewById<CardView>(R.id.reset)
        val back_btn = findViewById<ImageView>(R.id.back_btn)
        reset.setOnClickListener {
            val email = email_for_reset.text.toString()
            if(email.isNotEmpty()) {
                            auth.sendPasswordResetEmail(email)
                                .addOnSuccessListener {
                                    val notification = AlertDialog.Builder(this)
                                    notification.setIcon(R.drawable.side_nav_bar)
                                    notification.setTitle("Password Reset Link Sent")
                                    notification.setMessage("A password reset link has been sent to your email. Please check your inbox and follow the instructions to reset your password.")
                                    notification.show()
                                }
                                .addOnFailureListener { exception ->
                                    Toast.makeText(this, "Failed to send reset email: ${exception.message}", Toast.LENGTH_LONG).show()
                                }
            } else {
                Toast.makeText(this, "Enter email address", Toast.LENGTH_LONG).show()
            }
        }

        back_btn.setOnClickListener {
            onBackPressed()
        }
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.sky_blue)
        }
    }
}