package com.example.myappnavigation.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.Part

data class ProductoDto(
    val id: Int,
    val nombre: String,
    val precioBase: Int,
    val imagen: String,
    val activo: Boolean
)
data class ProductoRequest(val nombre: String, val precioBase: Int, val
imagen: String)

data class ActivoRequest(val activo: Boolean)

interface CatalogoApi {

    @GET("productos")
    suspend fun listarActivos(): List<ProductoDto>

    @GET("productos/todos")
    suspend fun listarTodos(): List<ProductoDto>

    @GET("productos/{id}")
    suspend fun obtener(@Path("id") id: Int): ProductoDto

    @POST("productos")
    suspend fun crear(@Body request: ProductoRequest): ProductoDto

    @PUT("productos/{id}")
    suspend fun actualizar(@Path("id") id: Int, @Body request: ProductoRequest): ProductoDto

    @PATCH("productos/{id}/activo")
    suspend fun cambiarActivo(@Path("id") id: Int, @Body request: ActivoRequest): ProductoDto

    @Multipart
    @POST("productos/{id}/imagen")
    suspend fun subirImagen(
        @Path("id") id: Int,
        @Part archivo: MultipartBody.Part
    ): ProductoDto
}