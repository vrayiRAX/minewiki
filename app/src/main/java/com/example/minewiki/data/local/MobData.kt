package com.example.minewiki.data.local

import com.example.minewiki.R
import com.example.minewiki.data.model.Mob

object MobData {
    val mobs = listOf(
        Mob(
            name = "Warden",
            imageRes = R.drawable.warden,
            type = "HOSTIL",
            health = 500,
            description = "El monstruo más fuerte. Es ciego, te detecta por las vibraciones al caminar.",
            drops = "Catalizador de Sculk",
            spawn = "Bioma: Deep Dark (Ciudades Antiguas)"
        ),
        Mob(
            name = "Creeper",
            imageRes = R.drawable.creeper,
            type = "HOSTIL",
            health = 20,
            description = "Se acerca sigilosamente y explota. Si le cae un rayo se vuelve eléctrico.",
            drops = "Pólvora (0-2), Cabeza (si muere por Creeper Eléctrico)",
            spawn = "Superficie (Noche), Cuevas"
        ),
        Mob(
            name = "Zombie",
            imageRes = R.drawable.zombie,
            type = "HOSTIL",
            health = 20,
            description = "Persigue aldeanos y jugadores. Se quema con la luz del sol.",
            drops = "Carne podrida, Zanahoria, Papa, Lingote de Hierro (Raro)",
            spawn = "Cualquier lugar oscuro"
        ),
        Mob(
            name = "Esqueleto",
            imageRes = R.drawable.skeleton,
            type = "HOSTIL",
            health = 20,
            description = "Dispara flechas con gran precisión. Busca sombra para no quemarse.",
            drops = "Huesos, Flechas, Arco (Raro)",
            spawn = "Superficie (Noche), Nether (Soul Sand Valley)"
        ),
        Mob(
            name = "Axolote",
            imageRes = R.drawable.axolote,
            type = "PASIVO",
            health = 14,
            description = "Ataca a guardianes y ahogados. Se hace el muerto para regenerar vida.",
            drops = "Nada (No lo mates :c)",
            spawn = "Cuevas Frondosas (Lush Caves)"
        ),
        Mob(
            name = "Vaca",
            imageRes = R.drawable.cow,
            type = "PASIVO",
            health = 10,
            description = "Fuente esencial de alimento y materiales básicos.",
            drops = "Cuero, Ternera cruda",
            spawn = "Biomas de Llanuras, Montañas"
        ),
        Mob(
            name = "Aldeano",
            imageRes = R.drawable.villager,
            type = "PASIVO",
            health = 20,
            description = "Puede tener oficios y comerciar items. Huye de los zombies.",
            drops = "Nada",
            spawn = "Aldeas"
        ),
        Mob(
            name = "Enderman",
            imageRes = R.drawable.enderman,
            type = "NEUTRAL",
            health = 40,
            description = "Se teletransporta y roba bloques. Se vuelve agresivo si lo miras a los ojos.",
            drops = "Perla de Ender (0-1)",
            spawn = "End, Nether (Warped Forest), Superficie"
        )
    )
}