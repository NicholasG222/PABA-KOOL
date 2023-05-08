package com.example.kool

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Mandiri : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mandiri)

        val tHarga = findViewById<TextView>(R.id.tHarga)
        val tPajak = findViewById<TextView>(R.id.tPajak)
        val tTotal = findViewById<TextView>(R.id.tTotal)
        val buttonSuccess = findViewById<Button>(R.id.buttonConfirm)
        val buttonBack = findViewById<Button>(R.id.button5)
        val db = Firebase.firestore
        val sp = getSharedPreferences("data_SP", MODE_PRIVATE)
        val kost = intent.getParcelableExtra<Kost>(data)

        tHarga.setText(kost!!.harga.toString())
        tPajak.setText(kost.harga?.let { pajak(it).toString() })
        if (kost.harga != null) {
            tTotal.setText((kost.harga + pajak(kost.harga)).toInt().toString())
        }
        buttonSuccess.setOnClickListener {
            val isi = sp.getString("SPlogin", null)
            db.collection("tbUser").get().addOnSuccessListener {
                    result ->
                for(document in result){
                    val email = document.getString("email")
                    val listKost = document.get("kostSewa") as MutableList<Any?>
                    if(email == isi){
                        listKost.add(kost)
                        db.collection("tbUser").document(email!!).update("kostSewa", listKost)

                    }
                }
            }
            val intent = Intent(this@Mandiri,Success::class.java)
            startActivity(intent)
        }
        buttonBack.setOnClickListener {
            val intent = Intent(this@Mandiri,Payment::class.java)
            startActivity(intent)
        }
    }
    fun pajak(harga: Int): Double{
        return harga.toDouble() * (5.0 / 100.0)
    }
    companion object{
        const val data = "Data1"

    }
}