package com.example.minewiki.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {

    // Variables para saber si el login fue exitoso
    private val _loginState = MutableLiveData<Boolean>()
    val loginState: LiveData<Boolean> = _loginState

    // Función para simular Login
    fun login(email: String, pass: String) {
        if (email.isNotEmpty() && pass.isNotEmpty()) {
            // AQUÍ MÁS ADELANTE CONSULTARÁS LA BASE DE DATOS
            _loginState.value = true // Simula éxito
        } else {
            _loginState.value = false // Simula error (campos vacíos)
        }
    }

    // Función para simular Registro
    fun register(name: String, email: String, pass: String) {
        if (name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty()) {
            // AQUÍ GUARDARÍAS EN LA BASE DE DATOS
            _loginState.value = true
        } else {
            _loginState.value = false
        }
    }
}