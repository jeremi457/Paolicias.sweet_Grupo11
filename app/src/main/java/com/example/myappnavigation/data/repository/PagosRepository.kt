package com.example.myappnavigation.data.repository

import com.example.myappnavigation.data.remote.ApiConfig
import com.example.myappnavigation.data.remote.PagoDto
import com.example.myappnavigation.data.remote.PagosApi
import com.example.myappnavigation.data.remote.RetrofitFactory
import com.example.myappnavigation.data.remote.ValidacionRequest
import com.example.myappnavigation.data.remote.llamarApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class PagosRepository {
    private val api = RetrofitFactory.crear(ApiConfig.PAGOS_URL,
        PagosApi::class.java)
    suspend fun subirComprobante(reservaId: Int, bytes: ByteArray):
            Result<PagoDto> = llamarApi {
        val cuerpo = bytes.toRequestBody("image/jpeg".toMediaType())
        val parte = MultipartBody.Part.createFormData("archivo",
            "comprobante.jpg", cuerpo)
        api.subirComprobante(reservaId, parte)
    }

    suspend fun obtenerComprobante(reservaId: Int): Result<ByteArray> = llamarApi {
            withContext(Dispatchers.IO) {
                api.obtenerComprobante(reservaId).bytes() }
        }
    suspend fun validar(reservaId: Int, aprobado: Boolean): Result<PagoDto> = llamarApi {
        api.validar(reservaId, ValidacionRequest(aprobado)) }
}