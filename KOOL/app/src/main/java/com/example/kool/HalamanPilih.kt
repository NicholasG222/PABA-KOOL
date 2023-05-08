package com.example.kool

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class HalamanPilih : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_halaman_pilih)
        val buttonPenyewa = findViewById<Button>(R.id.buttonPenyewa)
        val buttonPemilik = findViewById<Button>(R.id.buttonPemilik)
        buttonPenyewa.setOnClickListener {
            val intent = Intent(this@HalamanPilih, Login:: class.java).apply{
                putExtra(Login.data, "Penyewa")
            }
            startActivity(intent)
        }
        buttonPemilik.setOnClickListener {
            val intent = Intent(this@HalamanPilih, Login:: class.java).apply{
                putExtra(Login.data, "Pemilik")
            }
            startActivity(intent)
        }
    }
}