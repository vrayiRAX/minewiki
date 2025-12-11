package com.example.minewiki

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
data class UsuarioPaquete(val username: String, val password: String)

data class RespuestaAldeano(val status: String, val message: String)

interface VentanillaApi {
    @POST("register.php")
    fun enviarRegistro(@Body paquete: UsuarioPaquete): Call<RespuestaAldeano>

    @POST("login.php")
    fun loginUser(@Body request: UsuarioPaquete): Call<RespuestaAldeano>
}

object CamionDeEnvios {
    private const val RUTA = "http://192.168.1.5/minewiki_api/"

    val servicio: VentanillaApi by lazy {
        Retrofit.Builder()
            .baseUrl(RUTA)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VentanillaApi::class.java)
    }
}