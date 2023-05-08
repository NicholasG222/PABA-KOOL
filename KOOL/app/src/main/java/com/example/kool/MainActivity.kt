package com.example.kool

import android.content.Intent
import android.content.res.TypedArray
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Adapter
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var _logout : TextView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var rvKos: RecyclerView
    val db = Firebase.firestore


    private var arKos = arrayListOf<Kost>()


    private var adapterK = adapterKost(arKos, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvKos = findViewById(R.id.rvKost)
        val _btnCust = findViewById<Button>(R.id.btnCust)
        val _btnConsul = findViewById<Button>(R.id.btnConsul)
        val sortNama = findViewById<Button>(R.id.buttonSortNama)
        val sortHarga = findViewById<Button>(R.id.buttonSortHarga)
        val buttonHistory = findViewById<Button>(R.id.buttonHist)
        val sp = getSharedPreferences("data_SP", MODE_PRIVATE)

        _btnCust.setOnClickListener {
            val _webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://wa.me/6282131621117?text=Permisi, saya mau bertanya mengenai tempat di sekitar kos."))

            if (intent.resolveActivity(packageManager) != null){
                startActivity(_webIntent)
            }
        }

        _btnConsul.setOnClickListener {
            val eIntent = Intent(this@MainActivity,Consultation::class.java)
            startActivity(eIntent)
        }
        sortNama.setOnClickListener {
            arKos.clear()

            db.collection("tbKos")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        var kost = Kost(
                            document.getString("pemilik"),
                            document.getString("alamat"),
                            document.getString("fasilitas"),
                            document.getString("foto"),
                            document.get("harga").toString().toInt(),
                            document.getString("nama"),
                            document.getString("telepon"),
                            document.get("rating").toString().toDouble()
                        )
                        arKos.add(kost)
                    }
                    arKos.sortWith(compareBy<Kost> { it.nama }.thenBy { it.rating }.thenBy { it.harga })
                    adapterK = adapterKost(arKos.distinct() as ArrayList<Kost>, this )
                    rvKos.layoutManager = LinearLayoutManager(this)
                    rvKos.adapter = adapterK
                    adapterK.setOnItemClickCallback(object : adapterKost.OnItemClickCallback{

                        override fun onItemClicked(data: Kost) {
                            val intent = Intent(this@MainActivity, detailKost::class.java)
                            intent.putExtra("kirimData",data)
                            startActivity(intent)
                        }

                        override fun pindahHal(data: Kost) {
                            var total = 0.0
                            var count = 0
                            db.collection("tbRating").get().addOnSuccessListener { result ->
                                for(document in result){


                                    if(document.getString("namaKost") == data.nama){
                                        total += document.get("rating").toString().toDouble()
                                        count++
                                    }
                                }
                                db.collection("tbKos").document(data.nama!!).update("rating", total/count)
                            }
                            val intent = Intent(this@MainActivity,Review::class.java)
                            intent.putExtra("kirimData",data)
                            startActivity(intent)
                        }

                        override fun pindahPay(data: Kost) {

                            val intent = Intent(this@MainActivity,Payment::class.java).apply{
                                putExtra(Payment.data, data)

                            }
                            startActivity(intent)
                        }

                        override fun berhentiSewa(data: Kost) {
                            val isi = sp.getString("SPlogin", null)
                            db.collection("tbUser").get().addOnSuccessListener {
                                    result ->
                                for(document in result){
                                    val email = document.getString("email")

                                    val listKost = document.get("kostSewa") as MutableList<Any?>

                                    var bool = false
                                    var deleted: HashMap<String, Any>? = null
                                    if(email == isi){

                                        for(i in listKost){
                                            Log.d("debug", "tru")
                                            if((i as HashMap<String, Any>).get("nama") == data.nama){
                                                bool = true
                                                deleted = i

                                            }
                                        }
                                        if(bool){

                                            listKost.remove(deleted)
                                        }
                                        val user = sp.getString("SPlogin", null)
                                        val hash =
                                            hashMapOf<String, Any?>(
                                                "user" to user,
                                                "kost" to data
                                            )
                                        val key = user + data.nama
                                        db.collection("tbHistory").document(key).set(hash)
                                        db.collection("tbUser").document(email!!).update("kostSewa", listKost)
                                        resetView()
                                    }
                                }
                            }
                        }




                    })
                }
        }
        buttonHistory.setOnClickListener {
            val intent = Intent(this@MainActivity, HistorySewa::class.java)
            startActivity(intent)
        }
        sortHarga.setOnClickListener {
            arKos.clear()

            db.collection("tbKos")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        var kost = Kost(
                            document.getString("pemilik"),
                            document.getString("alamat"),
                            document.getString("fasilitas"),
                            document.getString("foto"),
                            document.get("harga").toString().toInt(),
                            document.getString("nama"),
                            document.getString("telepon"),
                            document.get("rating").toString().toDouble()
                        )
                        arKos.add(kost)
                    }
                    arKos.sortWith(compareBy<Kost> { it.harga }.thenBy { it.rating }.thenBy { it.nama })
                    adapterK = adapterKost(arKos.distinct() as ArrayList<Kost>, this )
                    rvKos.layoutManager = LinearLayoutManager(this)
                    rvKos.adapter = adapterK
                    adapterK.setOnItemClickCallback(object : adapterKost.OnItemClickCallback{

                        override fun onItemClicked(data: Kost) {
                            val intent = Intent(this@MainActivity, detailKost::class.java)
                            intent.putExtra("kirimData",data)
                            startActivity(intent)
                        }

                        override fun pindahHal(data: Kost) {
                            var total = 0.0
                            var count = 0
                            db.collection("tbRating").get().addOnSuccessListener { result ->
                                for(document in result){


                                    if(document.getString("namaKost") == data.nama){
                                        total += document.get("rating").toString().toDouble()
                                        count++
                                    }
                                }
                                db.collection("tbKos").document(data.nama!!).update("rating", total/count)
                            }
                            val intent = Intent(this@MainActivity,Review::class.java)
                            intent.putExtra("kirimData",data)
                            startActivity(intent)
                        }

                        override fun pindahPay(data: Kost) {

                            val intent = Intent(this@MainActivity,Payment::class.java).apply{
                                putExtra(Payment.data, data)

                            }
                            startActivity(intent)
                        }

                        override fun berhentiSewa(data: Kost) {
                            val isi = sp.getString("SPlogin", null)
                            db.collection("tbUser").get().addOnSuccessListener {
                                    result ->
                                for(document in result){
                                    val email = document.getString("email")

                                    val listKost = document.get("kostSewa") as MutableList<Any?>

                                    var bool = false
                                    var deleted: HashMap<String, Any>? = null
                                    if(email == isi){

                                        for(i in listKost){
                                            Log.d("debug", "tru")
                                            if((i as HashMap<String, Any>).get("nama") == data.nama){
                                                bool = true
                                                deleted = i

                                            }
                                        }
                                        if(bool){

                                            listKost.remove(deleted)
                                        }
                                        val user = sp.getString("SPlogin", null)
                                        val hash =
                                            hashMapOf<String, Any?>(
                                                "user" to user,
                                                "kost" to data
                                            )
                                        val key = user + data.nama
                                        db.collection("tbHistory").document(key).set(hash)
                                        db.collection("tbUser").document(email!!).update("kostSewa", listKost)
                                        resetView()
                                    }
                                }
                            }
                        }




                    })
                }
        }


        SiapkanData()





        adapterK.setOnItemClickCallback(object : adapterKost.OnItemClickCallback{

            override fun onItemClicked(data: Kost) {
                val intent = Intent(this@MainActivity, detailKost::class.java)
                intent.putExtra("kirimData",data)
                startActivity(intent)
            }

            override fun pindahHal(data: Kost) {
                var total = 0.0
                var count = 0
                db.collection("tbRating").get().addOnSuccessListener { result ->
                    for(document in result){


                        if(document.getString("namaKost") == data.nama){
                            total += document.get("rating").toString().toDouble()
                            count++
                        }
                    }
                    db.collection("tbKos").document(data.nama!!).update("rating", total/count)
                }
                val intent = Intent(this@MainActivity,Review::class.java)
                intent.putExtra("kirimData",data)
                startActivity(intent)
            }

            override fun pindahPay(data: Kost) {

                val intent = Intent(this@MainActivity,Payment::class.java).apply{
                    putExtra(Payment.data, data)

                }
                startActivity(intent)
            }

            override fun berhentiSewa(data: Kost) {
                val isi = sp.getString("SPlogin", null)
                db.collection("tbUser").get().addOnSuccessListener {
                        result ->
                    for(document in result){
                        val email = document.getString("email")

                        val listKost = document.get("kostSewa") as MutableList<Any?>

                        var bool = false
                        var deleted: HashMap<String, Any>? = null
                        if(email == isi){

                            for(i in listKost){

                                if((i as HashMap<String, Any>).get("foto") == data.foto){
                                    bool = true
                                    deleted = i

                                }
                            }
                            if(bool){

                                listKost.remove(deleted)
                            }
                            val user = sp.getString("SPlogin", null)
                            val hash =
                                hashMapOf<String, Any?>(
                                    "user" to user,
                                    "kost" to data
                                )
                            val key = user + data.nama
                            db.collection("tbHistory").document(key).set(hash)
                            db.collection("tbUser").document(email!!).update("kostSewa", listKost)
                            resetView()
                        }
                    }
                }
            }




        })

        _logout = findViewById(R.id.logout)
        firebaseAuth = FirebaseAuth.getInstance()
        _logout.setOnClickListener {
            firebaseAuth.signOut()

            val intent = Intent(this@MainActivity,HalamanPilih::class.java)
            startActivity(intent)
            val editor = sp.edit()
            editor.remove("SPlogin")
            editor.apply()
        }
    }

    private fun SiapkanData(){
        db.collection("tbKos")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    var kost = Kost(document.getString("pemilik"), document.getString("alamat"), document.getString("fasilitas"), document.getString("foto"),
                        document.get("harga").toString().toInt(), document.getString("nama"), document.getString("telepon"), document.get("rating").toString().toDouble())
                    arKos.add(kost)
                }

                rvKos.layoutManager = LinearLayoutManager(this)
                rvKos.adapter = adapterK

            }

    }

    private fun resetView(){

        rvKos.layoutManager = LinearLayoutManager(this)
        rvKos.adapter = adapterK
    }




}


