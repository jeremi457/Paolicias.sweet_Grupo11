package com.example.myappnavigation.model

object EstadoReserva {
    const val PENDIENTE_PAGO = "PENDIENTE_PAGO"
    const val COMPROBANTE_ENVIADO = "COMPROBANTE_ENVIADO"
    const val CONFIRMADA = "CONFIRMADA"
    const val RECHAZADA = "RECHAZADA"

    fun texto(estado: String): String = when (estado) {
        PENDIENTE_PAGO -> "Pendiente de pago"
        COMPROBANTE_ENVIADO -> "Comprobante enviado"
        CONFIRMADA -> "Confirmada"
        RECHAZADA -> "Rechazada"
        else -> estado
    }
}