package com.example.myserver

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

data class Consejo(val mensaje: String)

fun main() {
    val aiServiceUrl = System.getenv("AI_SERVICE_URL") ?: "http://localhost:8000"
    val client = HttpClient(CIO) {
        install(io.ktor.client.plugins.HttpTimeout) {
            requestTimeoutMillis = 120_000
            connectTimeoutMillis = 60_000
            socketTimeoutMillis = 120_000
        }
    }

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {

        install(ContentNegotiation) {
            gson()
        }

        routing {

            get("/") {
                call.respondText("¡Servidor Backend Gateway de MineWiki funcionando!")
            }

            // Endpoint clásico de consejos estáticos
            get("/consejo") {
                val listaConsejos = listOf(
                    "Nunca caves directamente hacia abajo.",
                    "Un bloque en el Nether equivale a 8 bloques en el Overworld.",
                    "Los gatos espantan a los Creepers y Phantoms.",
                    "Necesitas 15 librerías para encantar al nivel 30.",
                    "La obsidiana tarda 9.4 segundos en picarse con diamante.",
                    "El hielo azul es el bloque más rápido para viajar en bote.",
                    "Los Piglins te atacarán si no llevas una pieza de armadura de oro.",
                    "Las tortugas sueltan 'Escamas' solo cuando crecen de bebé a adulto.",
                    "El Warden no puede ver, te detecta por las vibraciones.",
                    "Puedes curar a un Aldeano Zombie con una poción de debilidad y una manzana dorada.",
                    "La lluvia daña a los Enderman y a los Blazes.",
                    "Usar una cama en el End o el Nether causa una explosión masiva.",
                    "El encantamiento 'Fortuna' no funciona con hierro ni oro antiguo (raw).",
                    "Los delfines te dan un impulso de velocidad si nadas cerca de ellos.",
                    "La Soul Sand (Arena de Almas) te hace caminar más lento, pero la Soul Soil no.",
                    "Los Axolotes atacan a los calamares y guardianes, y te dan regeneración.",
                    "Un balde de leche elimina cualquier efecto de poción (bueno o malo).",
                    "Las corrientes de agua fluyen 8 bloques antes de detenerse.",
                    "Los Golems de Hierro no te atacarán si fuiste tú quien los construyó.",
                    "El encantamiento 'Toque de Seda' es el único que permite recoger bloques de vidrio intactos.",
                    "Las abejas mueren poco después de picarte, al igual que en la vida real.",
                    "Los biomas de 'Campos de Hongos' son los únicos lugares seguros donde no aparecen monstruos de noche.",
                    "Puedes reparar tus Elytras dañadas usando membranas de fantasma en un yunque."
                )
                val consejoRandom = listaConsejos.random()

                call.respond(Consejo(consejoRandom))
            }

            // --- ENDPOINTS INTEGRADOS DE INTELIGENCIA ARTIFICIAL ---

            // Proxy Chat Asistente IA
            post("/api/ai/chat") {
                try {
                    val body = call.receiveText()
                    val response: HttpResponse = client.post("$aiServiceUrl/api/v1/chat") {
                        contentType(ContentType.Application.Json)
                        setBody(body)
                    }
                    call.respondText(response.bodyAsText(), ContentType.Application.Json, response.status)
                } catch (e: Exception) {
                    call.respondText(
                        """{"reply": "El servidor IA tardó en responder. Intenta enviar tu pregunta de nuevo: ${e.message}", "model_used": "Timeout Fallback", "is_mock": true}""",
                        ContentType.Application.Json,
                        HttpStatusCode.OK
                    )
                }
            }

            // Proxy Tip Dinámico con IA
            get("/api/ai/consejo") {
                try {
                    val category = call.request.queryParameters["category"] ?: ""
                    val url = if (category.isNotEmpty()) "$aiServiceUrl/api/v1/tip?category=$category" else "$aiServiceUrl/api/v1/tip"
                    val response: HttpResponse = client.get(url)
                    call.respondText(response.bodyAsText(), ContentType.Application.Json, response.status)
                } catch (e: Exception) {
                    call.respondText(
                        """{"tip": "Consejo de respaldo: Nunca caves directo abajo.", "source": "Fallback Local"}""",
                        ContentType.Application.Json,
                        HttpStatusCode.OK
                    )
                }
            }

            // Proxy RAG sobre la Wiki
            post("/api/ai/rag") {
                try {
                    val body = call.receiveText()
                    val response: HttpResponse = client.post("$aiServiceUrl/api/v1/rag/ask") {
                        contentType(ContentType.Application.Json)
                        setBody(body)
                    }
                    call.respondText(response.bodyAsText(), ContentType.Application.Json, response.status)
                } catch (e: Exception) {
                    call.respondText(
                        """{"answer": "Error al consultar la Wiki vectorial: ${e.message}", "sources": []}""",
                        ContentType.Application.Json,
                        HttpStatusCode.InternalServerError
                    )
                }
            }

            // Proxy Noticias IA de Minecraft (Snapshots, Versiones)
            get("/api/ai/noticias") {
                try {
                    val response: HttpResponse = client.get("$aiServiceUrl/api/v1/news")
                    call.respondText(response.bodyAsText(), ContentType.Application.Json, response.status)
                } catch (e: Exception) {
                    call.respondText(
                        """{"news": [], "source": "Fallback Local Error"}""",
                        ContentType.Application.Json,
                        HttpStatusCode.OK
                    )
                }
            }

        }
    }.start(wait = true)
}