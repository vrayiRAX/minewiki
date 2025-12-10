package com.example.minewiki.ui.wiki

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.minewiki.R
import com.example.minewiki.data.model.ApiItem

class BlocksAdapter(private var blockList: List<ApiItem>) :
    RecyclerView.Adapter<BlocksAdapter.BlockViewHolder>() {

    class BlockViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvBlockName)
        val tvDesc: TextView = view.findViewById(R.id.tvBlockDesc)
        val imgBlock: ImageView = view.findViewById(R.id.imgBlock)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_block, parent, false)
        return BlockViewHolder(view)
    }

    override fun onBindViewHolder(holder: BlockViewHolder, position: Int) {
        val item = blockList[position]

        // 1. Nombre visible (Ej: "Diamond Sword")
        holder.tvName.text = item.displayName
        holder.tvDesc.text = "ID: ${item.id}"

        // 2. Limpieza del nombre para la URL (Ej: "diamond_sword")
        val cleanName = item.name
            .replace("minecraft:", "")
            .trim()
            .lowercase()

        // 3. URL DE LA IMAGEN (Probamos con la versión 1.19.1)
        val imageUrl = "https://raw.githubusercontent.com/PrismarineJS/minecraft-assets/master/data/1.19.1/items/$cleanName.png"

        // 4. CARGAR CON COIL + DETECCIÓN DE ERRORES
        holder.imgBlock.load(imageUrl) {
            crossfade(true)
            placeholder(android.R.drawable.ic_menu_rotate) // Cargando...

            // Si falla, mostramos un icono rojo y avisamos al Logcat
            error(android.R.drawable.stat_notify_error)
            listener(
                onSuccess = { _, _ ->
                    Log.d("WikiCarga", "¡ÉXITO! Cargó imagen: $cleanName")
                },
                onError = { _, result ->
                    Log.e("WikiError", "FALLÓ $cleanName. Razón: ${result.throwable.message}")
                    // Intento secundario: A veces los bloques están en otra carpeta
                    val blockUrl = "https://raw.githubusercontent.com/PrismarineJS/minecraft-assets/master/data/1.19.1/blocks/$cleanName.png"
                    holder.imgBlock.load(blockUrl)
                }
            )
        }
    }

    override fun getItemCount() = blockList.size

    fun updateData(newItems: List<ApiItem>) {
        blockList = newItems
        notifyDataSetChanged()
    }
}