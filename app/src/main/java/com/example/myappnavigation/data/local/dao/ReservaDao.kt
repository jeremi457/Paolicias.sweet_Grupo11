package com.example.myappnavigation.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myappnavigation.data.local.entity.ReservaEntity

@Dao
interface ReservaDao {

    // guarda una reserva y devuelve el id generado
    @Insert
    suspend fun insertar(reserva: ReservaEntity): Long

    @Query("SELECT * FROM reservas ORDER BY id DESC")
    suspend fun obtenerTodas(): List<ReservaEntity>

    @Query("SELECT * FROM reservas WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): ReservaEntity?

    @Query("UPDATE reservas SET comprobanteUri = :uri, estado = :estado WHERE id = :id")
    suspend fun actualizarComprobante(id: Int, uri: String, estado: String)
}
