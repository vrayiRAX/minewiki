package com.example.minewiki.ui.mobs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minewiki.R
import com.example.minewiki.data.local.MobData

class MobsFragment : Fragment(R.layout.fragment_mobs) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvMobs = view.findViewById<RecyclerView>(R.id.rvMobs)
        rvMobs.layoutManager = LinearLayoutManager(context)
        rvMobs.adapter = MobAdapter(MobData.mobs)
    }
}