package com.example.minewiki.ui.mobs

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.minewiki.R
import com.example.minewiki.data.model.Mob

class MobAdapter(private val mobs: List<Mob>) : RecyclerView.Adapter<MobAdapter.MobViewHolder>() {

    class MobViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvMobName)
        val tvDesc: TextView = view.findViewById(R.id.tvMobDesc)
        val tvHealth: TextView = view.findViewById(R.id.tvMobHealth)
        val tvType: TextView = view.findViewById(R.id.tvMobType)
        val tvDrops: TextView = view.findViewById(R.id.tvMobDrops)
        val tvSpawn: TextView = view.findViewById(R.id.tvMobSpawn)
        val imgMob: ImageView = view.findViewById(R.id.imgMob)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MobViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mob, parent, false)
        return MobViewHolder(view)
    }

    override fun onBindViewHolder(holder: MobViewHolder, position: Int) {
        val mob = mobs[position]
        holder.tvName.text = mob.name
        holder.tvDesc.text = mob.description
        holder.tvHealth.text = "❤ ${mob.health} HP"
        holder.tvType.text = mob.type
        holder.tvDrops.text = "🎁 Drops: ${mob.drops}"
        holder.tvSpawn.text = "🌎 Spawn: ${mob.spawn}"

        when (mob.type) {
            "HOSTIL" -> holder.tvType.setBackgroundColor(Color.parseColor("#FF5555"))
            "PASIVO" -> holder.tvType.setBackgroundColor(Color.parseColor("#55FF55"))
            else -> holder.tvType.setBackgroundColor(Color.parseColor("#FFAA00"))
        }

        holder.imgMob.setImageResource(mob.imageRes)
        holder.imgMob.scaleType = ImageView.ScaleType.FIT_CENTER
    }

    override fun getItemCount() = mobs.size
}