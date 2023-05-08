package com.example.kool

data class User(
    val email: String?,
    val pass: String?,
    val nama: String?,
    val kostSewa: MutableList<Kost>
)
