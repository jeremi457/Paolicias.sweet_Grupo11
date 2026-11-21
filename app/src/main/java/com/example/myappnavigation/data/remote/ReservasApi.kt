package com.example.myappnavigation.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
data class ReservaRequest(
    val clienteEmail: String,
    val productoId: Int,
    val fecha: String,
    val horario: String,
    val asistentes: Int,
    val telefono: String,
    val observaciones: String
)
data class ReservaDto(
    val id: Int,
    val clienteEmail: String,
    val productoId: Int,
    val productoNombre: String,
    val fecha: String,
    val horario: String,
    val asistentes: Int,
    val telefono: String,
    val observaciones: String?,
    val total: Int,
    val abono: Int,
    val estado: String
)

interface ReservasApi {
    @POST("reservas")
    suspend fun crear(@Body request: ReservaRequest): ReservaDto
    @GET("reservas")
    suspend fun listarPorEmail(@Query("email") email: String):
            List<ReservaDto>
    @GET("reservas/todas")
    suspend fun listarTodas(): List<ReservaDto>
    @GET("reservas/{id}")
    suspend fun obtener(@Path("id") id: Int): ReservaDto
}