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
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.fragment_login) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etInput = view.findViewById<EditText>(R.id.etLoginEmail)
        val etPass = view.findViewById<EditText>(R.id.etLoginPass)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        val tvGoToRegister = view.findViewById<TextView>(R.id.tvGoToRegister)

        btnLogin.setOnClickListener {
            val inputUsuario = etInput.text.toString()
            val pass = etPass.text.toString()

            if (inputUsuario.isEmpty() || pass.isEmpty()) {
                Toast.makeText(context, "Llena los datos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val user = AppDatabase.getDatabase(requireContext()).userDao().login(inputUsuario, pass)

                if (user != null) {

                    // Guardar sesión
                    val prefs = requireActivity().getSharedPreferences("MineWikiSesion", Context.MODE_PRIVATE)
                    val editor = prefs.edit()
                    editor.putBoolean("esta_logueado", true)
                    editor.putString("current_user_name", user.name)
                    editor.apply()

                    Toast.makeText(context, "¡Hola de nuevo, ${user.name}!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_login_to_home)
                } else {
                    Toast.makeText(context, "Credenciales incorrectas (Local)", Toast.LENGTH_SHORT).show()
                }
            }
        }

        tvGoToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
    }
}