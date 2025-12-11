package com.example.myserver

import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

data class Consejo(val mensaje: String)

fun main() {
            //sacar ip   ipconfig
    embeddedServer(Netty, port = 8080, host = "192.168.1.5")  {

        install(ContentNegotiation) {
            gson()
        }

        routing {

            get("/") {
                call.respondText("¡Servidor de MineWiki funcionando!")
            }

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
        }
    }.start(wait = true)
}