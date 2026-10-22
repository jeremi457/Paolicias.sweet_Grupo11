package com.example.myappnavigation.model

/**
 * Modelo de estado inmutable para el formulario de reserva y cotizaciones.
 */
data class ReservaModel(
    val productoId: Int = 0,
    val productoNombre: String = "",
    val precioBase: Int = 0,
    val fecha: String = "",
    val horario: String = "",
    val asistentes: Int = 50,
    val telefono: String = "",
    val observaciones: String = "",
    val fechaError: String? = null,
    val horarioError: String? = null,
    val telefonoError: String? = null,
    val observacionesError: String? = null
) {
    val total: Int get() = Tarifario.calcularTotal(precioBase, asistentes)
    val abono: Int get() = Tarifario.calcularAbono(total)
}
