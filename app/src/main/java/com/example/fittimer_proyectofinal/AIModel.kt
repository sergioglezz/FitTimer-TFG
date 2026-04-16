package com.example.fittimer_proyectofinal

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.TextPart
import com.google.ai.client.generativeai.type.asTextOrNull
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.runBlocking

object AIModel {
    private val model = GenerativeModel(
        "gemini-1.5-flash",
        BuildConfig.GEMINI_API_KEY,
        generationConfig = generationConfig {
            temperature = 1f
            topK = 64
            topP = 0.95f
            maxOutputTokens = 8192
            responseMimeType = "text/plain"
        },
        safetySettings = listOf(
            SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.MEDIUM_AND_ABOVE),
            SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.MEDIUM_AND_ABOVE),
            SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.MEDIUM_AND_ABOVE),
            SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.MEDIUM_AND_ABOVE),
        )
    )

    // Añadir un mensaje inicial al historial del chat
    private val chatHistory = mutableListOf<Content>(
        Content(
            parts = listOf(
                TextPart("Actúa como un entrenador personal. Proporcióname consejos sobre ejercicios, rutinas de entrenamiento y nutrición.")
            )
        )
    )

    private val chat = model.startChat(chatHistory)

    fun sendMessage(input: String, callback: (String) -> Unit) {
        runBlocking {
            // Envía el mensaje
            val response = chat.sendMessage(input)

            // Maneja la respuesta
            val result = response.text ?: response.candidates.first().content.parts.first().asTextOrNull().orEmpty()
            callback(result)
        }
    }
}
