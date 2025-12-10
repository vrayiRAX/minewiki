package com.example.minewiki.data.remote

import com.example.minewiki.data.model.ApiItem
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface MinecraftApiService {

    // Esta es la dirección exacta donde está la lista de items (JSON)
    @GET("PrismarineJS/minecraft-data/master/data/pc/1.19/items.json")
    suspend fun getItems(): List<ApiItem>
}

// Objeto Singleton para usar la conexión en toda la app
object RetrofitClient {
    private const val BASE_URL = "https://raw.githubusercontent.com/"

    val instance: MinecraftApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MinecraftApiService::class.java)
    }
}