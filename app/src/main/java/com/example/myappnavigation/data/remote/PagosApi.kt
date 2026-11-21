package com.example.myappnavigation.data.remote

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

data class PagoDto(val reservaId: Int, val monto: Int, val estado: String)
data class ValidacionRequest(val aprobado: Boolean)

interface PagosApi {
    @Multipart
    @POST("pagos/{reservaId}/comprobante")
    suspend fun subirComprobante(
        @Path("reservaId") reservaId: Int,
        @Part archivo: MultipartBody.Part): PagoDto

    @GET("pagos/{reservaId}/comprobante")
    suspend fun obtenerComprobante(@Path("reservaId") reservaId: Int): ResponseBody

    @PATCH("pagos/{reservaId}/validacion")
    suspend fun validar(
        @Path("reservaId") reservaId: Int,
        @Body request: ValidacionRequest
    ): PagoDto
}