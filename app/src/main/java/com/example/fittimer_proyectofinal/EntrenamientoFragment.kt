package com.example.fittimer_proyectofinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EntrenamientoFragment : Fragment() {

    private lateinit var linearLayoutConversation: LinearLayout
    private lateinit var editTextQuestion: EditText
    private lateinit var buttonAsk: Button
    private val conversation = StringBuilder()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_entrenamiento, container, false)

        // Obtener referencias de los elementos de la UI
        linearLayoutConversation = view.findViewById(R.id.linearLayoutConversation)
        editTextQuestion = view.findViewById(R.id.editTextQuestion)
        buttonAsk = view.findViewById(R.id.buttonAsk)

        // Mostrar el mensaje de presentación de la IA
        addMessageToConversation("IA: ¡Hola! Soy tu entrenador personal virtual. Estoy aquí para ayudarte con tus rutinas de entrenamiento y darte consejos sobre ejercicios y nutrición. ¿En qué puedo ayudarte hoy?", false)

        // Configurar el botón para enviar el mensaje a la IA
        buttonAsk.setOnClickListener {
            val question = editTextQuestion.text.toString()
            if (question.isNotEmpty()) {
                // Mostrar el mensaje del usuario en el TextView
                addMessageToConversation("Tú: $question", true)

                // Limpiar el EditText
                editTextQuestion.text.clear()

                // Enviar el mensaje a la IA
                sendMessageToAI(question)
            }
        }

        return view
    }

    private fun sendMessageToAI(message: String) {
        // Deshabilitar el botón mientras se procesa la solicitud
        buttonAsk.isEnabled = false

        CoroutineScope(Dispatchers.IO).launch {
            AIModel.sendMessage(message) { response ->
                // Manejar la respuesta en el hilo principal
                CoroutineScope(Dispatchers.Main).launch {
                    // Actualizar el TextView con la respuesta de la IA
                    addMessageToConversation("IA: $response", false)
                    // Volver a habilitar el botón
                    buttonAsk.isEnabled = true
                }
            }
        }
    }

    private fun addMessageToConversation(message: String, isUser: Boolean) {
        val textView = TextView(context).apply {
            text = message
            textSize = 16f
            setPadding(16)
            if (isUser) {
                setBackgroundResource(R.drawable.user_message_background)
                setTextColor(ContextCompat.getColor(context, android.R.color.white))
                textAlignment = View.TEXT_ALIGNMENT_VIEW_END
            } else {
                setBackgroundResource(R.drawable.ia_message_background)
                setTextColor(ContextCompat.getColor(context, android.R.color.black))
                textAlignment = View.TEXT_ALIGNMENT_VIEW_START
            }
        }
        linearLayoutConversation.addView(textView)
    }
}
