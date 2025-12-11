package com.example.minewiki.ui.recipes

import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.minewiki.R
import com.example.minewiki.data.model.Recipe

class RecipeAdapter(private val recipes: List<Recipe>) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    // Usamos la 1.19.1 que tiene nombres modernos (oak_planks, crafting_table)
    private val BASE_URL = "https://raw.githubusercontent.com/PrismarineJS/minecraft-assets/master/data/1.19.1/"

    class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvRecipeName)
        val imgResult: ImageView = view.findViewById(R.id.imgResult)
        val gridLayout: GridLayout = view.findViewById(R.id.gridIngredients)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]

        holder.tvName.text = recipe.name

        // 1. CARGAR RESULTADO (Inteligente)
        cargarImagenInteligente(holder.imgResult, recipe.output_id)

        // 2. CARGAR GRILLA
        holder.gridLayout.removeAllViews()
        val slotSizePx = (35 * Resources.getSystem().displayMetrics.density).toInt()
        val marginPx = (2 * Resources.getSystem().displayMetrics.density).toInt()

        for (ingredientId in recipe.grid) {
            val slotImage = ImageView(holder.itemView.context)
            val params = GridLayout.LayoutParams()
            params.width = slotSizePx
            params.height = slotSizePx
            params.setMargins(marginPx, marginPx, marginPx, marginPx)
            slotImage.layoutParams = params
            slotImage.setBackgroundColor(0xFF8B8B8B.toInt()) // Gris
            slotImage.setPadding(8, 8, 8, 8)
            slotImage.scaleType = ImageView.ScaleType.FIT_CENTER

            if (ingredientId != "air") {
                cargarImagenInteligente(slotImage, ingredientId)
            }
            holder.gridLayout.addView(slotImage)
        }
    }

    // --- MAGIA: Intenta Items, si falla, intenta Blocks ---
    private fun cargarImagenInteligente(imageView: ImageView, id: String) {
        val urlItem = "${BASE_URL}items/$id.png"
        val urlBlock = "${BASE_URL}blocks/$id.png"

        // Intento 1: Buscar en ITEMS
        imageView.load(urlItem) {
            crossfade(true)
            placeholder(android.R.drawable.ic_menu_rotate)

            // SI FALLA (listener onError), intentamos buscar en BLOCKS
            listener(
                onError = { _, _ ->
                    // Intento 2: Buscar en BLOCKS
                    imageView.load(urlBlock) {
                        // Si falla aquí también, ponemos error rojo
                        error(android.R.drawable.stat_notify_error)
                        listener(onError = { _, res ->
                            Log.e("WIKI_FAIL", "No se encontró '$id' en items ni blocks. Verifica el nombre.")
                        })
                    }
                }
            )
        }
    }

    override fun getItemCount() = recipes.size
}