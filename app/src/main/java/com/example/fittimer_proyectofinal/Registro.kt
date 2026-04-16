package com.example.fittimer_proyectofinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import java.security.MessageDigest
import android.view.View


class Registro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val databaseHelper = DatabaseHelper(this)

        val nombreEditText: EditText = findViewById(R.id.nombreEditText)
        val contraseñaEditText: EditText = findViewById(R.id.contrasenaEditText)
        val confirmarContrasenaEditText: EditText = findViewById(R.id.confirmarContrasenaEditText)
        val agregarBtn: Button = findViewById(R.id.RegistroBtn)
        val errorTextView: TextView = findViewById(R.id.errorTextView)
        val atras = findViewById<CardView>(R.id.CardViewAtras)

        atras.setOnClickListener {
            val intent = Intent(this, Inicio::class.java)
            startActivity(intent)
        }

        fun hashContraseña(password: String): String {
            val bytes = password.toByteArray()
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            return digest.fold("", { str, it -> str + "%02x".format(it) })
        }

        agregarBtn.setOnClickListener {
            val nombre = nombreEditText.text.toString()
            val contraseña = contraseñaEditText.text.toString()
            val contraseñaconfirmada = confirmarContrasenaEditText.text.toString()

            // Limpiar el mensaje de error antes de cualquier validación
            errorTextView.visibility = View.GONE

            // Verificar si el nombre de usuario ya existe
            if (databaseHelper.getUsuarioId(nombre) != null) {
                errorTextView.text = "El nombre de usuario ya existe"
                errorTextView.visibility = View.VISIBLE
                return@setOnClickListener
            }


            // Verificar si los campos están llenos
            if (nombre.isNotEmpty() && contraseña.isNotEmpty() && contraseñaconfirmada.isNotEmpty()) {
                // Verificar que la contraseña tenga un mínimo de 8 caracteres
                if (contraseña.length < 8) {
                    errorTextView.text = "La contraseña debe tener al menos 8 caracteres"
                    errorTextView.visibility = View.VISIBLE
                    return@setOnClickListener
                }

                // Verificar si las contraseñas coinciden
                if (contraseña == contraseñaconfirmada) {
                    val contraseñaHasheada = hashContraseña(contraseña)
                    databaseHelper.addUsuario(nombre, contraseñaHasheada)
                    errorTextView.text = "Usuario agregado"
                    errorTextView.visibility = View.VISIBLE
                    val intent = Intent(this, IMC::class.java)
                    intent.putExtra("nombre", nombre)
                    startActivity(intent)
                    finish()
                } else {
                    errorTextView.text = "Las contraseñas no coinciden"
                    errorTextView.visibility = View.VISIBLE
                }
            } else {
                errorTextView.text = "Por favor, rellene los campos"
                errorTextView.visibility = View.VISIBLE
            }
        }
    }

}
