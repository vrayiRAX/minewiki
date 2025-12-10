package com.example.minewiki.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.minewiki.R
import com.example.minewiki.viewmodel.AuthViewModel

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etEmail = view.findViewById<EditText>(R.id.etLoginEmail)
        val etPass = view.findViewById<EditText>(R.id.etLoginPass)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        val tvGoToRegister = view.findViewById<TextView>(R.id.tvGoToRegister)

        // 2. Configurar botón de Login
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val pass = etPass.text.toString()
            viewModel.login(email, pass)
        }

        // 3. Observar resultado del Login
        viewModel.loginState.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(context, "¡Login Correcto!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_login_to_home)
            } else {
                Toast.makeText(context, "Error: Datos incorrectos", Toast.LENGTH_SHORT).show()
            }
        }

        // 4. Configurar botón de ir al Registro
        tvGoToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
    }
}