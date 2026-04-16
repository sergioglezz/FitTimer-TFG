package com.example.fittimer_proyectofinal

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Canvas
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import java.util.*
import android.graphics.Typeface


class HomeFragment : Fragment() {

    private var nombre: String? = null
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseHelper = DatabaseHelper(requireContext())

        nombre = activity?.intent?.getStringExtra(ProgresoFragment.NOMBRE_KEY)
        if (nombre == null) {
            Log.e("HomeFragment", "No se encontró el nombre en el Intent")
            activity?.finish()
            return
        }

        setupDragAndDrop(view)
        loadIconsFromDatabase(view)
        highlightToday(view)

        val buttonLimpiarDias = view.findViewById<Button>(R.id.buttonLimpiarDias)
        buttonLimpiarDias.setOnClickListener {
            limpiarDias(view)
        }
    }

    private fun setupDragAndDrop(view: View) {
        val imageViewPecho = view.findViewById<ImageView>(R.id.imageViewPecho).apply {
            tag = "pecho_icon"
        }
        val imageViewEspalda = view.findViewById<ImageView>(R.id.imageViewEspalda).apply {
            tag = "espalda_icon"
        }
        val imageViewPierna = view.findViewById<ImageView>(R.id.imageViewPierna).apply {
            tag = "pierna_icon"
        }
        val imageViewBrazo = view.findViewById<ImageView>(R.id.imageViewBrazo).apply {
            tag = "brazo_icon"
        }
        val imageViewPush = view.findViewById<ImageView>(R.id.imageViewPush).apply {
            tag = "push_icon"
        }
        val imageViewPull = view.findViewById<ImageView>(R.id.imageViewPull).apply {
            tag = "pull_icon"
        }

        setDragListener(imageViewPecho)
        setDragListener(imageViewEspalda)
        setDragListener(imageViewPierna)
        setDragListener(imageViewBrazo)
        setDragListener(imageViewPush)
        setDragListener(imageViewPull)

        val cardViewLunes = view.findViewById<CardView>(R.id.cardViewLunes)
        val cardViewMartes = view.findViewById<CardView>(R.id.cardViewMartes)
        val cardViewMiercoles = view.findViewById<CardView>(R.id.cardViewMiercoles)
        val cardViewJueves = view.findViewById<CardView>(R.id.cardViewJueves)
        val cardViewViernes = view.findViewById<CardView>(R.id.cardViewViernes)
        val cardViewSabado = view.findViewById<CardView>(R.id.cardViewSabado)
        val cardViewDomingo = view.findViewById<CardView>(R.id.cardViewDomingo)
        setDropListener(cardViewLunes, "lunes")
        setDropListener(cardViewMartes, "martes")
        setDropListener(cardViewMiercoles, "miercoles")
        setDropListener(cardViewJueves, "jueves")
        setDropListener(cardViewViernes, "viernes")
        setDropListener(cardViewSabado, "sabado")
        setDropListener(cardViewDomingo, "domingo")
    }

    private fun setDragListener(view: View) {
        view.setOnLongClickListener {
            Log.d("DragDrop", "Starting drag for view with tag: ${it.tag}")
            val item = ClipData.Item(it.tag as CharSequence)
            val dragData = ClipData(
                it.tag as CharSequence,
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                item
            )
            val myShadow = MyDragShadowBuilder(it)
            it.startDragAndDrop(dragData, myShadow, it, 0)
            true
        }
    }

    private fun setDropListener(view: CardView, dia: String) {
        view.setOnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    Log.d("DragDrop", "Drag started")
                    event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    Log.d("DragDrop", "Drag entered")
                    v.invalidate()
                    true
                }
                DragEvent.ACTION_DRAG_LOCATION -> true
                DragEvent.ACTION_DRAG_EXITED -> {
                    Log.d("DragDrop", "Drag exited")
                    v.invalidate()
                    true
                }
                DragEvent.ACTION_DROP -> {
                    Log.d("DragDrop", "Drag dropped")
                    val item = event.clipData.getItemAt(0)
                    val dragData = item.text.toString()

                    Log.d("DragDrop", "Dropped data: $dragData")

                    val container = v as CardView

                    if (container.childCount == 0) {
                        val imageView = ImageView(requireContext())
                        val resourceId = resources.getIdentifier(dragData, "drawable", requireContext().packageName)
                        if (resourceId != 0) {
                            imageView.setImageResource(resourceId)
                            imageView.layoutParams = LayoutParams(140, 140) // Tamaño ajustado
                            imageView.tag = dragData
                            container.addView(imageView)
                            Log.d("DragDrop", "Image added to container with tag: $dragData")
                            nombre?.let {
                                databaseHelper.addIcono(it, dia, dragData)
                            }
                        } else {
                            Log.d("DragDrop", "Resource not found for tag: $dragData")
                        }
                    }

                    v.invalidate()
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    Log.d("DragDrop", "Drag ended")
                    v.invalidate()
                    true
                }
                else -> {
                    Log.d("DragDrop", "Unknown action type received by OnDragListener.")
                    false
                }
            }
        }
    }


    private fun limpiarDias(view: View) {
        val dias = listOf(
            R.id.cardViewLunes, R.id.cardViewMartes, R.id.cardViewMiercoles,
            R.id.cardViewJueves, R.id.cardViewViernes, R.id.cardViewSabado, R.id.cardViewDomingo
        )
        for (dia in dias) {
            val cardView = view.findViewById<CardView>(dia)
            cardView.removeAllViews()
        }
        nombre?.let {
            databaseHelper.clearIconos(it)
        }
    }

    private fun loadIconsFromDatabase(view: View) {
        nombre?.let {
            val iconos = databaseHelper.getIconos(it)
            iconos?.forEach { (dia, icono) ->
                val cardViewId = resources.getIdentifier("cardView${dia.capitalize()}", "id", requireContext().packageName)
                val cardView = view.findViewById<CardView>(cardViewId)
                val imageView = ImageView(requireContext())
                val resourceId = resources.getIdentifier(icono, "drawable", requireContext().packageName)
                if (resourceId != 0) {
                    imageView.setImageResource(resourceId)
                    imageView.layoutParams = LayoutParams(140, 140) // Tamaño ajustado
                    imageView.tag = icono
                    cardView.addView(imageView)
                }
            }
        }
    }

    private fun highlightToday(view: View) {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        val dayTextViewId = when (dayOfWeek) {
            Calendar.MONDAY -> R.id.textViewL
            Calendar.TUESDAY -> R.id.textViewM
            Calendar.WEDNESDAY -> R.id.textViewX
            Calendar.THURSDAY -> R.id.textViewJ
            Calendar.FRIDAY -> R.id.textViewV
            Calendar.SATURDAY -> R.id.textViewS
            Calendar.SUNDAY -> R.id.textViewD
            else -> null
        }

        dayTextViewId?.let {
            val dayTextView = view.findViewById<TextView>(it)
            dayTextView.setTextColor(resources.getColor(R.color.colorHighlight, null))
            dayTextView.setTypeface(null, Typeface.BOLD)
        }
    }

    private class MyDragShadowBuilder(view: View) : View.DragShadowBuilder(view) {
        private val shadow = view

        override fun onProvideShadowMetrics(size: Point, touch: Point) {
            val width: Int = view.width
            val height: Int = view.height

            size.set(width, height)
            touch.set(width / 2, height / 2)
        }

        override fun onDrawShadow(canvas: Canvas) {
            shadow.draw(canvas)
        }
    }
}
