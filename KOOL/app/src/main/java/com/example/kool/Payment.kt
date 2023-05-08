package com.example.kool

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class Payment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val bca = findViewById<Button>(R.id.buttonBCA)
        val mandiri = findViewById<Button>(R.id.buttonMandiri)
        val niaga = findViewById<Button>(R.id.buttonNiaga)
        val back = findViewById<Button>(R.id.button6)
        val kost = intent.getParcelableExtra<Kost>(data)
        bca.setOnClickListener {

            val intent = Intent(this@Payment,Bca::class.java).apply {
                putExtra(Bca.data, kost)
            }
            startActivity(intent)
        }

        mandiri.setOnClickListener {
            val intent = Intent(this@Payment,Mandiri::class.java).apply {
                putExtra(Mandiri.data, kost)
            }
            startActivity(intent)
        }

        niaga.setOnClickListener {
            val intent = Intent(this@Payment,Niaga::class.java).apply {
                putExtra(Niaga.data, kost)
            }
            startActivity(intent)
        }

        back.setOnClickListener {
            val intent = Intent(this@Payment,MainActivity::class.java)
            startActivity(intent)
        }
    }
    companion object{
        const val data = "data"

    }
}