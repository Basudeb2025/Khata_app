package com.example.apna_khata

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class main_screen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        replaceWithfrage(Home_frage())
        val add = findViewById<FloatingActionButton>(R.id.fabbtn)
        add.setOnClickListener {
            startActivity(Intent(this,Addmoney::class.java))
        }
      val btnnav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        btnnav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.itemhome->replaceWithfrage(Home_frage())
                R.id.itemaccount-> replaceWithfrage(Accoun())
            }
            true
        }
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.blue)
        }
    }
    private fun replaceWithfrage(fragment: Fragment) {
        val fargementMana = supportFragmentManager
        val frgetrac = fargementMana.beginTransaction()
        frgetrac.replace(R.id.framebtn,fragment)
        frgetrac.commit()
    }
}