package com.example.myserver

import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

// Datos que enviaremos (Modelo simple)
data class Consejo(val mensaje: String)

fun main() {
    // Arrancamos el servidor en el puerto 8080
    // host = "0.0.0.0" permite que dispositivos externos (tu celular) se conecten
    embeddedServer(Netty, port = 8080, host = "192.168.1.5")  {

        // Configuramos para que entienda JSON
        install(ContentNegotiation) {
            gson()
        }

        // Definimos las rutas (URLs)
        routing {

            // Cuando alguien entre a http://localhost:8080/
            get("/") {
                call.respondText("¡Servidor de MineWiki funcionando!")
            }

            // Cuando la app pida un consejo: http://localhost:8080/consejo
            get("/consejo") {
                val listaConsejos = listOf(
                    "Nunca caves directamente hacia abajo.",
                    "Lleva un cubo de agua para evitar daño por caída.",
                    "El oro es rápido, pero se rompe muy fácil.",
                    "Usa camas en el Nether para explotar cosas.",
                    "Los gatos espantan a los Creepers.",
                    "La obsidiana tarda 9.4 segundos en picarse con diamante."
                )
                // Elegimos uno al azar
                val consejoRandom = listaConsejos.random()

                // Lo enviamos como respuesta JSON
                call.respond(Consejo(consejoRandom))
            }
        }
    }.start(wait = true)
}