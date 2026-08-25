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
import com.example.minewiki.data.local.AppDatabase
import kotlinx.coroutines.launch
import java.io.File

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var imgProfile: ImageView
    private var currentUserId: Int = -1
    private var latestTmpUri: Uri? = null

    // GALERÍA
    private val selectFromGallery = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            processAndSaveImage(it)
        }
    }

    // CÁMARA
    private val takePhoto = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess && latestTmpUri != null) {
            processAndSaveImage(latestTmpUri!!)
        }
    }

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

        imgProfile = view.findViewById<ImageView>(R.id.imgProfileLarge)

        val tvName = view.findViewById<TextView>(R.id.tvProfileName)
        val tvEmail = view.findViewById<TextView>(R.id.tvProfileEmail)
        val btnGallery = view.findViewById<Button>(R.id.btnGallery)
        val btnCamera = view.findViewById<Button>(R.id.btnCamera)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        val sharedPref = requireActivity().getSharedPreferences("MineWikiData", 0)
        currentUserId = sharedPref.getInt("current_user_id", -1)

        if (currentUserId != -1) {
            lifecycleScope.launch {
                try {
                    val db = AppDatabase.getDatabase(requireContext())
                    val user = db.userDao().getUserById(currentUserId)
                    if (user != null) {
                        tvName.text = user.name.uppercase()
                        tvEmail.text = user.email

                        // Intentar cargar la foto guardada desde SharedPreferences o Base de datos
                        val savedPath = sharedPref.getString("profile_image_$currentUserId", null) ?: user.profileImage
                        if (!savedPath.isNullOrEmpty()) {
                            val imgFile = File(savedPath)
                            if (imgFile.exists()) {
                                imgProfile.load(imgFile)
                            } else {
                                imgProfile.load(Uri.parse(savedPath))
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        val btnBack = view.findViewById<android.widget.ImageButton>(R.id.btnBack)
        val btnGoHome = view.findViewById<Button>(R.id.btnGoHome)

        btnBack?.setOnClickListener {
            findNavController().popBackStack()
        }

        btnGoHome?.setOnClickListener {
            try {
                findNavController().navigate(R.id.homeFragment)
            } catch (e: Exception) {
                findNavController().popBackStack()
            }
        }

        btnGallery.setOnClickListener {
            selectFromGallery.launch("image/*")
        }

        btnCamera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                abrirCamara()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }

        btnLogout.setOnClickListener {
            val sesionPref = requireActivity().getSharedPreferences("MineWikiSesion", android.content.Context.MODE_PRIVATE)
            sesionPref.edit().clear().apply()
            sharedPref.edit().clear().apply()

            try {
                findNavController().navigate(R.id.loginFragment)
            } catch (e: Exception) {
                findNavController().popBackStack()
            }
        }
    }

    private fun abrirCamara() {
        try {
            val tmpFile = File.createTempFile("tmp_image_file", ".jpg", requireContext().cacheDir).apply {
                createNewFile()
                deleteOnExit()
            }

            val authority = "${requireContext().packageName}.fileprovider"

            latestTmpUri = FileProvider.getUriForFile(
                requireContext(),
                authority,
                tmpFile
            )
            takePhoto.launch(latestTmpUri)
        } catch (e: Exception) {
            Toast.makeText(context, "Error cámara: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun processAndSaveImage(sourceUri: Uri) {
        if (currentUserId == -1) return

        lifecycleScope.launch {
            try {
                // Copiar la imagen a la memoria interna privada de la app para que NUNCA se pierda ni venza el permiso
                val destinationFile = File(requireContext().filesDir, "profile_user_$currentUserId.jpg")

                requireContext().contentResolver.openInputStream(sourceUri)?.use { input ->
                    destinationFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }

                val permanentPath = destinationFile.absolutePath

                // 1. Mostrar en pantalla de inmediato
                imgProfile.load(destinationFile)

                // 2. Guardar ruta permanente en SharedPreferences
                val sharedPref = requireActivity().getSharedPreferences("MineWikiData", 0)
                sharedPref.edit().putString("profile_image_$currentUserId", permanentPath).apply()

                // 3. Guardar ruta en la base de datos Room
                val db = AppDatabase.getDatabase(requireContext())
                db.userDao().updateProfileImage(currentUserId, permanentPath)

                Toast.makeText(context, "¡Foto de perfil guardada exitosamente!", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                Toast.makeText(context, "Error al guardar foto: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }
}