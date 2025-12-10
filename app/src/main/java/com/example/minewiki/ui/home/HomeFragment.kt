package com.example.minewiki.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope // <--- IMPORTANTE
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.minewiki.R
import com.example.minewiki.viewmodel.HomeViewModel
import kotlinx.coroutines.launch // <--- IMPORTANTE

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- REFERENCIAS UI ---
        val cardProfile = view.findViewById<View>(R.id.cardProfile)
        val imgNavProfile = view.findViewById<ImageView>(R.id.imgNavProfile)
        val btnMenu = view.findViewById<ImageButton>(R.id.btnNavMenu)
        val tvWelcome = view.findViewById<TextView>(R.id.tvWelcomeUser)

        // Cajas de Versiones
        val layoutJava = view.findViewById<LinearLayout>(R.id.layoutJava)
        val tvJavaVersion = view.findViewById<TextView>(R.id.tvJavaVersion)
        val layoutBedrock = view.findViewById<LinearLayout>(R.id.layoutBedrock)
        val tvBedrockVersion = view.findViewById<TextView>(R.id.tvBedrockVersion)

        // --- SOLUCIÓN ERROR 1: Definimos la variable tvDailyTip ---
        // Asegúrate de que en fragment_home.xml exista un TextView con id="@+id/tvDailyTip"
        val tvDailyTip = view.findViewById<TextView>(R.id.tvDailyTip)

        // --- CARGAR FOTO PERFIL ---
        val sharedPref = requireActivity().getSharedPreferences("MineWikiData", 0)
        val savedImage = sharedPref.getString("profile_image", null)
        if (savedImage != null) {
            imgNavProfile.load(savedImage)
        }

        // --- NOMBRE USUARIO ---
        homeViewModel.text.observe(viewLifecycleOwner) {
            tvWelcome.text = "¡HOLA EXPLORADOR!"
        }

        // --- MICROSERVICIO (SOLUCIÓN ERROR 2 y 3) ---
        // Usamos lifecycleScope.launch para crear la Corrutina
        if (tvDailyTip != null) { // Verificamos que exista para que no crashee si olvidaste el XML
            lifecycleScope.launch {
                try {
                    // Mensaje de carga inicial
                    tvDailyTip.text = "Consultando a la Mesa de Encantamientos..."

                    // Llamada al servidor (Función Suspendida)
                    val respuesta = com.example.minewiki.data.remote.MicroserviceClient.instance.obtenerConsejo()

                    // Mostrar resultado
                    tvDailyTip.text = respuesta.mensaje
                } catch (e: Exception) {
                    Log.e("MicroError", "Fallo al conectar: ${e.message}")
                    tvDailyTip.text = "Inicia el servidor (Main.kt) para ver consejos."
                }
            }
        }

        // --- CLICKS ---
        cardProfile.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_profile)
        }

        btnMenu.setOnClickListener { clickedView ->
            showMainMenu(clickedView)
        }

        layoutJava.setOnClickListener { clickedView ->
            val versionesJava = listOf("1.21.1 (Actual)", "1.20.6", "1.19.4", "1.16.5", "1.8.9 (PvP)")
            showVersionSelector(clickedView, tvJavaVersion, versionesJava)
        }

        layoutBedrock.setOnClickListener { clickedView ->
            val versionesBedrock = listOf("1.21.30 (Actual)", "1.21.20", "1.21.0", "Preview Beta")
            showVersionSelector(clickedView, tvBedrockVersion, versionesBedrock)
        }
    }

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

    private fun showVersionSelector(view: View, targetTextView: TextView, versions: List<String>) {
        val popup = PopupMenu(context, view)
        versions.forEachIndexed { index, version ->
            popup.menu.add(0, index, 0, version)
        }
        popup.setOnMenuItemClickListener { menuItem ->
            targetTextView.text = menuItem.title.toString().split(" ")[0]
            Toast.makeText(context, "Versión: ${menuItem.title}", Toast.LENGTH_SHORT).show()
            true
        }
        popup.show()
    }
}