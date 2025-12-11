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
import com.example.minewiki.CamionDeEnvios
import com.example.minewiki.R
import com.example.minewiki.RespuestaAldeano
import com.example.minewiki.UsuarioPaquete
import com.example.minewiki.data.local.AppDatabase
import com.example.minewiki.data.local.UserEntity
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment(R.layout.fragment_register) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etName = view.findViewById<EditText>(R.id.etRegisterName)
        val etPass = view.findViewById<EditText>(R.id.etRegisterPass)
        val etEmail = view.findViewById<EditText>(R.id.etRegisterEmail)
        val btnRegister = view.findViewById<Button>(R.id.btnRegister)
        val tvGoToLogin = view.findViewById<TextView>(R.id.tvGoToLogin)

        btnRegister.setOnClickListener {
            val name = etName.text.toString()
            val pass = etPass.text.toString()
            val email = etEmail.text.toString()

            if (name.isEmpty() || pass.isEmpty()) {
                Toast.makeText(context, "Completa los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnRegister.isEnabled = false
            btnRegister.text = "Guardando..."

            val paquete = UsuarioPaquete(username = name, password = pass)

            CamionDeEnvios.servicio.enviarRegistro(paquete).enqueue(object : Callback<RespuestaAldeano> {

                override fun onResponse(call: Call<RespuestaAldeano>, response: Response<RespuestaAldeano>) {
                    if (response.isSuccessful && response.body()?.status == "success") {
                        guardarEnLocal(name, email, pass)

                    } else {
                        btnRegister.isEnabled = true
                        btnRegister.text = "Registrar"
                        Toast.makeText(context, "Error en XAMPP: ${response.body()?.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RespuestaAldeano>, t: Throwable) {
                    btnRegister.isEnabled = true
                    btnRegister.text = "Registrar"
                    Toast.makeText(context, "Fallo conexión con XAMPP. Revisa tu red.", Toast.LENGTH_LONG).show()
                }
            })
        }

        tvGoToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_register_to_login)
        }
    }

    private fun guardarEnLocal(name: String, email: String, pass: String) {
        lifecycleScope.launch {
            try {
                val newUser = UserEntity(name = name, email = email, password = pass)
                AppDatabase.getDatabase(requireContext()).userDao().insertUser(newUser)

                Toast.makeText(context, "¡Registrado en XAMPP y Celular!", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_register_to_login)
            } catch (e: Exception) {
                Toast.makeText(context, "Error guardando copia local", Toast.LENGTH_SHORT).show()
            }
        }
    }
}