package com.example.minewiki.ui.enchantments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minewiki.R
import com.example.minewiki.data.local.EnchantmentData

class EnchantmentsFragment : Fragment(R.layout.fragment_enchantments) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv = view.findViewById<RecyclerView>(R.id.rvEnchantments)
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = EnchantmentAdapter(EnchantmentData.enchantments)

        view.findViewById<View>(R.id.btnBack).setOnClickListener {
            findNavController().popBackStack()
        }
    }
}