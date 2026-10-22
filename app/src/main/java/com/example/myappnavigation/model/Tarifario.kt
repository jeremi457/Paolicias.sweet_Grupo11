package com.example.myappnavigation.model

object Tarifario {
    const val PASO = 10
    const val MAX_ASISTENTES = 100

    fun calcularTotal(precioBase: Int, asistentes: Int): Int {
        return precioBase * (asistentes / PASO)
    }

    fun calcularAbono(total: Int): Int = total / 2
}