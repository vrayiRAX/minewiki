package com.example.minewiki.ui.enchantments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.minewiki.R
import com.example.minewiki.data.model.Enchantment

class EnchantmentAdapter(private val list: List<Enchantment>) : RecyclerView.Adapter<EnchantmentAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvDesc: TextView = view.findViewById(R.id.tvDesc)
        val tvLevel: TextView = view.findViewById(R.id.tvLevel)
        val imgIcon: ImageView = view.findViewById(R.id.imgIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_enchantment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.tvName.text = item.name
        holder.tvDesc.text = item.description
        holder.tvLevel.text = "Nvl ${item.maxLevel}"
        holder.imgIcon.setImageResource(item.imageRes)
        holder.imgIcon.scaleType = ImageView.ScaleType.FIT_CENTER
    }

    override fun getItemCount() = list.size
}