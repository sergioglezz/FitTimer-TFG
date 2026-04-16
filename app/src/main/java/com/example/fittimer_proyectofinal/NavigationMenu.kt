package com.example.fittimer_proyectofinal

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment

class NavigationMenu : AppCompatActivity() {

    private lateinit var buttonHome: ImageButton
    private lateinit var buttonEntrenamiento: ImageButton
    private lateinit var buttonAlimentacion: ImageButton
    private lateinit var buttonProgreso: ImageButton
    private lateinit var buttonCerrarSesion: Button
    private lateinit var cardViewUsuario: CardView
    private var isCardViewVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_menu)

        buttonHome = findViewById(R.id.imageButtonHome)
        buttonEntrenamiento = findViewById(R.id.imageButtonEntrenamiento)
        buttonAlimentacion = findViewById(R.id.imageButtonAlimentacion)
        buttonProgreso = findViewById(R.id.imageButtonProgreso)
        buttonCerrarSesion = findViewById(R.id.buttonCerrarSesion)
        cardViewUsuario = findViewById(R.id.cardViewUsuario)

        setupNavigation()

        // Set default selection
        if (savedInstanceState == null) {
            handleIntent(intent)
        }

        val slideInAnimation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, -1f,
            Animation.RELATIVE_TO_SELF, 0f
        ).apply {
            duration = 500
        }

        val slideOutAnimation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, -1f
        ).apply {
            duration = 500
        }

        slideOutAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                val params = cardViewUsuario.layoutParams as ConstraintLayout.LayoutParams
                params.topMargin = -cardViewUsuario.height
                cardViewUsuario.layoutParams = params
                isCardViewVisible = false
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        val buttonUsuario = findViewById<ImageButton>(R.id.imageButtonUsuario)
        buttonUsuario.setOnClickListener {
            if (!isCardViewVisible) {
                cardViewUsuario.startAnimation(slideInAnimation)
                val params = cardViewUsuario.layoutParams as ConstraintLayout.LayoutParams
                params.topMargin = 0
                cardViewUsuario.layoutParams = params
            } else {
                cardViewUsuario.startAnimation(slideOutAnimation)
            }
            isCardViewVisible = !isCardViewVisible
        }

        buttonCerrarSesion.setOnClickListener {
            val intent = Intent(this, Inicio::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun handleIntent(intent: Intent?) {
        intent?.let {
            val targetFragment = it.getStringExtra(IMC.TARGET_FRAGMENT_KEY)
            if (targetFragment == IMC.PROGRESO_FRAGMENT) {
                openFragment(ProgresoFragment())
                updateIcons(R.id.imageButtonProgreso)
            } else {
                openFragment(HomeFragment())
                updateIcons(R.id.imageButtonHome)
            }
        }
    }

    private fun setupNavigation() {
        buttonHome.setOnClickListener {
            openFragment(HomeFragment())
            updateIcons(R.id.imageButtonHome)
        }
        buttonEntrenamiento.setOnClickListener {
            openFragment(EntrenamientoFragment())
            updateIcons(R.id.imageButtonEntrenamiento)
        }
        buttonAlimentacion.setOnClickListener {
            openFragment(AlimentacionFragment())
            updateIcons(R.id.imageButtonAlimentacion)
        }
        buttonProgreso.setOnClickListener {
            openFragment(ProgresoFragment())
            updateIcons(R.id.imageButtonProgreso)
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }

    private fun updateIcons(selectedButtonId: Int) {
        // Reset all icons to default
        buttonHome.setImageResource(R.drawable.home)
        buttonEntrenamiento.setImageResource(R.drawable.entrenamiento)
        buttonAlimentacion.setImageResource(R.drawable.alimentacion)
        buttonProgreso.setImageResource(R.drawable.progreso)

        // Set the selected icon
        when (selectedButtonId) {
            R.id.imageButtonHome -> buttonHome.setImageResource(R.drawable.homeselec)
            R.id.imageButtonEntrenamiento -> buttonEntrenamiento.setImageResource(R.drawable.entrenamientoselec)
            R.id.imageButtonAlimentacion -> buttonAlimentacion.setImageResource(R.drawable.alimentacionselec)
            R.id.imageButtonProgreso -> buttonProgreso.setImageResource(R.drawable.progresoselec)
        }
    }
    override fun onBackPressed() {
        super.onBackPressedDispatcher

    }
}
