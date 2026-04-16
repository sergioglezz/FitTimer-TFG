package com.example.fittimer_proyectofinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import java.security.MessageDigest

class InicioSesion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_sesion)

        val databaseHelper = DatabaseHelper(this)

        val nombreEditText: EditText = findViewById(R.id.nombreEditText)
        val contraseñaEditText: EditText = findViewById(R.id.contrasenaEditText)
        val agregarBtn: Button = findViewById(R.id.RegistroBtn)
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
            if (nombre.isNotEmpty() && contraseña.isNotEmpty()) {
                val contraseñaHasheada = hashContraseña(contraseña)
                val contraseñaHasheadaDb = databaseHelper.getContrasenaHasheadaPorNombre(nombre)
                if (contraseñaHasheada == contraseñaHasheadaDb) {
                    Toast.makeText(this, "Sesion iniciada", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, NavigationMenu::class.java).apply {
                        putExtra("nombre", nombre)
                    }
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Contraseñas Incorrecta", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, rellene los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
