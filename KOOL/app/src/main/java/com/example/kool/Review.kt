package com.example.kool

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Review : AppCompatActivity() {
    private lateinit var rvReview: RecyclerView


    val db = Firebase.firestore
    private var arReview = arrayListOf<RatingData>()
    private lateinit var kost: String


    private var adapterK = adapterReview(arReview)
    private fun siapkanData(){
        arReview.clear()
        db.collection("tbRating")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if(document.getString("namaKost") == kost) {
                        var rev = RatingData(
                            document.getString("review")!!,
                            document.get("rating").toString().toInt(),
                            document.getString("user")!!,
                            document.getString("namaKost")!!
                        )

                        arReview.add(rev)
                    }
                }

                rvReview.layoutManager = LinearLayoutManager(this)
                rvReview.adapter = adapterK

            }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        val star1 = findViewById<ImageView>(R.id.star1)
        val star2 = findViewById<ImageView>(R.id.star2)
        val star3 = findViewById<ImageView>(R.id.star3)
        val star4 = findViewById<ImageView>(R.id.star4)
        val star5 = findViewById<ImageView>(R.id.star5)
        val editReview = findViewById<EditText>(R.id.editReview)
        val btnback = findViewById<Button>(R.id.button4)
        val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)

        kost = intent.getParcelableExtra<Kost>("kirimData")?.nama.toString()
        val sp = getSharedPreferences("data_SP", MODE_PRIVATE)
        var rating = 0
        rvReview = findViewById(R.id.rvReview)
        siapkanData()
        star1.setImageResource(R.drawable.ic_baseline_star_24)
        star2.setImageResource(R.drawable.ic_baseline_star_24)
        star3.setImageResource(R.drawable.ic_baseline_star_24)
        star4.setImageResource(R.drawable.ic_baseline_star_24)
        star5.setImageResource(R.drawable.ic_baseline_star_24)
        star1.setOnClickListener {
            star1.setImageResource(R.drawable.ic_baseline_star_24_yellow)
            star2.setImageResource(R.drawable.ic_baseline_star_24)
            star3.setImageResource(R.drawable.ic_baseline_star_24)
            star4.setImageResource(R.drawable.ic_baseline_star_24)
            star5.setImageResource(R.drawable.ic_baseline_star_24)

            rating = 1
        }
        star2.setOnClickListener {
            star1.setImageResource(R.drawable.ic_baseline_star_24_yellow)
            star2.setImageResource(R.drawable.ic_baseline_star_24_yellow)
            star3.setImageResource(R.drawable.ic_baseline_star_24)
            star4.setImageResource(R.drawable.ic_baseline_star_24)
            star5.setImageResource(R.drawable.ic_baseline_star_24)
            rating = 2
        }
        star3.setOnClickListener {
            star1.setImageResource(R.drawable.ic_baseline_star_24_yellow)
            star2.setImageResource(R.drawable.ic_baseline_star_24_yellow)
            star3.setImageResource(R.drawable.ic_baseline_star_24_yellow)
            star4.setImageResource(R.drawable.ic_baseline_star_24)
            star5.setImageResource(R.drawable.ic_baseline_star_24)

            rating = 3
        }
        star4.setOnClickListener {
            star1.setImageResource(R.drawable.ic_baseline_star_24_yellow)
            star2.setImageResource(R.drawable.ic_baseline_star_24_yellow)
            star3.setImageResource(R.drawable.ic_baseline_star_24_yellow)
            star4.setImageResource(R.drawable.ic_baseline_star_24_yellow)
            star5.setImageResource(R.drawable.ic_baseline_star_24)
            rating = 4
        }
        star5.setOnClickListener {
            star1.setImageResource(R.drawable.ic_baseline_star_24_yellow)
            star2.setImageResource(R.drawable.ic_baseline_star_24_yellow)
            star3.setImageResource(R.drawable.ic_baseline_star_24_yellow)
            star4.setImageResource(R.drawable.ic_baseline_star_24_yellow)
            star5.setImageResource(R.drawable.ic_baseline_star_24_yellow)
            rating = 5
        }
        btnback.setOnClickListener {
            val intent = Intent(this@Review,MainActivity::class.java)
            startActivity(intent)
        }
        buttonSubmit.setOnClickListener {

            var review = editReview.text.toString()
            if(review.isBlank()){
                review = ""
            }
            if(rating > 0){
            val hash = hashMapOf(
                "rating" to rating,
                "review" to review,
                "user" to sp.getString("SPlogin", null),
                "namaKost" to kost
            )
                var bool = true

                var docID = sp.getString("SPlogin", null) + " " + kost
               db.collection("tbRating").document(docID).set(hash)
               siapkanData()

        }else{
            Toast.makeText(this, "Rating harus diisi", Toast.LENGTH_LONG).show()
            }

        }
    }


}