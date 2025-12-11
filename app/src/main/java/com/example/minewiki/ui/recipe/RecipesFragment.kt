package com.example.minewiki.ui.recipes

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minewiki.R
import com.example.minewiki.data.local.RecipeData // Importamos nuestros datos locales

class RecipesFragment : Fragment(R.layout.fragment_recipes) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvRecipes = view.findViewById<RecyclerView>(R.id.rvRecipes)

        // Configuramos el RecyclerView
        rvRecipes.layoutManager = LinearLayoutManager(context)

        // CARGAMOS LOS DATOS LOCALES DIRECTAMENTE
        // Ya no necesitamos llamar al servidor ni usar try-catch
        rvRecipes.adapter = RecipeAdapter(RecipeData.recipes)
    }
}