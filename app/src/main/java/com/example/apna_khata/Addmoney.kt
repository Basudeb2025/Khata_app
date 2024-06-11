package com.example.apna_khata

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlin.properties.Delegates

class Addmoney : AppCompatActivity() {
    lateinit var auth:FirebaseAuth
    var check:Boolean = true
    lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_addmoney)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.sky_blue)
        }
        auth = FirebaseAuth.getInstance()
        val bck = findViewById<ImageView>(R.id.back)
        val totalm = findViewById<EditText>(R.id.Totalmoney)
        val monthm = findViewById<EditText>(R.id.Montlymoney)
        val mmadd = findViewById<Button>(R.id.TotalADDbtn)
        val addbtn= findViewById<Button>(R.id.TotalADDmm)
        val showtotal = findViewById<TextView>(R.id.totalshow)
        val showmonth = findViewById<TextView>(R.id.monylyshow)
        val uid = auth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("User")
        firebaseFirestore = FirebaseFirestore.getInstance()
        val database = databaseReference.child(uid.toString())
        // Add TotalMoney
        addbtn.setOnClickListener {
            val Totalm = totalm.text.toString()
            if(Totalm.isNotEmpty()){
                val totalMoneyData = hashMapOf(
                    "Totalmoney" to Totalm
                )
                firebaseFirestore.collection("Users").document(uid.toString())
                    .set(totalMoneyData, SetOptions.merge())
                    .addOnSuccessListener {
                        //startActivity(Intent(this, main_screen::class.java))
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to update total money", Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(this, "Enter the details carefully", Toast.LENGTH_LONG).show()
            }
        }

// Add monthly money
        mmadd.setOnClickListener {
            val monthlymm = monthm.text.toString()
            if(monthlymm.isNotEmpty()){
                val monthlyMoneyData = hashMapOf(
                    "check" to check, // Assuming check is already defined
                    "monthmoney" to monthlymm
                )
                firebaseFirestore.collection("Users").document(uid.toString())
                    .set(monthlyMoneyData, SetOptions.merge())
                    .addOnSuccessListener {
                        //startActivity(Intent(this, main_screen::class.java))
                        Toast.makeText(this, "updated monthly money", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to update monthly money", Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(this, "Enter the details carefully", Toast.LENGTH_LONG).show()
            }
        }


        //Read the add data
        firebaseFirestore.collection("Users").document(uid.toString())
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val totalMoney = documentSnapshot.getString("Totalmoney")
                    val monthlyMoney = documentSnapshot.getString("monthmoney")
                    if(totalMoney != null){
                        showtotal.text = totalMoney.toString()
                    }
                    else showtotal.text = "0000"
                    if(monthlyMoney != null){
                        showmonth.text = monthlyMoney.toString()
                    }
                    else showmonth.text = "0000"
                    // Use totalMoney and monthlyMoney here, such as logging or displaying in UI
                } else {
                    Log.d(TAG, "No details found for user $uid")
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting user details: ", exception)
            }

        val userId = auth.currentUser?.uid
        bck.setOnClickListener {
            onBackPressed()
        }
        //val btn = findViewById<CardView>(R.id.TotalADDbtn)
    }
}