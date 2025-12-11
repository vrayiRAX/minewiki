package com.example.minewiki.data.local

import com.example.minewiki.data.model.Recipe

object RecipeData {
    val recipes = listOf(
        Recipe(
            name = "Espada de Diamante",
            output_id = "diamond_sword",
            grid = listOf(
                "air", "diamond", "air",
                "air", "diamond", "air",
                "air", "stick", "air"
            )
        ),
        Recipe(
            name = "Mesa de Crafteo",
            output_id = "crafting_table",
            grid = listOf(
                "oak_planks", "oak_planks", "air",
                "oak_planks", "oak_planks", "air",
                "air", "air", "air"
            )
        ),
        Recipe(
            name = "Pico de Hierro",
            output_id = "iron_pickaxe",
            grid = listOf(
                "iron_ingot", "iron_ingot", "iron_ingot",
                "air", "stick", "air",
                "air", "stick", "air"
            )
        ),
        Recipe(
            name = "Antorcha",
            output_id = "torch",
            grid = listOf(
                "air", "coal", "air",
                "air", "stick", "air",
                "air", "air", "air"
            )
        ),
        Recipe(
            name = "Cofre",
            output_id = "chest",
            grid = listOf(
                "oak_planks", "oak_planks", "oak_planks",
                "oak_planks", "air", "oak_planks",
                "oak_planks", "oak_planks", "oak_planks"
            )
        ),
        Recipe(
            name = "Horno",
            output_id = "furnace",
            grid = listOf(
                "cobblestone", "cobblestone", "cobblestone",
                "cobblestone", "air", "cobblestone",
                "cobblestone", "cobblestone", "cobblestone"
            )
        )
    )
}