package com.example.myappnavigation.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

object EstadoReserva {
    const val PENDIENTE = "Pendiente de pago"
    const val COMPROBANTE_ENVIADO = "Comprobante enviado"
}

@Entity(tableName = "reservas")
data class ReservaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val productoId: Int,
    val productoNombre: String,
    val fecha: String,
    val horario: String,
    val asistentes: Int,
    val telefono: String,
    val observaciones: String,
    val total: Int,
    val abono: Int,
    val estado: String = EstadoReserva.PENDIENTE,
    val comprobanteUri: String? = null
)
