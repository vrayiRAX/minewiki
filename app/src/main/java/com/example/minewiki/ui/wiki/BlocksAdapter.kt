package com.example.minewiki.ui.wiki

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

        val tvStack: TextView = view.findViewById(R.id.tvStackSize)
        val tvLocation: TextView = view.findViewById(R.id.tvLocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_block, parent, false)
        return BlockViewHolder(view)
    }

    override fun onBindViewHolder(holder: BlockViewHolder, position: Int) {
        val item = blockList[position]
        val cleanName = item.name.replace("minecraft:", "").trim().lowercase()

        // NOMBRE Y ID Y STACK
        holder.tvName.text = item.displayName
        holder.tvDesc.text = "ID: ${item.name}"
        holder.tvStack.text = "STACK: ${item.stackSize}"

        val location = when {
            cleanName.contains("nether") || cleanName.contains("quartz") || cleanName.contains("soul") -> "NETHER"
            cleanName.contains("end_") || cleanName.contains("purpur") || cleanName.contains("chorus") -> "THE END"
            cleanName.contains("diamond") || cleanName.contains("gold") || cleanName.contains("iron") -> "MINAS"
            cleanName.contains("wood") || cleanName.contains("log") || cleanName.contains("leaves") -> "BOSQUES"
            else -> "OVERWORLD"
        }
        holder.tvLocation.text = location


        val colorLocation = when(location) {
            "NETHER" -> 0xFFFF5555.toInt()
            "THE END" -> 0xFFAA00AA.toInt()
            "MINAS" -> 0xFF55FFFF.toInt()
            else -> 0xFF55FF55.toInt()
        }
        holder.tvLocation.setTextColor(colorLocation)


        // CARGAR IMAGEN
        val imageUrl = "https://raw.githubusercontent.com/PrismarineJS/minecraft-assets/master/data/1.19.1/items/$cleanName.png"

        holder.imgBlock.load(imageUrl) {
            crossfade(true)
            placeholder(android.R.drawable.ic_menu_rotate)
            error(android.R.drawable.stat_notify_error)
            listener(
                onError = { _, _ ->
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