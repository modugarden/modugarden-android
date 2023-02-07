package com.example.modugarden.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.modugarden.data.Category
import com.example.modugarden.data.Signup

class SignupViewModel: ViewModel() {
    private val inEmail = mutableStateOf("")
    private val inPassword = mutableStateOf("")
    private val inIsTermsCheck = mutableStateOf(false)
    private val inName = mutableStateOf("")
    private val inBirthday = mutableStateOf("1990/12/20")
    private val inCategory = mutableStateOf(listOf<Boolean>())
    private val inSocial = mutableStateOf(false)
    private val inCert = mutableStateOf("!!!")

    fun saveEmail(email: String) {
        inEmail.value = email
    }
    fun savePassword(password: String) {
        inPassword.value = password
    }
    fun saveIsTermsCheck(isTermsCheck: Boolean) {
        inIsTermsCheck.value = isTermsCheck
    }
    fun saveName(name: String) {
        inName.value = name
    }
    fun saveBirthday(birthday: String) {
        inBirthday.value = birthday
    }
    fun saveCategory(category: List<Boolean>) {
        inCategory.value = category
    }
    fun saveSocial(social: Boolean) {
        inSocial.value = social
    }
    fun saveCert(cert: String) {
        inCert.value = cert
    }
    fun getAllData(): Signup {
        return Signup(inEmail.value, inPassword.value, inIsTermsCheck.value, inName.value, inBirthday.value, inCategory.value, inSocial.value, inCert.value)
    }
}