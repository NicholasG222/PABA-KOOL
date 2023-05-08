package com.example.kool

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*


class MasukkanDataKos : AppCompatActivity() {
    private var SELECT_PICTURE = 0
    lateinit var _editImage: EditText
    private val PICK_IMAGE_REQUEST = 71
    private var filePath : Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    private lateinit var _editAlamat: EditText
    private lateinit var _editFasilitas:EditText
    private lateinit var _editHarga:EditText
    private lateinit var _editNama:EditText
    private lateinit var _editNoWa:EditText
    private lateinit var db:FirebaseFirestore
    private lateinit var downloadUri: Uri

    fun selectImage(){
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "PILIH GAMBAR"), PICK_IMAGE_REQUEST)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_masukkan_data_kos)

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        var sp = getSharedPreferences("data_SP", MODE_PRIVATE)
        // 6 edit text
        _editImage = findViewById<EditText>(R.id.editImage)

       _editNama = findViewById<EditText>(R.id.editNama)
       _editNoWa = findViewById<EditText>(R.id.editNoWA)
       _editAlamat = findViewById<EditText>(R.id.editAlamat)
        _editHarga = findViewById<EditText>(R.id.editHarga)
        _editFasilitas = findViewById<EditText>(R.id.editFasilitas)
        //2 button
        val _buttonTambah = findViewById<Button>(R.id.buttonTambah)
        val _buttonBack = findViewById<Button>(R.id.buttonBack)
        db = Firebase.firestore

        _buttonTambah.setOnClickListener {

            val kost = Kost(sp.getString("SPlogin2", null),_editAlamat.text.toString(), _editFasilitas.text.toString(),_editImage.text.toString(), _editHarga.text.toString().toInt()
                , _editNama.text.toString(), _editNoWa.text.toString(), 0.0)
            kost.nama?.let { it1 -> db.collection("tbKos").document(it1).set(kost)
                .addOnSuccessListener {
                    _editImage.setText("")
                    _editNama.setText("")
                    _editNoWa.setText("")
                    _editAlamat.setText("")
                    _editFasilitas.setText("")
                    _editHarga.setText("")
                    Toast.makeText(this,"Simpan data berhasil",Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this,"Gagal menyimpan data",Toast.LENGTH_LONG).show()
                }
            }
        }
        _buttonBack.setOnClickListener {
            val intent = Intent(this@MasukkanDataKos, TambahKos::class.java)
            startActivity(intent)
        }
    }


}