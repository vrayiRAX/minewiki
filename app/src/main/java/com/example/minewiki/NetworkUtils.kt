package com.example.minewiki

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class UsuarioPaquete(val username: String, val password: String)

data class RespuestaAldeano(val status: String, val message: String)

// DTOs para la API de Inteligencia Artificial
data class ChatHistoryMessage(val role: String, val content: String)
data class AiChatRequest(val message: String, val use_fast_model: Boolean = true, val history: List<ChatHistoryMessage>? = null)
data class AiChatResponse(val reply: String? = null, val model_used: String? = null, val is_mock: Boolean? = false)

data class AiTipResponse(val tip: String? = null, val category: String? = null, val source: String? = null)

data class AiRagRequest(val question: String, val top_k: Int = 3)
data class SourceDocDto(val title: String? = null, val content: String? = null, val score: Float? = 0f)
data class AiRagResponse(val answer: String? = null, val sources: List<SourceDocDto>? = emptyList(), val model_used: String? = null)

data class MinecraftNewsDto(val id: Int = 0, val title: String? = null, val tag: String? = null, val version: String? = null, val summary: String? = null, val date: String? = null)
data class NewsResponseDto(val news: List<MinecraftNewsDto>? = emptyList(), val source: String? = null)

interface VentanillaApi {
    @POST("register.php")
    fun enviarRegistro(@Body paquete: UsuarioPaquete): Call<RespuestaAldeano>

    @POST("login.php")
    fun loginUser(@Body request: UsuarioPaquete): Call<RespuestaAldeano>
}

// Interfaz para la API del Backend Gateway Ktor (Puerto 8080)
interface MineWikiAiApi {
    @POST("api/ai/chat")
    fun enviarMensajeChat(@Body request: AiChatRequest): Call<AiChatResponse>

    @GET("api/ai/consejo")
    fun obtenerConsejoIA(@Query("category") category: String? = null): Call<AiTipResponse>

    @POST("api/ai/rag")
    fun consultarWikiRAG(@Body request: AiRagRequest): Call<AiRagResponse>

    @GET("api/ai/noticias")
    fun obtenerNoticiasIA(): Call<NewsResponseDto>
}

object CamionDeEnvios {
    private const val RUTA = "http://192.168.1.5/minewiki_api/"
    private const val RUTA_KTOR_GATEWAY = "http://192.168.1.100:8080/"

    private val okHttpClient: okhttp3.OkHttpClient by lazy {
        okhttp3.OkHttpClient.Builder()
            .connectTimeout(90, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(90, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(90, java.util.concurrent.TimeUnit.SECONDS)
            .build()
    }

    val servicio: VentanillaApi by lazy {
        Retrofit.Builder()
            .baseUrl(RUTA)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VentanillaApi::class.java)
    }

    val aiServicio: MineWikiAiApi by lazy {
        Retrofit.Builder()
            .baseUrl(RUTA_KTOR_GATEWAY)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MineWikiAiApi::class.java)
    }
}