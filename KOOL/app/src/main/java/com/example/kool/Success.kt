package com.example.kool

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Success : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)

        val btnback = findViewById<Button>(R.id.button3)
        btnback.setOnClickListener {
            val intent = Intent(this@Success,MainActivity::class.java)
            startActivity(intent)
        }
    }
}