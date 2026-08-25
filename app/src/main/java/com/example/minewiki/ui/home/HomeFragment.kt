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
        if (!savedImage.isNullOrEmpty()) {
            val file = java.io.File(savedImage)
            if (file.exists()) {
                imgNavProfile.load(file)
            } else {
                imgNavProfile.load(savedImage)
            }
        }

        tvWelcome.text = "¡HOLA ${userName.uppercase()}!"
        val animation = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.fade_in_up)

        contentLayout?.startAnimation(animation)
        cardProfile.startAnimation(animation)

        // MICROSERVICIO DE CONSEJOS Y NOTICIAS CON IA
        if (tvDailyTip != null) {
            lifecycleScope.launch {
                val listaConsejosLocal = listOf(
                    "Nunca caves directamente hacia abajo, podrías caer en lava.",
                    "Un bloque recorrido en el Nether equivale a 8 bloques en el Overworld.",
                    "Los gatos espantan a los Creepers y Phantoms.",
                    "Necesitas 15 estanterías alrededor de una mesa de encantamientos para el nivel 30.",
                    "La obsidiana tarda 9.4 segundos en romperla con pico de diamante.",
                    "El hielo azul es el bloque más rápido para viajar en bote.",
                    "Los Piglins no te atacan si llevas al menos una pieza de armadura de oro.",
                    "El Warden es ciego y te detecta por las vibraciones y el olor.",
                    "La Soul Sand te hace caminar más lento, pero la Soul Soil no."
                )
                try {
                    val respuesta = com.example.minewiki.data.remote.MicroserviceClient.instance.obtenerConsejo()
                    tvDailyTip.text = respuesta.mensaje
                } catch (e: Exception) {
                    // Fallback local automático si cambia la red/IP o no hay servidor disponible
                    tvDailyTip.text = listaConsejosLocal.random()
                }
            }
        }

        // Cargar Noticias IA de Minecraft en el apartado Destacados
        cargarNoticiasIA(view)

        // NAVEGACIÓN VERSIONES
        cardProfile.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_profile)
        }

        val cardAiChat = view.findViewById<View>(R.id.cardAiChat)
        cardAiChat?.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_chat)
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
        popup.menu.add(0, 3, 0, "Lista de Mobs")
        popup.menu.add(0, 4, 0, "Encantamientos")
        popup.menu.add(0, 5, 0, "🤖 Asistente IA (Chatbot)")
        popup.menu.add(0, 6, 0, "Cerrar Sesión")


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
                    findNavController().navigate(R.id.mobsFragment); true
                }
                4 -> {
                    findNavController().navigate(R.id.enchantmentsFragment)
                    true
                }
                5 -> {
                    findNavController().navigate(R.id.action_home_to_chat)
                    true
                }
                6 -> {
                    val sharedPref = requireActivity().getSharedPreferences("MineWikiData", android.content.Context.MODE_PRIVATE)
                    sharedPref.edit().clear().apply()

                    val sesionPref = requireActivity().getSharedPreferences("MineWikiSesion", android.content.Context.MODE_PRIVATE)
                    sesionPref.edit().clear().apply()

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

    private fun cargarNoticiasIA(view: View) {
        val layoutNewsContainer = view.findViewById<LinearLayout>(R.id.layoutNewsContainer) ?: return

        // Noticias de IA por defecto (Snapshots 1.21.4, Pale Garden, Bundles) para mostrar de inmediato
        val noticiasIniciales = listOf(
            com.example.minewiki.MinecraftNewsDto(
                id = 1,
                title = "NUEVO BIOMA: PALE GARDEN Y THE CREAKING",
                tag = "SNAPSHOT 24w38a",
                version = "1.21.4 Snapshot",
                summary = "Llega el bioma bosque pálido con madera de Pale Oak y el mob Creaking que solo ataca cuando no lo miras.",
                date = "IA Reciente"
            ),
            com.example.minewiki.MinecraftNewsDto(
                id = 2,
                title = "¡LOS BUNDLES LLEGAN OFICIALMENTE!",
                tag = "ACTUALIZACIÓN 1.21.2",
                version = "Java & Bedrock",
                summary = "Los sacos de inventario (Bundles) ya están disponibles en todas las plataformas para organizar tus items fácilmente.",
                date = "IA Reciente"
            ),
            com.example.minewiki.MinecraftNewsDto(
                id = 3,
                title = "TRIAL CHAMBERS Y CRAFTER AUTOMÁTICO",
                tag = "TRICKY TRIALS",
                version = "1.21.0 Final",
                summary = "Explora las cámaras de desafío, combate al Breeze y automatiza tus crafteos con el nuevo bloque Crafter de Redstone.",
                date = "IA Destacado"
            ),
            com.example.minewiki.MinecraftNewsDto(
                id = 4,
                title = "ARMADURAS DE LOBO TEÑIBLES Y MOLDES",
                tag = "BEDROCK BETA",
                version = "1.21.40 Preview",
                summary = "Equipa a tus lobos domesticados con armaduras de escamas de armadillo teñibles con tintes de cualquier color.",
                date = "IA Beta"
            )
        )

        // Reemplazar inmediatamente las tarjetas estáticas anteriores por las de la IA
        if (isAdded) {
            layoutNewsContainer.removeAllViews()
            for (news in noticiasIniciales) {
                layoutNewsContainer.addView(crearCardNoticia(news))
            }
        }

        // Consultar servidor en segundo plano para actualizar en vivo
        lifecycleScope.launch {
            try {
                com.example.minewiki.CamionDeEnvios.aiServicio.obtenerNoticiasIA().enqueue(object : retrofit2.Callback<com.example.minewiki.NewsResponseDto> {
                    override fun onResponse(
                        call: retrofit2.Call<com.example.minewiki.NewsResponseDto>,
                        response: retrofit2.Response<com.example.minewiki.NewsResponseDto>
                    ) {
                        val noticiasServidor = response.body()?.news
                        if (!noticiasServidor.isNullOrEmpty() && isAdded) {
                            layoutNewsContainer.removeAllViews()
                            for (news in noticiasServidor) {
                                layoutNewsContainer.addView(crearCardNoticia(news))
                            }
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<com.example.minewiki.NewsResponseDto>, t: Throwable) {
                        // Mantener noticias de IA iniciales
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun crearCardNoticia(news: com.example.minewiki.MinecraftNewsDto): View {
        val card = androidx.cardview.widget.CardView(requireContext()).apply {
            val params = LinearLayout.LayoutParams(
                dpToPx(280),
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                marginEnd = dpToPx(16)
            }
            layoutParams = params
            radius = dpToPx(8).toFloat()
            setCardBackgroundColor(android.graphics.Color.parseColor("#1E1E1E"))
            cardElevation = dpToPx(4).toFloat()
        }

        val container = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dpToPx(14), dpToPx(14), dpToPx(14), dpToPx(14))
        }

        val tagTv = TextView(requireContext()).apply {
            text = "⚡ ${news.tag?.uppercase() ?: "NOTICIA"}"
            setTextColor(android.graphics.Color.parseColor("#55FF55"))
            textSize = 11f
            setTypeface(null, android.graphics.Typeface.BOLD)
        }

        val titleTv = TextView(requireContext()).apply {
            text = news.title ?: ""
            setTextColor(android.graphics.Color.WHITE)
            textSize = 14f
            setTypeface(null, android.graphics.Typeface.BOLD)
            setPadding(0, dpToPx(4), 0, dpToPx(4))
        }

        val summaryTv = TextView(requireContext()).apply {
            text = news.summary ?: ""
            setTextColor(android.graphics.Color.parseColor("#CCCCCC"))
            textSize = 12f
            maxLines = 3
        }

        val verTv = TextView(requireContext()).apply {
            text = "Versión: ${news.version ?: "Actual"} | ${news.date ?: "Reciente"}"
            setTextColor(android.graphics.Color.parseColor("#888888"))
            textSize = 10f
            setPadding(0, dpToPx(6), 0, 0)
        }

        container.addView(tagTv)
        container.addView(titleTv)
        container.addView(summaryTv)
        container.addView(verTv)
        card.addView(container)

        card.setOnClickListener {
            Toast.makeText(context, "${news.title}\n\n${news.summary}", Toast.LENGTH_LONG).show()
        }

        return card
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}