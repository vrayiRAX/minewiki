package com.example.minewiki.ui.profile

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.minewiki.R
import com.example.minewiki.data.local.AppDatabase // Si te da error aquí, cambia 'local' por 'db'
import kotlinx.coroutines.launch
import java.io.File

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var imgProfile: ImageView
    private var currentUserId: Int = -1
    private var latestTmpUri: Uri? = null

    // 1. LANZADOR DE GALERÍA
    private val selectFromGallery = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imgProfile.load(it)
            saveProfileImage(it.toString())
        }
    }

    // 2. LANZADOR DE CÁMARA
    private val takePhoto = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess && latestTmpUri != null) {
            imgProfile.load(latestTmpUri)
            saveProfileImage(latestTmpUri.toString())
            Toast.makeText(context, "¡Foto capturada!", Toast.LENGTH_SHORT).show()
        }
    }

    // 3. SOLICITUD DE PERMISO
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            abrirCamara()
        } else {
            Toast.makeText(context, "Se requiere permiso de cámara", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- CORRECCIÓN DE ERRORES AQUÍ ---
        // Agregamos <ImageView>, <TextView> y <Button> para que Kotlin sepa qué son.

        imgProfile = view.findViewById<ImageView>(R.id.imgProfileLarge)

        val tvName = view.findViewById<TextView>(R.id.tvProfileName)
        val tvEmail = view.findViewById<TextView>(R.id.tvProfileEmail)

        val btnGallery = view.findViewById<Button>(R.id.btnGallery)
        val btnCamera = view.findViewById<Button>(R.id.btnCamera)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)

        // ----------------------------------

        // Cargar Usuario
        val sharedPref = requireActivity().getSharedPreferences("MineWikiData", 0)
        currentUserId = sharedPref.getInt("current_user_id", -1)

        if (currentUserId != -1) {
            lifecycleScope.launch {
                try {
                    // Nota: Si 'data.local' te da error, cámbialo a 'data.db'
                    val db = AppDatabase.getDatabase(requireContext())
                    val user = db.userDao().getUserById(currentUserId)
                    if (user != null) {
                        tvName.text = user.name.uppercase()
                        tvEmail.text = user.email
                        val savedImage = sharedPref.getString("profile_image_$currentUserId", null)
                        if (savedImage != null) {
                            imgProfile.load(Uri.parse(savedImage))
                        }
                    }
                } catch (e: Exception) {}
            }
        }

        // ACCIONES DE BOTONES
        btnGallery.setOnClickListener {
            selectFromGallery.launch("image/*")
        }

        btnCamera.setOnClickListener {
            // Verificar permiso antes de abrir
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                abrirCamara()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }

        btnLogout.setOnClickListener {
            sharedPref.edit().remove("current_user_id").apply()
            try {
                findNavController().navigate(R.id.loginFragment)
            } catch (e: Exception) {
                findNavController().popBackStack()
            }
        }
    }

    private fun abrirCamara() {
        try {
            val tmpFile = File.createTempFile("tmp_image_file", ".png", requireContext().cacheDir).apply {
                createNewFile()
                deleteOnExit()
            }

            // Usamos packageName para evitar errores de escritura
            val authority = "${requireContext().packageName}.fileprovider"

            latestTmpUri = FileProvider.getUriForFile(
                requireContext(),
                authority,
                tmpFile
            )
            takePhoto.launch(latestTmpUri)
        } catch (e: Exception) {
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun saveProfileImage(uriString: String) {
        if (currentUserId != -1) {
            val sharedPref = requireActivity().getSharedPreferences("MineWikiData", 0)
            sharedPref.edit().putString("profile_image_$currentUserId", uriString).apply()
        }
    }
}