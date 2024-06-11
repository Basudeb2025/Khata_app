package com.example.apna_khata

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class view_list : AppCompatActivity() {
    lateinit var auth:FirebaseAuth
    lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()
        val cashin = findViewById<TextView>(R.id.cashinch)
        val cashinmoney = findViewById<TextView>(R.id.cashinMoney)
        val reason = findViewById<TextView>(R.id.Reason)
        val delete = findViewById<ImageView>(R.id.delete)
        val bckbtn = findViewById<ImageView>(R.id.back)
        bckbtn.setOnClickListener {
            onBackPressed()
        }
        val intcash = intent.getStringExtra("Cashinorcashout")
        val intmoney = intent.getStringExtra("CashMoney")
        val intreason = intent.getStringExtra("ReasonFor")
        val linkofId = intent.getStringExtra("LinkofItem")
        cashin.text = intcash.toString()
        cashinmoney.text = intmoney.toString()
        reason.text = intreason.toString()
        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(auth.currentUser?.uid.toString()).child("List").child(linkofId.toString())
        delete.setOnClickListener {
            val notfication = AlertDialog.Builder(this)
            notfication.setIcon(R.drawable.delete)
            notfication.setTitle("Are you sure?")
            notfication.setMessage("Do you want to Delete it")
            notfication.setPositiveButton("yes", DialogInterface.OnClickListener { dialog, which ->
                databaseReference.removeValue().addOnSuccessListener {
                    startActivity(Intent(this,main_screen::class.java))
                    finish()
                }.addOnCanceledListener {
                    Toast.makeText(this,"Unable to delete it",Toast.LENGTH_LONG).show()
                }
            })
            notfication.setNegativeButton("no", DialogInterface.OnClickListener { dialog, which ->
                //For stay on that page
            })
            notfication.show()
        }
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.sky_blue)
        }
    }
}