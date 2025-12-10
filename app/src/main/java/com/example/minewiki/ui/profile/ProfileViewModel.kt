package com.example.minewiki.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    private val _username = MutableLiveData<String>().apply {
        value = "Steve_Pro_Gamer"
    }
    val username: LiveData<String> = _username

    private val _email = MutableLiveData<String>().apply {
        value = "steve@minecraft.net"
    }
    val email: LiveData<String> = _email

    private val _xpLevel = MutableLiveData<Int>().apply {
        value = 27
    }
    val xpLevel: LiveData<Int> = _xpLevel
}