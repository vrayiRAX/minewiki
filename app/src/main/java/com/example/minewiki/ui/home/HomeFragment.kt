package com.example.minewiki.ui.home

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView // Necesario para la imagen del perfil
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load // IMPORTANTE: Librería para cargar la foto
import com.example.minewiki.R
import com.example.minewiki.viewmodel.HomeViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cardProfile = view.findViewById<View>(R.id.cardProfile)
        val imgNavProfile = view.findViewById<ImageView>(R.id.imgNavProfile)
        val btnMenu = view.findViewById<ImageButton>(R.id.btnNavMenu)
        val tvWelcome = view.findViewById<TextView>(R.id.tvWelcomeUser)
        val layoutJava = view.findViewById<LinearLayout>(R.id.layoutJava)
        val tvJavaVersion = view.findViewById<TextView>(R.id.tvJavaVersion)
        val layoutBedrock = view.findViewById<LinearLayout>(R.id.layoutBedrock)
        val tvBedrockVersion = view.findViewById<TextView>(R.id.tvBedrockVersion)
        val sharedPref = requireActivity().getSharedPreferences("MineWikiData", 0)
        val savedImage = sharedPref.getString("profile_image", null)
        val imgBackground = view.findViewById<ImageView>(R.id.imgBackground)
        val urlFondo = "https://images.hdqwalls.com/wallpapers/minecraft-rtx-4k-si.jpg"

        imgBackground.load(urlFondo) {
            crossfade(true)
            error(R.drawable.bg_login)
        }

        if (savedImage != null) {
            imgNavProfile.load(savedImage)
        }

        //NOMBRE DE USUARIO
        homeViewModel.text.observe(viewLifecycleOwner) {
            tvWelcome.text = "¡HOLA EXPLORADOR!"
        }

        // CLICKS DEL NAVBAR
        cardProfile.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_profile)
        }

        btnMenu.setOnClickListener { clickedView ->
            showMainMenu(clickedView)
        }

        // CLICK EN VERSIÓN JAVA
        layoutJava.setOnClickListener { clickedView ->
            val versionesJava = listOf("1.21.1 (Actual)", "1.20.6", "1.19.4", "1.16.5", "1.8.9 (PvP)")
            showVersionSelector(clickedView, tvJavaVersion, versionesJava)
        }

        // CLICK EN VERSIÓN BEDROCK
        layoutBedrock.setOnClickListener { clickedView ->
            val versionesBedrock = listOf("1.21.30 (Actual)", "1.21.20", "1.21.0", "Preview Beta")
            showVersionSelector(clickedView, tvBedrockVersion, versionesBedrock)
        }
    }

    // Menú Principal
    private fun showMainMenu(view: View) {
        val popup = PopupMenu(context, view)
        popup.menu.add(0, 1, 0, "Wiki de Bloques")
        popup.menu.add(0, 2, 0, "Cerrar Sesión")

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                1 -> {
                    findNavController().navigate(R.id.action_home_to_blocks)
                    true
                }
                2 -> {
                    findNavController().navigate(R.id.action_home_to_login)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    // Menu para seleccionar versiones
    private fun showVersionSelector(view: View, targetTextView: TextView, versions: List<String>) {
        val popup = PopupMenu(context, view)

        versions.forEachIndexed { index, version ->
            popup.menu.add(0, index, 0, version)
        }

        popup.setOnMenuItemClickListener { menuItem ->
            targetTextView.text = menuItem.title.toString().split(" ")[0] // Tomamos solo el número
            Toast.makeText(context, "Versión cambiada a: ${menuItem.title}", Toast.LENGTH_SHORT).show()
            true
        }
        popup.show()
    }
}