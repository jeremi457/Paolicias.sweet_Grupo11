package com.example.myappnavigation.model

/**
 * Logica de tarificacion y presupuestos de eventos.
 * Calcula el valor total en base a los asistentes y el abono requerido del 50%.
 */
object Tarifario {
    const val PASO = 10
    const val MAX_ASISTENTES = 100

    /**
     * Calcula el monto total estimado para el evento.
     */
    fun calcularTotal(precioBase: Int, asistentes: Int): Int {
        return precioBase * (asistentes / PASO)
    }

    /**
     * Calcula el abono del 50% requerido para confirmar y bloquear la fecha.
     */
    fun calcularAbono(total: Int): Int = total / 2
}
