package com.example.minewiki.ui.auth

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

        val etName = view.findViewById<EditText>(R.id.etRegisterName)
        val etEmail = view.findViewById<EditText>(R.id.etRegisterEmail)
        val etPass = view.findViewById<EditText>(R.id.etRegisterPass)
        val btnRegister = view.findViewById<Button>(R.id.btnRegister)
        val tvGoToLogin = view.findViewById<TextView>(R.id.tvGoToLogin)

        btnRegister.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val pass = etPass.text.toString()

            if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // GUARDAR EN BASE DE DATOS
            lifecycleScope.launch {
                val newUser = UserEntity(name = name, email = email, password = pass)
                AppDatabase.getDatabase(requireContext()).userDao().insertUser(newUser)

                Toast.makeText(context, "¡Cuenta Crafteada! Ahora inicia sesión.", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_register_to_login)
            }
        }

        tvGoToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_register_to_login)
        }
    }
}