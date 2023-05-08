package com.example.kool

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HistorySewa : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_history_sewa)
        val lvHist = findViewById<ListView>(R.id.listView)
        val back = findViewById<Button>(R.id.buttonBack)
        val db = Firebase.firestore
        val sp = getSharedPreferences("data_SP", MODE_PRIVATE)
        val mut = mutableListOf<String>()
        Log.d("debughist", sp.getString("SPlogin", null)!!)
        db.collection("tbHistory").get().addOnSuccessListener { result->
            for(doc in result){
                if(doc.getString("user") == sp.getString("SPlogin", null)){
                    mut.add((doc.get("kost") as HashMap<String, Any>).get("nama").toString())
                    val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mut)
                    lvHist.adapter = adapter
                }
            }
        }
        back.setOnClickListener {
            val intent = Intent(this@HistorySewa, MainActivity::class.java)
            startActivity(intent)
        }



    }
}