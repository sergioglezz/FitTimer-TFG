package com.example.fittimer_proyectofinal

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class AlimentacionFragment : Fragment() {

    private lateinit var btnDefinicion: Button
    private lateinit var btnVolumen: Button
    private lateinit var cardView1: CardView
    private lateinit var cardView2: CardView
    private lateinit var cardView3: CardView
    private lateinit var cardView4: CardView
    private lateinit var cardView5: CardView
    private lateinit var cardView6: CardView
    private lateinit var cardView7: CardView
    private lateinit var cardView8: CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_alimentacion, container, false)

        btnDefinicion = view.findViewById(R.id.btnDefinicion)
        btnVolumen = view.findViewById(R.id.btnVolumen)
        cardView1 = view.findViewById(R.id.cardView1)
        cardView2 = view.findViewById(R.id.cardView2)
        cardView3 = view.findViewById(R.id.cardView3)
        cardView4 = view.findViewById(R.id.cardView4)
        cardView5 = view.findViewById(R.id.cardView5)
        cardView6 = view.findViewById(R.id.cardView6)
        cardView7 = view.findViewById(R.id.cardView7)
        cardView8 = view.findViewById(R.id.cardView8)

        btnDefinicion.setOnClickListener {
            mostrarDefinicion()
        }

        btnVolumen.setOnClickListener {
            mostrarVolumen()
        }

        // Set onClickListeners for cardViews
        setCardViewClickListener(cardView1, "Ensalada de Pollo a la Parrilla", R.drawable.grilledchickensalad, "390 kcal", getString(R.string.recipe_ensalada_pollo))
        setCardViewClickListener(cardView2, "Tazón de Quinoa y Vegetales", R.drawable.quinoaconverdurasemplatada, "433 kcal", getString(R.string.recipe_quinoa_vegetales))
        setCardViewClickListener(cardView3, "Smoothie Verde Energético", R.drawable.batidoverde, "252 kcal", getString(R.string.recipe_smoothie_verde))
        setCardViewClickListener(cardView4, "Salmón al Horno con Espárragos", R.drawable.salmonalhorno, "445 kcal", getString(R.string.recipe_salmon_horno))
        setCardViewClickListener(cardView5, "Smoothie de Avena y Frutas", R.drawable.c4cc16012490b66d5a8655338e3f5023, "1070 kcal", getString(R.string.recipe_smoothie_avena))
        setCardViewClickListener(cardView6, "Pasta con Pollo y Salsa Alfredo", R.drawable.pasta, "1824 kcal", getString(R.string.recipe_pasta_pollo))
        setCardViewClickListener(cardView7, "Hamburguesa con Guacamole y Papas", R.drawable.hamburgesa, "1589 kcal", getString(R.string.recipe_hamburguesa_guacamole))
        setCardViewClickListener(cardView8, "Lasagna de Carne y Queso", R.drawable.lasana, "3690 kcal", getString(R.string.recipe_lasagna))

        mostrarDefinicion() // Mostrar el primer conjunto de tarjetas al inicio

        return view
    }

    private fun setCardViewClickListener(cardView: CardView, title: String, imageResId: Int, calories: String, instructions: String) {
        cardView.setOnClickListener {
            val intent = Intent(activity, Receta::class.java).apply {
                putExtra("RECIPE_TITLE", title)
                putExtra("RECIPE_IMAGE", imageResId)
                putExtra("RECIPE_CALORIES", calories)
                putExtra("RECIPE_INSTRUCTIONS", instructions)
            }
            startActivity(intent)
        }
    }

    private fun mostrarDefinicion() {
        cardView1.visibility = View.VISIBLE
        cardView2.visibility = View.VISIBLE
        cardView3.visibility = View.VISIBLE
        cardView4.visibility = View.VISIBLE

        cardView5.visibility = View.GONE
        cardView6.visibility = View.GONE
        cardView7.visibility = View.GONE
        cardView8.visibility = View.GONE
    }

    private fun mostrarVolumen() {
        cardView1.visibility = View.GONE
        cardView2.visibility = View.GONE
        cardView3.visibility = View.GONE
        cardView4.visibility = View.GONE

        cardView5.visibility = View.VISIBLE
        cardView6.visibility = View.VISIBLE
        cardView7.visibility = View.VISIBLE
        cardView8.visibility = View.VISIBLE
    }
}
