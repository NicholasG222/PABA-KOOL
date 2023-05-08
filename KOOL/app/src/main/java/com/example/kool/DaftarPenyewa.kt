package com.example.kool

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DaftarPenyewa : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_penyewa)
        val db = Firebase.firestore
        val kostIntent = intent.getParcelableExtra<Kost>("data")
        val lvPenyewa = findViewById<ListView>(R.id.lvPenyewa)
        val tvJumlah = findViewById<TextView>(R.id.tvJumlah)
        var mut = mutableListOf<String>()
        val back = findViewById<Button>(R.id.buttonBack)
        back.setOnClickListener {
            val intent = Intent(this@DaftarPenyewa, TambahKos::class.java)
            startActivity(intent)
        }
        var count = 0
        db.collection("tbUser").get().addOnSuccessListener { result->
            for(doc in result){
                val kost = doc.get("kostSewa") as MutableList<Any?>
                for(i in kost){
                    if((i as HashMap<String, Any>).get("nama") == kostIntent!!.nama){
                        mut.add(doc.getString("email").toString())
                        count++
                        val lvAdapter: ArrayAdapter<String> = ArrayAdapter(
                            this,
                            android.R.layout.simple_list_item_1,
                            mut
                        )
                        lvPenyewa.adapter = lvAdapter
                    }
                }
            }
            tvJumlah.setText("Jumlah penyewa: "+ count.toString())
        }

    }
}