package com.example.kool

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class TambahKos : AppCompatActivity() {
    private lateinit var sp: SharedPreferences
    private lateinit var dataKos: MutableList<Kost>
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var rvKos: RecyclerView
    private lateinit var _logout : TextView
    val db = Firebase.firestore

    lateinit var img: ImageView
    private var arKos: ArrayList<Kost> = arrayListOf<Kost>()
    private var adapterK = adapterTambahKos(arKos)
    private fun siapkanData(){
        db.collection("tbKos")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    var kost = Kost(document.getString("pemilik"),document.getString("alamat"), document.getString("fasilitas"), document.getString("foto"),
                        document.get("harga").toString().toInt(), document.getString("nama"), document.getString("telepon"), document.get("rating").toString().toDouble())
                    if(kost.pemilik == sp.getString("SPlogin2", null)){
                        arKos.add(kost)
                    }

                }
                Log.d("DEBUG", arKos.toString())
                rvKos.layoutManager = LinearLayoutManager(this)
                rvKos.adapter = adapterK

            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_kos)
        rvKos = findViewById(R.id.rvKost)
        sp = getSharedPreferences("data_SP", MODE_PRIVATE)
        val addButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        _logout = findViewById(R.id.logout)
        firebaseAuth = FirebaseAuth.getInstance()
        _logout.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(this@TambahKos,HalamanPilih::class.java)
            startActivity(intent)
            val editor = sp.edit()
            editor.remove("SPlogin2")
            editor.apply()
        }
        siapkanData()

        addButton.setOnClickListener {
            val intent = Intent(this@TambahKos, MasukkanDataKos::class.java)
            startActivity(intent)
        }

        adapterK.setOnItemClickCallback(object : adapterTambahKos.OnItemClickCallback{

            override fun onItemClicked(data: Kost) {
                val intent = Intent(this@TambahKos, DetailPemilik::class.java)
                intent.putExtra("kirimData",data)
                startActivity(intent)
            }

            override fun lihatPenyewa(data: Kost) {
                val intent = Intent(this@TambahKos, DaftarPenyewa::class.java).apply{
                    putExtra("data", data)
                }
                startActivity(intent)
            }


        })


    }
}