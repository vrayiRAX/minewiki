package com.example.minewiki.ui.chat

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minewiki.AiChatRequest
import com.example.minewiki.AiChatResponse
import com.example.minewiki.CamionDeEnvios
import com.example.minewiki.ChatHistoryMessage
import com.example.minewiki.R
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatFragment : Fragment(R.layout.fragment_chat) {

    private lateinit var adapter: ChatAdapter
    private val messagesList = mutableListOf<ChatMessage>()
    private lateinit var recyclerChat: RecyclerView
    private lateinit var etMessage: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        val btnSend = view.findViewById<Button>(R.id.btnSendChat)
        etMessage = view.findViewById(R.id.etChatMessage)
        recyclerChat = view.findViewById(R.id.recyclerChat)

        val chipWarden = view.findViewById<TextView>(R.id.chipWarden)
        val chipBeacon = view.findViewById<TextView>(R.id.chipBeacon)
        val chipNether = view.findViewById<TextView>(R.id.chipNether)

        adapter = ChatAdapter(messagesList)
        recyclerChat.layoutManager = LinearLayoutManager(requireContext()).apply {
            stackFromEnd = true
        }
        recyclerChat.adapter = adapter

        if (messagesList.isEmpty()) {
            adapter.addMessage(
                ChatMessage(
                    message = "Hola, Jugador! Soy tu Asistente Experto de IA para Minecraft.\n\nPuedes preguntarme sobre crafteos, biomas, mobs, mecanicas de redstone o portales.\n\nRecuerdo el contexto de nuestra conversacion, puedes hacer preguntas de seguimiento!",
                    isUser = false,
                    modelUsed = "Groq LLM"
                )
            )
        }

        btnBack.setOnClickListener { findNavController().popBackStack() }
        btnSend.setOnClickListener { enviarMensajeUsuario() }

        chipWarden.setOnClickListener {
            etMessage.setText("Como vencer al Warden y que cuidados debo tener?")
            enviarMensajeUsuario()
        }
        chipBeacon.setOnClickListener {
            etMessage.setText("Como activar un Faro (Beacon) al nivel maximo?")
            enviarMensajeUsuario()
        }
        chipNether.setOnClickListener {
            etMessage.setText("Como funciona la equivalencia de bloques entre el Nether y el Overworld?")
            enviarMensajeUsuario()
        }
    }

    /** Construye historial de conversacion para control de contexto multi-turno. */
    private fun buildHistory(): List<ChatHistoryMessage> {
        return messagesList
            .filter { !it.message.startsWith("\u23f3") && !it.message.startsWith("\u26a0\ufe0f") }
            .takeLast(6)
            .map { msg ->
                ChatHistoryMessage(
                    role = if (msg.isUser) "user" else "assistant",
                    content = msg.message
                )
            }
    }

    private fun enviarMensajeUsuario() {
        val texto = etMessage.text.toString().trim()
        if (texto.isEmpty()) return

        val historialActual = buildHistory()

        adapter.addMessage(ChatMessage(message = texto, isUser = true))
        etMessage.setText("")

        val loadingMsg = ChatMessage(message = "\u23f3 Consultando a la IA de Groq...", isUser = false, modelUsed = "Pensando...")
        adapter.addMessage(loadingMsg)
        val loadingPosition = adapter.itemCount - 1
        recyclerChat.smoothScrollToPosition(loadingPosition)

        lifecycleScope.launch {
            try {
                val req = AiChatRequest(
                    message = texto,
                    use_fast_model = true,
                    history = historialActual.ifEmpty { null }
                )
                CamionDeEnvios.aiServicio.enviarMensajeChat(req).enqueue(object : Callback<AiChatResponse> {
                    override fun onResponse(call: Call<AiChatResponse>, response: Response<AiChatResponse>) {
                        if (isAdded) {
                            val reply = response.body()?.reply ?: "No se recibio respuesta del Asistente de IA."
                            val model = response.body()?.model_used ?: "Groq IA"
                            if (loadingPosition < messagesList.size) {
                                messagesList[loadingPosition] = ChatMessage(message = reply, isUser = false, modelUsed = model)
                                adapter.notifyItemChanged(loadingPosition)
                                recyclerChat.smoothScrollToPosition(loadingPosition)
                            }
                        }
                    }

                    override fun onFailure(call: Call<AiChatResponse>, t: Throwable) {
                        if (isAdded && loadingPosition < messagesList.size) {
                            val errorMsg = "Error de conexion con el servidor IA: ${t.localizedMessage ?: "Tiempo de espera agotado"}.\n\nVerifica tu red y que el servidor este en ejecucion."
                            messagesList[loadingPosition] = ChatMessage(message = errorMsg, isUser = false, modelUsed = "Error de Red")
                            adapter.notifyItemChanged(loadingPosition)
                            recyclerChat.smoothScrollToPosition(loadingPosition)
                        }
                    }
                })
            } catch (e: Exception) {
                Toast.makeText(context, "Error al enviar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
