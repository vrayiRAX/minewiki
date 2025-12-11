package com.example.minewiki.ui.recipes

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController // Importar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minewiki.R
import com.example.minewiki.data.local.RecipeData

class RecipesFragment : Fragment(R.layout.fragment_recipes) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvRecipes = view.findViewById<RecyclerView>(R.id.rvRecipes)
        rvRecipes.layoutManager = LinearLayoutManager(context)
        rvRecipes.adapter = RecipeAdapter(RecipeData.recipes)

        // --- SOLO AGREGUÉ ESTO ---
        view.findViewById<View>(R.id.btnBack).setOnClickListener {
            findNavController().popBackStack()
        }
    }
}