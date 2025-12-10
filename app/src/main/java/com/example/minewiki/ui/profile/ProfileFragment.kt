package com.example.minewiki.ui.profile

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.minewiki.R

class ProfileFragment : Fragment() {

    private lateinit var imgProfile: ImageView
    private lateinit var btnCamera: Button
    private lateinit var btnGallery: Button

    private var imageUri: Uri? = null

    // Launcher para abrir la cámara
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                imgProfile.setImageURI(imageUri)
            }
        }

    // Launcher para seleccionar imagen de la galería
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                imgProfile.setImageURI(uri)
            }
        }

    // Permisos para cámara
    private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) openCamera()
        }

    // Permisos para galería
    private val requestGalleryPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) openGallery()
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        imgProfile = view.findViewById(R.id.imgProfileLarge)
        btnCamera = view.findViewById(R.id.btnCamera)
        btnGallery = view.findViewById(R.id.btnGallery)

        btnCamera.setOnClickListener { checkCameraPermission() }
        btnGallery.setOnClickListener { checkGalleryPermission() }

        return view
    }

    /** ================================
     *     PERMISOS
     *  ================================= */
    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> openCamera()

            else -> requestCameraPermission.launch(Manifest.permission.CAMERA)
        }
    }

    private fun checkGalleryPermission() {
        val permission =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU)
                Manifest.permission.READ_MEDIA_IMAGES
            else
                Manifest.permission.READ_EXTERNAL_STORAGE

        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED -> openGallery()

            else -> requestGalleryPermission.launch(permission)
        }
    }

    /** ================================
     *     ACCIONES
     *  ================================= */
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val uri = requireContext().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            ContentValues()
        )

        imageUri = uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)

        cameraLauncher.launch(intent)
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }
}
