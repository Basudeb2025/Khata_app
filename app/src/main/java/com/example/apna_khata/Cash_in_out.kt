package com.example.apna_khata

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class Cash_in_out : AppCompatActivity() {
    lateinit var dataBase:DatabaseReference
    lateinit var auth:FirebaseAuth
    var ch:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cash_in_out)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()
        val cashin = findViewById<TextView>(R.id.cashIn)
        val cashout = findViewById<TextView>(R.id.cashOut)
        val txt = findViewById<TextView>(R.id.textcash)
        val cain = findViewById<CardView>(R.id.cain)
        val caout = findViewById<CardView>(R.id.caout)
        val amount = findViewById<TextInputEditText>(R.id.amount_h)
        val reason = findViewById<TextInputEditText>(R.id.reason_h)
        val saveBtn = findViewById<CardView>(R.id.save_btn)
        val check = findViewById<CheckBox>(R.id.checkbox)
        check.setOnClickListener {
            if(check.isChecked) ch = true
            else ch = false
        }
        val intent1 = intent.getStringExtra("valued")
        txt.text = intent1.toString()
        if(intent1 == "Cash In"){
            txt.setTextColor(ContextCompat.getColor(this, R.color.green))
            cashin.setTextColor(ContextCompat.getColor(this, R.color.green))
            cashout.setTextColor(ContextCompat.getColor(this, R.color.black))
        }
        else{
            cashout.setTextColor(ContextCompat.getColor(this, R.color.red))
            cashin.setTextColor(ContextCompat.getColor(this, R.color.black))
        }
        saveBtn.setOnClickListener {
            val amounth = amount.text.toString()
            val reasonh = reason.text.toString()
            val currentDateTime = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val dateString = dateFormat.format(currentDateTime.time)
            val timeString = timeFormat.format(currentDateTime.time)
            var cashin = txt.text.toString()
            //val userData = mapOf("Cashin" to cashin,"Cashout" to cashou, "Reason" to reasonh , "Date" to dateString, "Time" to timeString)
            val datas = Datas2(cashin,amounth,reasonh,dateString,timeString,ch)
            val userId = auth.currentUser?.uid
            dataBase = FirebaseDatabase.getInstance().getReference("User")
            if(amounth.isNotEmpty() && reasonh.isNotEmpty()) {
                dataBase.child(userId.toString()).child("List").push().setValue(datas)
                    .addOnSuccessListener {
                        startActivity(Intent(this, main_screen::class.java))
                        finish()
                    }.addOnFailureListener {
                    Toast.makeText(this, "Failed to save", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this, "Please enter full details", Toast.LENGTH_LONG).show()
            }
        }
        cashin.setOnClickListener {
            txt.setTextColor(ContextCompat.getColor(this, R.color.green))
            cashin.setTextColor(ContextCompat.getColor(this, R.color.green))
            cashout.setTextColor(ContextCompat.getColor(this, R.color.black))
            txt.text = "Cash In"
        }
        cashout.setOnClickListener {
            txt.setTextColor(ContextCompat.getColor(this, R.color.red))
            cashout.setTextColor(ContextCompat.getColor(this, R.color.red))
            cashin.setTextColor(ContextCompat.getColor(this, R.color.black))
            txt.text = "Cash Out"
        }
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.sky_blue)
        }
      val btn = findViewById<ImageView>(R.id.back)
        btn.setOnClickListener {
            onBackPressed()
        }
    }
}