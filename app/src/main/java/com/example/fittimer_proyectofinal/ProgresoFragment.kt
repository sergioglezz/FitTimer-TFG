package com.example.fittimer_proyectofinal

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class ProgresoFragment : Fragment() {

    private lateinit var tvResult: TextView
    private lateinit var tvIMC: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvPesoActual: TextView
    private lateinit var tvPesoAnterior: TextView
    private lateinit var tvUnicoPeso: TextView
    private lateinit var tvWeightChange: TextView
    private lateinit var textViewactual: TextView
    private lateinit var textViewanterior: TextView
    private lateinit var textViewUnicoPeso: TextView
    private lateinit var btnRecalculate: Button
    private lateinit var databaseHelper: DatabaseHelper
    private var nombre: String? = null

    companion object {
        const val NOMBRE_KEY = "nombre"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_progreso, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseHelper = DatabaseHelper(requireContext())

        nombre = activity?.intent?.getStringExtra(NOMBRE_KEY)
        if (nombre == null) {
            // Manejar el caso en el que el extra no esté presente
            Toast.makeText(context, "No se encontró el nombre en el Intent", Toast.LENGTH_SHORT).show()
            activity?.finish()
            return
        }

        initComponents(view)
        loadResultFromDatabase()
        initListeners(view)
    }

    private fun initComponents(view: View) {
        tvIMC = view.findViewById(R.id.tvIMC)
        tvResult = view.findViewById(R.id.tvResult)
        tvDescription = view.findViewById(R.id.tvDescription)
        tvPesoActual = view.findViewById(R.id.tvPesoActual)
        tvPesoAnterior = view.findViewById(R.id.tvPesoAnterior)
        tvUnicoPeso = view.findViewById(R.id.tvUnicoPeso)
        tvWeightChange = view.findViewById(R.id.tvWeightChange)
        textViewactual = view.findViewById(R.id.textViewactual)
        textViewanterior = view.findViewById(R.id.textViewanterior)
        textViewUnicoPeso = view.findViewById(R.id.textViewUnicoPeso)
        btnRecalculate = view.findViewById(R.id.btnRecalculate)
    }

    private fun loadResultFromDatabase() {
        val imc: Double? = nombre?.let { databaseHelper.getUltimoIMC(it) }
        if (imc != null) {
            initUI(imc)
        } else {
            // No se encontró ningún IMC guardado
            tvIMC.text = getString(R.string.no_imc)
            tvResult.text = getString(R.string.no_imc_result)
            tvDescription.text = getString(R.string.no_imc_description)
            tvResult.setTextColor(ContextCompat.getColor(requireContext(), R.color.default_text_color))
        }
        showWeightChange()
    }

    private fun showWeightChange() {
        val weights: List<Double>? = nombre?.let { databaseHelper.getPesos(it) }
        if (weights != null && weights.size > 1) {
            val lastWeight = weights[weights.size - 1]
            val previousWeight = weights[weights.size - 2]
            val weightChange = lastWeight - previousWeight
            val changeText = if (weightChange > 0) {
                "Has ganado ${weightChange} kg."
            } else {
                "Has perdido ${-weightChange} kg."
            }
            textViewUnicoPeso.visibility = View.GONE
            textViewactual.visibility = View.VISIBLE
            textViewanterior.visibility = View.VISIBLE
            tvPesoAnterior.text = "${previousWeight}"
            tvPesoActual.text = "${lastWeight}"
        } else if (weights != null && weights.size == 1) {
            tvUnicoPeso.text = "${weights[0]}"
        } else {
            tvWeightChange.text = "No hay datos de peso disponibles."
        }
    }

    private fun initListeners(view: View) {
        btnRecalculate.setOnClickListener {
            val intent = Intent(context, IMC::class.java).apply {
                putExtra(NOMBRE_KEY, nombre)
            }
            startActivity(intent)
        }
    }

    private fun initUI(result: Double) {
        tvIMC.text = result.toString()
        when (result) {
            in 0.00..18.50 -> { // Bajo peso
                tvResult.text = getString(R.string.title_bajo_peso)
                tvResult.setTextColor(ContextCompat.getColor(requireContext(), R.color.peso_bajo))
                tvDescription.text = getString(R.string.description_bajo_peso)
            }
            in 18.51..24.99 -> { // Peso normal
                tvResult.text = getString(R.string.title_peso_normal)
                tvResult.setTextColor(ContextCompat.getColor(requireContext(), R.color.peso_normal))
                tvDescription.text = getString(R.string.description_peso_normal)
            }
            in 25.00..29.99 -> { // Sobrepeso
                tvResult.text = getString(R.string.title_sobrepeso)
                tvResult.setTextColor(ContextCompat.getColor(requireContext(), R.color.peso_sobrepeso))
                tvDescription.text = getString(R.string.description_sobrepeso)
            }
            in 30.00..99.00 -> { // Obesidad
                tvResult.text = getString(R.string.title_obesidad)
                tvResult.setTextColor(ContextCompat.getColor(requireContext(), R.color.obesidad))
                tvDescription.text = getString(R.string.description_obesidad)
            }
            else -> { // Error
                tvIMC.text = getString(R.string.error)
                tvResult.text = getString(R.string.error)
                tvResult.setTextColor(ContextCompat.getColor(requireContext(), R.color.obesidad))
                tvDescription.text = getString(R.string.error)
            }
        }
    }
}
