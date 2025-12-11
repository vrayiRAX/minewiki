package com.example.minewiki.data.local

import com.example.minewiki.R
import com.example.minewiki.data.model.Enchantment

object EnchantmentData {
    val enchantments = listOf(
        Enchantment(
            name = "Reparación (Mending)",
            description = "Repara la durabilidad del objeto usando orbes de experiencia (XP).",
            maxLevel = "I",
            imageRes = R.drawable.libro_encantado
        ),
        Enchantment(
            name = "Irrompibilidad (Unbreaking)",
            description = "Aumenta la durabilidad del objeto para que tarde más en romperse.",
            maxLevel = "III",
            imageRes = R.drawable.libro_encantado
        ),
        Enchantment(
            name = "Protección",
            description = "Reduce el daño recibido de la mayoría de las fuentes.",
            maxLevel = "IV",
            imageRes = R.drawable.libro_encantado
        ),
        Enchantment(
            name = "Protección contra el fuego",
            description = "Reduce el daño por fuego y lava. Reduce el tiempo de quemadura.",
            maxLevel = "IV",
            imageRes = R.drawable.libro_encantado
        ),
        Enchantment(
            name = "Caída de Pluma",
            description = "Reduce el daño por caída y teletransportaciones de Ender Pearl.",
            maxLevel = "IV",
            imageRes = R.drawable.libro_encantado
        ),
        Enchantment(
            name = "Espinas (Thorns)",
            description = "Devuelve parte del daño recibido al atacante.",
            maxLevel = "III",
            imageRes = R.drawable.libro_encantado
        ),
        Enchantment(
            name = "Agilidad Acuática",
            description = "Aumenta la velocidad al caminar bajo el agua.",
            maxLevel = "III",
            imageRes = R.drawable.libro_encantado
        ),
        Enchantment(
            name = "Filo (Sharpness)",
            description = "Aumenta el daño cuerpo a cuerpo contra todo tipo de mobs.",
            maxLevel = "V",
            imageRes = R.drawable.libro_encantado
        ),
        Enchantment(
            name = "Golpeo (Smite)",
            description = "Aumenta el daño extra contra no-muertos (Zombies, Esqueletos).",
            maxLevel = "V",
            imageRes = R.drawable.libro_encantado
        ),
        Enchantment(
            name = "Aspecto Ígneo",
            description = "Prende fuego al objetivo al golpearlo. Cocina la carne animal.",
            maxLevel = "II",
            imageRes = R.drawable.libro_encantado
        ),
        Enchantment(
            name = "Botín (Looting)",
            description = "Aumenta la cantidad de objetos que sueltan los mobs al morir.",
            maxLevel = "III",
            imageRes = R.drawable.libro_encantado
        ),
        Enchantment(
            name = "Empuje (Knockback)",
            description = "Empuja a los enemigos hacia atrás al golpearlos.",
            maxLevel = "II",
            imageRes = R.drawable.libro_encantado
        ),
        Enchantment(
            name = "Eficiencia",
            description = "Aumenta la velocidad al picar, talar o cavar bloques.",
            maxLevel = "V",
            imageRes = R.drawable.libro_encantado
        ),
        Enchantment(
            name = "Fortuna",
            description = "Aumenta la cantidad de items obtenidos al romper minerales (Diamante, Carbón).",
            maxLevel = "III",
            imageRes = R.drawable.libro_encantado
        ),
        Enchantment(
            name = "Toque de Seda",
            description = "Permite recoger el bloque tal cual es (ej: Mena de Diamante, Cristal, Hielo).",
            maxLevel = "I",
            imageRes = R.drawable.libro_encantado
        ),
        Enchantment(
            name = "Poder (Power)",
            description = "Aumenta el daño de las flechas disparadas con arco.",
            maxLevel = "V",
            imageRes = R.drawable.libro_encantado
        ),
        Enchantment(
            name = "Infinidad",
            description = "Al disparar con el arco no se consumen flechas (requiere tener al menos una).",
            maxLevel = "I",
            imageRes = R.drawable.libro_encantado
        ),
        Enchantment(
            name = "Lealtad",
            description = "El tridente regresa a tu mano automáticamente después de lanzarlo.",
            maxLevel = "III",
            imageRes = R.drawable.libro_encantado
        ),
        Enchantment(
            name = "Conductividad",
            description = "Invoca un rayo al golpear a un mob con tridente durante una tormenta.",
            maxLevel = "I",
            imageRes = R.drawable.libro_encantado
        )
    )
}