package com.example.myappnavigation.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Estados posibles para el flujo de reserva de eventos.
 */
object EstadoReserva {
    const val PENDIENTE = "Pendiente de pago"
    const val COMPROBANTE_ENVIADO = "Comprobante enviado"
}

/**
 * Entidad Room para el almacenamiento local de cotizaciones y reservas.
 */
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
