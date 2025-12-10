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

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Referencias a los IDs del nuevo diseño XML
        val etName = view.findViewById<EditText>(R.id.etRegisterName)
        val etEmail = view.findViewById<EditText>(R.id.etRegisterEmail)
        val etPass = view.findViewById<EditText>(R.id.etRegisterPass)
        val btnRegister = view.findViewById<Button>(R.id.btnRegister)
        val tvGoToLogin = view.findViewById<TextView>(R.id.tvGoToLogin)

        btnRegister.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val pass = etPass.text.toString()

            // Aquí llamamos a la función de registrar
            viewModel.register(name, email, pass)
        }

        viewModel.loginState.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(context, "¡Cuenta Crafteada!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_register_to_home)
            } else {
                Toast.makeText(context, "Faltan datos", Toast.LENGTH_SHORT).show()
            }
        }

        tvGoToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_register_to_login)
        }
    }
}