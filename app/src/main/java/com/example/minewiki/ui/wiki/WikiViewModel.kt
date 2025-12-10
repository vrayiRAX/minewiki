package com.example.minewiki.ui.wiki

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minewiki.data.model.ApiItem
import com.example.minewiki.data.remote.RetrofitClient
import kotlinx.coroutines.launch

class WikiViewModel : ViewModel() {

    private val _items = MutableLiveData<List<ApiItem>>()
    val items: LiveData<List<ApiItem>> = _items
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        fetchMinecraftItems()
    }

    private fun fetchMinecraftItems() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.instance.getItems()
                _items.value = response.take(100)

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}