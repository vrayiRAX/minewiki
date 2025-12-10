package com.example.minewiki.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {

    private val _loginState = MutableLiveData<Boolean>()
    val loginState: LiveData<Boolean> = _loginState


    fun login(email: String, pass: String) {
        if (email.isNotEmpty() && pass.isNotEmpty()) {
            _loginState.value = true
        } else {
            _loginState.value = false
        }
    }

    fun register(name: String, email: String, pass: String) {
        if (name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty()) {
            _loginState.value = true
        } else {
            _loginState.value = false
        }
    }
}