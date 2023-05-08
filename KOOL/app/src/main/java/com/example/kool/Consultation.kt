package com.example.kool

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class Consultation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultation)

        val _btnConsul = findViewById<Button>(R.id.buttonConsul)
        val daerahKos = findViewById<EditText>(R.id.editTextTextPersonName)
        val hargaKos = findViewById<EditText>(R.id.editTextTextPersonName2)
        val fasilitas = findViewById<EditText>(R.id.editTextTextPersonName3)
        val jabatan = findViewById<EditText>(R.id.editTextTextPersonName4)
        val _btnback = findViewById<Button>(R.id.buttonBack)

        _btnConsul.setOnClickListener {
            val _daerahKos = daerahKos.text.toString()
            val _hargaKos = hargaKos.text.toString().toInt()
            val _fasilitas = fasilitas.text.toString()
            val _jabatan = jabatan.text.toString()
            val _webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://wa.me/6285220259890/?text=Permisi, saya mau berkonsultasi mengenai rekomendasi kos untuk " + _jabatan
                    + " dengan harga kos sekitar "+_hargaKos+" dengan fasilitas "+_fasilitas+" di daerah "+_daerahKos+"."))

            if (intent.resolveActivity(packageManager) != null){
                startActivity(_webIntent)
            }
        }

        _btnback.setOnClickListener {
            val intent = Intent(this@Consultation,MainActivity::class.java)
            startActivity(intent)
        }
    }
}