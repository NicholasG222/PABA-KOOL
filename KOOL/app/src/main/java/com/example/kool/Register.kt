package com.example.kool

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Register : AppCompatActivity() {

    private lateinit var _registerEmail : EditText
    private lateinit var _registerPassword : EditText
    private lateinit var _registerButton : Button
    private lateinit var _registerPasswordConfirm : EditText
    private lateinit var _textLogin : TextView
    private lateinit var _registerNama: EditText
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        _registerNama = findViewById(R.id.editNama)
        _registerEmail = findViewById(R.id.registerEmail)
        _registerPassword = findViewById(R.id.registerPassword)
        _registerButton = findViewById(R.id.registerButton)
        _registerPasswordConfirm = findViewById(R.id.registerPasswordConfirm)
        _textLogin = findViewById(R.id.textLogin)
        val sp = getSharedPreferences("data_SP", MODE_PRIVATE)
        val db = Firebase.firestore
        firebaseAuth = FirebaseAuth.getInstance()

        _textLogin.setOnClickListener {
            val intent = Intent(this@Register, Login::class.java)
            startActivity(intent)
        }
        _registerButton.setOnClickListener {
            val email = _registerEmail.text.toString()
            val password = _registerPassword.text.toString()
            val confirmPassword = _registerPasswordConfirm.text.toString()
            val nama = _registerNama.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && nama.isNotEmpty()) {
                if (password == confirmPassword){
                    val newUser = User(email, password, nama, ArrayList<Kost>())
                    db.collection("tbUser").document(email).set(newUser)
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                        if (it.isSuccessful){


                            val role = intent.getStringExtra(data)
                            if(role == "Penyewa") {
                                val intent = Intent(this@Register, MainActivity::class.java)
                                startActivity(intent)
                                val editor = sp.edit()
                                editor.putString("SPlogin", email)
                                editor.apply()
                            }else{
                                val intent = Intent(this@Register, TambahKos::class.java)
                                startActivity(intent)
                                val editor = sp.edit()
                                editor.putString("SPlogin2", email)
                                editor.apply()
                            }
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Password tidak sama",Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Tidak boleh ada bagian yang kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }
    companion object{
        const val data = "data"
    }
}