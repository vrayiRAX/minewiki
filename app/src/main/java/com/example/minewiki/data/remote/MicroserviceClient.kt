package com.example.minewiki.data.remote

import com.example.minewiki.data.model.Consejo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface MicroserviceApi {
    @GET("/consejo")
    suspend fun obtenerConsejo(): Consejo
}

object MicroserviceClient {
    private const val BASE_URL = "http://192.168.1.5:8080/" //sacar ip   ipconfig

    val instance: MicroserviceApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MicroserviceApi::class.java)
    }
}