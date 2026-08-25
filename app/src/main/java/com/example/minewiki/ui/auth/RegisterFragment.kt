package com.example.minewiki.ui.auth

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.minewiki.R
import com.example.minewiki.data.local.AppDatabase
import com.example.minewiki.data.local.UserEntity
import kotlinx.coroutines.launch

class RegisterFragment : Fragment(R.layout.fragment_register) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etName     = view.findViewById<EditText>(R.id.etRegisterName)
        val etPass     = view.findViewById<EditText>(R.id.etRegisterPass)
        val etEmail    = view.findViewById<EditText>(R.id.etRegisterEmail)
        val btnRegister = view.findViewById<Button>(R.id.btnRegister)
        val tvGoToLogin = view.findViewById<TextView>(R.id.tvGoToLogin)

        btnRegister.setOnClickListener {
            val name  = etName.text.toString().trim()
            val pass  = etPass.text.toString().trim()
            val email = etEmail.text.toString().trim()

            // Validar campos requeridos
            if (name.isEmpty() || pass.isEmpty()) {
                Toast.makeText(context, "Completa los campos obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pass.length < 4) {
                Toast.makeText(context, "La contraseña debe tener al menos 4 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnRegister.isEnabled = false
            btnRegister.text = "Guardando..."

            lifecycleScope.launch {
                try {
                    val db = AppDatabase.getDatabase(requireContext())

                    // Verificar si el nombre de usuario ya existe
                    val existente = db.userDao().findByName(name)
                    if (existente != null) {
                        btnRegister.isEnabled = true
                        btnRegister.text = "Registrar"
                        Toast.makeText(context, "El usuario \"$name\" ya existe. Prueba con otro nombre.", Toast.LENGTH_LONG).show()
                        return@launch
                    }

                    // Insertar el nuevo usuario localmente
                    val nuevoUsuario = UserEntity(name = name, email = email, password = pass)
                    db.userDao().insertUser(nuevoUsuario)

                    // Recuperar el usuario recién creado para obtener su ID
                    val usuarioCreado = db.userDao().findByName(name)

                    // Guardar sesión automáticamente igual que el login
                    val prefs = requireActivity().getSharedPreferences("MineWikiSesion", Context.MODE_PRIVATE)
                    prefs.edit()
                        .putBoolean("esta_logueado", true)
                        .putString("current_user_name", name)
                        .apply()

                    // También guardar el ID en MineWikiData para la pantalla Home
                    val dataPrefs = requireActivity().getSharedPreferences("MineWikiData", 0)
                    dataPrefs.edit()
                        .putInt("current_user_id", usuarioCreado?.id ?: 0)
                        .putString("current_user_name", name)
                        .apply()

                    Toast.makeText(context, "¡Bienvenido, $name! Cuenta creada.", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_register_to_login)

                } catch (e: Exception) {
                    btnRegister.isEnabled = true
                    btnRegister.text = "Registrar"
                    Toast.makeText(context, "Error al guardar: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        tvGoToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_register_to_login)
        }
    }
}