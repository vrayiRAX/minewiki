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

    // Variable para controlar si está cargando (para poner un progress bar si quieres)
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        fetchMinecraftItems()
    }

    private fun fetchMinecraftItems() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // LLAMADA A LA API (Internet)
                val response = RetrofitClient.instance.getItems()

                // Filtramos un poco para que no sean 1000 items de golpe (opcional)
                _items.value = response.take(100)

            } catch (e: Exception) {
                // Manejo de error (Sin internet, etc)
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}