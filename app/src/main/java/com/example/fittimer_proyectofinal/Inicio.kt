package com.example.fittimer_proyectofinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Inicio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        val buttonR = findViewById<Button>(R.id.RegistroBtn)
        buttonR.setOnClickListener {
            val intent = Intent(this, Registro::class.java)
            startActivity(intent)
        }
        val buttonI = findViewById<Button>(R.id.InicioSesionBtn)
        buttonI.setOnClickListener {
            val intent = Intent(this, InicioSesion::class.java)
            startActivity(intent)
        }
    }
    override fun onBackPressed() {
        super.onBackPressedDispatcher

    }
}