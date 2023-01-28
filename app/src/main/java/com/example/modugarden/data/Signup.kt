package com.example.modugarden.data

data class Signup(
    val email: String,
    val password: String,
    val isTermsCheck: Boolean,
    val name: String,
    val birthday: String,
    val category: List<Boolean>,
    val social: Boolean,
    val cert: String
)
