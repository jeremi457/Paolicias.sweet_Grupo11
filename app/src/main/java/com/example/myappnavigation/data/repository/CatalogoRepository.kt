package com.example.myappnavigation.data.repository

import com.example.myappnavigation.data.remote.ActivoRequest
import com.example.myappnavigation.data.remote.ApiConfig
import com.example.myappnavigation.data.remote.CatalogoApi
import com.example.myappnavigation.data.remote.ProductoDto
import com.example.myappnavigation.data.remote.ProductoRequest
import com.example.myappnavigation.data.remote.RetrofitFactory
import com.example.myappnavigation.data.remote.llamarApi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class CatalogoRepository {

    private val api = RetrofitFactory.crear(ApiConfig.CATALOGO_URL,
        CatalogoApi::class.java)

    suspend fun listarActivos(): Result<List<ProductoDto>> = llamarApi { api.listarActivos() }

    suspend fun listarTodos(): Result<List<ProductoDto>> = llamarApi { api.listarTodos() }

    suspend fun obtener(id: Int): Result<ProductoDto> = llamarApi { api.obtener(id) }

    suspend fun crear(nombre: String, precioBase: Int, imagen: String):
            Result<ProductoDto> =
        llamarApi { api.crear(ProductoRequest(nombre, precioBase, imagen)) }

    suspend fun actualizar(id: Int, nombre: String, precioBase: Int, imagen: String):
            Result<ProductoDto> = llamarApi { api.actualizar(id, ProductoRequest(nombre, precioBase,
        imagen)) }

    suspend fun cambiarActivo(id: Int, activo: Boolean):
            Result<ProductoDto> = llamarApi {
                api.cambiarActivo(id, ActivoRequest(activo)) }

    suspend fun subirImagen(id: Int, bytes: ByteArray): Result<ProductoDto> = llamarApi {
        val cuerpo = bytes.toRequestBody("image/jpeg".toMediaType())
        val parte = MultipartBody.Part.createFormData("archivo", "producto.jpg", cuerpo)
        api.subirImagen(id, parte)
    }
}