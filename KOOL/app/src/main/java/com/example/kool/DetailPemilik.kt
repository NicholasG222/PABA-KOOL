package com.example.kool

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DetailPemilik : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_pemilik)
        val editNama = findViewById<EditText>(R.id.tvNama)
        val editAlamat = findViewById<EditText>(R.id.tvAlamat)
        val editTelp = findViewById<EditText>(R.id.tvTelp)
        val editFasilitas = findViewById<EditText>(R.id.tvFasilitas)
        val editHarga = findViewById<EditText>(R.id.tvHarga)
        val buttonBack = findViewById<Button>(R.id.buttonBack)
        val buttonSubmit = findViewById<Button>(R.id.buttonEdit)
        val db = Firebase.firestore
        val data = intent.getParcelableExtra<Kost>("kirimData")
        val change = data?.nama
        val sp = getSharedPreferences("data_SP2", MODE_PRIVATE)
        buttonBack.setOnClickListener {
            val intent = Intent(this@DetailPemilik, TambahKos::class.java)
            startActivity(intent)
        }
        buttonSubmit.setOnClickListener {
            var bool = true
            db.collection("tbUser").get().addOnSuccessListener { result ->
                for (document in result) {
                    var arKos = document.get("kostSewa") as MutableList<Any>
                    for (i in arKos) {
                        if ((i as HashMap<String, Any>).get("nama") == change) {
                            bool = false
                        }
                    }
                }
            }
            var namaBaru = editNama.text.toString()
            val alamat = editAlamat.text.toString()
            val telp = editTelp.text.toString()
            val fasilitas = editFasilitas.text.toString()
            val harga = editHarga.text.toString()


            if (bool) {
                if (data != null) {
                    db.collection("tbKos").document(namaBaru).set(data)
                }


                if (namaBaru.isBlank()) {
                    namaBaru = data?.nama.toString()
                } else {
                    if (change != null) {
                        db.collection("tbKos").document(change).delete()
                    }

                }


                db.collection("tbKos").document(namaBaru).update("nama", namaBaru)
                if (alamat.isNotBlank()) {
                    if (change != null) {
                        db.collection("tbKos").document(namaBaru).update("alamat", alamat)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Berhasil mengganti data", Toast.LENGTH_LONG)
                                    .show()
                            }
                        db.collection("tbKos").document(change).delete()


                    }
                }
                    if (telp.isNotBlank()) {
                        if (change != null) {
                            db.collection("tbKos").document(namaBaru).update("telepon", telp)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this,
                                        "Berhasil mengganti data",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }
                            db.collection("tbKos").document(change).delete()

                        }
                    }
                    if (fasilitas.isNotBlank()) {
                        if (change != null) {
                            db.collection("tbKos").document(namaBaru).update("fasilitas", fasilitas)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this,
                                        "Berhasil mengganti data",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }
                            db.collection("tbKos").document(change).delete()

                        }
                    }
                    if (harga.isNotBlank()) {
                        if (change != null) {
                            db.collection("tbKos").document(namaBaru).update("harga", harga.toInt())
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this,
                                        "Berhasil mengganti data",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }
                            db.collection("tbKos").document(change).delete()

                        }


                    }
                }else{
                    Toast.makeText(this, "Tidak bisa mengganti data karena sudah ada penyewa", Toast.LENGTH_LONG).show()
            }

        }
    }
    companion object{
        const val data = "data"
    }
}


