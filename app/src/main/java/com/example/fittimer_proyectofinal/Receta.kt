package com.example.fittimer_proyectofinal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class Receta : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receta)

        val title = intent.getStringExtra("RECIPE_TITLE")
        val imageResId = intent.getIntExtra("RECIPE_IMAGE", 0)
        val calories = intent.getStringExtra("RECIPE_CALORIES")
        val instructions = intent.getStringExtra("RECIPE_INSTRUCTIONS")

        val imageView: ImageView = findViewById(R.id.imageView9)
        val titleTextView: TextView = findViewById(R.id.textView17)
        val instructionsTextView: TextView = findViewById(R.id.textView18)
        val backButton: Button = findViewById(R.id.button)

        imageView.setImageResource(imageResId)
        titleTextView.text = title
        instructionsTextView.text = "$instructions\n\n$calories"

        backButton.setOnClickListener {
            finish() // Finish the activity to go back to the previous one
        }
    }
}
