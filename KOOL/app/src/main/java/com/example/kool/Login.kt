package com.example.kool

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson

class Login : AppCompatActivity() {

    private lateinit var _loginEmail :EditText
    private lateinit var _loginPassword :EditText
    private lateinit var _loginButton :Button
    private lateinit var _textRegister : TextView

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dataTerima: String
    lateinit var sp: SharedPreferences
    lateinit var sp2: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        _loginEmail = findViewById(R.id.loginEmail)
        _loginPassword = findViewById(R.id.loginPassword)
        _loginButton = findViewById(R.id.loginButton)
        _textRegister = findViewById(R.id.textRegister)
        dataTerima = intent.getStringExtra(data).toString()
        firebaseAuth = FirebaseAuth.getInstance()
        sp = getSharedPreferences("data_SP", MODE_PRIVATE)

        _textRegister.setOnClickListener {
            val intent = Intent (this@Login, Register::class.java).apply{
                putExtra(Register.data, dataTerima)
            }
            startActivity(intent)
        }

        _loginButton.setOnClickListener {
            val email = _loginEmail.text.toString()
            val password = _loginPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                    if (it.isSuccessful){
                        if(dataTerima == "Penyewa") {
                            val editor = sp.edit()
                            editor.putString("SPlogin", email)
                            editor.apply()
                            val intent = Intent(this@Login, MainActivity::class.java)
                            startActivity(intent)


                        }else{
                            val editor = sp.edit()
                            editor.putString("SPlogin2", email)
                            editor.apply()
                            val intent = Intent(this@Login, TambahKos::class.java)
                            startActivity(intent)

                        }

                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }else {
                Toast.makeText(this, "Tidak boleh ada bagian yang kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null){

            if(dataTerima == "Penyewa") {

                val intent = Intent(this@Login, MainActivity::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this@Login, TambahKos::class.java)
                startActivity(intent)
            }
        }
    }
    companion object{
        const val data = "data"
    }
}