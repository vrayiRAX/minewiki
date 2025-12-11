package com.example.minewiki.ui.home

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.minewiki.R
import com.example.minewiki.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cardProfile = view.findViewById<View>(R.id.cardProfile)
        val imgNavProfile = view.findViewById<ImageView>(R.id.imgNavProfile)
        val btnMenu = view.findViewById<ImageButton>(R.id.btnNavMenu)
        val tvWelcome = view.findViewById<TextView>(R.id.tvWelcomeUser)
        val contentLayout = view.findViewById<View>(R.id.scrollViewContent)
        val tvDailyTip = view.findViewById<TextView>(R.id.tvDailyTip)

        val layoutJava = view.findViewById<LinearLayout>(R.id.layoutJava)
        val tvJavaVersion = view.findViewById<TextView>(R.id.tvJavaVersion)
        val layoutBedrock = view.findViewById<LinearLayout>(R.id.layoutBedrock)
        val tvBedrockVersion = view.findViewById<TextView>(R.id.tvBedrockVersion)
        val sharedPref = requireActivity().getSharedPreferences("MineWikiData", 0)
        val userId = sharedPref.getInt("current_user_id", -1)

        // Nombre de usuario
        val userName = sharedPref.getString("current_user_name", "Explorador") ?: "Explorador"

        // Cargar foto guardada
        val savedImage = sharedPref.getString("profile_image_$userId", null)
        if (savedImage != null) {
            imgNavProfile.load(savedImage)
        }

        tvWelcome.text = "¡HOLA ${userName.uppercase()}!"
        val animation = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.fade_in_up)

        contentLayout?.startAnimation(animation)
        cardProfile.startAnimation(animation)

        // MICROSERVICIO
        if (tvDailyTip != null) {
            lifecycleScope.launch {
                try {
                    tvDailyTip.text = "Consultando a la Mesa de Encantamientos..."
                    val respuesta = com.example.minewiki.data.remote.MicroserviceClient.instance.obtenerConsejo()
                    tvDailyTip.text = respuesta.mensaje

                } catch (e: java.net.UnknownHostException) {
                    tvDailyTip.text = "Sin conexión a internet :("
                } catch (e: Exception) {
                    tvDailyTip.text = "Inicia el servidor (Main.kt) para ver consejos."
                }
            }
        }

        // NAVEGACIÓN VERSIONES
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
        popup.menu.add(0, 2, 0, "Mesa de Crafteo")
        popup.menu.add(0, 2, 0, "Cerrar Sesión")

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                1 -> {
                    findNavController().navigate(R.id.action_home_to_blocks)
                    true
                }
                2 -> {
                    findNavController().navigate(R.id.action_home_to_recipes)
                    true
                }
                3 -> {
                val sharedPref = requireActivity().getSharedPreferences("MineWikiData", 0)
                sharedPref.edit().remove("current_user_id").apply()
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