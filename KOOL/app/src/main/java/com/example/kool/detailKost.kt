package com.example.kool

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import com.squareup.picasso.Picasso

class detailKost : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_kost)

        val _tvFoto = findViewById<ImageView>(R.id.imageKost)
        val _tvNama = findViewById<TextView>(R.id.tvNama)
        val _tvAlamat = findViewById<TextView>(R.id.tvAlamat)
        val _tvFasilitas = findViewById<TextView>(R.id.tvFasilitas)
        val _tvHarga = findViewById<TextView>(R.id.tvHarga)
        val _tvTelp = findViewById<TextView>(R.id.tvTelp)
        val backButton = findViewById<Button>(R.id.buttonBack)

        val dataIntent = intent.getParcelableExtra<Kost>("kirimData")
        if (dataIntent != null) {
            Picasso.get().load(dataIntent.foto!!).into(_tvFoto)
        }
        _tvNama.setText(dataIntent!!.nama)
        _tvAlamat.setText(dataIntent.alamat)
        _tvFasilitas.setText(dataIntent.fasilitas)
        _tvTelp.setText(dataIntent.telepon)
        _tvHarga.setText(dataIntent.harga.toString())

        _tvTelp.setOnClickListener {
                val _webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://wa.me/" + _tvTelp.text.toString()))

                if (intent.resolveActivity(packageManager) != null){
                    startActivity(_webIntent)
                }
        }
        backButton.setOnClickListener {
            val backIntent = Intent(this@detailKost, MainActivity::class.java)
            startActivity(backIntent)
        }
    }
}