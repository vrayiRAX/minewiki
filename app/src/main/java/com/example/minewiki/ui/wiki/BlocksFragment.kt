package com.example.minewiki.ui.wiki

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager // Usaremos Grid para que se vea mejor
import androidx.recyclerview.widget.RecyclerView
import com.example.minewiki.R

class BlocksFragment : Fragment(R.layout.fragment_blocks) {

    private val viewModel: WikiViewModel by viewModels() // Usamos el nuevo ViewModel
    private lateinit var adapter: BlocksAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerBlocks)

        // Configuramos el RecyclerView (Ahora en 2 columnas estilo Grid)
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        adapter = BlocksAdapter(emptyList()) // Empezamos vacíos
        recyclerView.adapter = adapter

        // Observamos los datos de Internet
        viewModel.items.observe(viewLifecycleOwner) { listaDeItems ->
            adapter.updateData(listaDeItems)
        }

        // (Opcional) Mensaje de error si la lista llega vacía
        viewModel.isLoading.observe(viewLifecycleOwner) { cargando ->
            if (cargando) {
                Toast.makeText(context, "Descargando bloques...", Toast.LENGTH_SHORT).show()
            }
        }
    }
}