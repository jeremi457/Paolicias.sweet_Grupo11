package com.example.myappnavigation.data.remote

import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException

data class ErrorCuerpo(val message: String?)

object ErroresRed {
    fun mensaje(e: Throwable): String {
        return when (e) {
            is HttpException -> {
                val cuerpo = e.response()?.errorBody()?.string()
                val detalle = try {
                    Gson().fromJson(cuerpo, ErrorCuerpo::class.java)?.message
                } catch (ex: Exception) {
                    null
                }
                detalle ?: "Error del servidor (${e.code()})."
            }
            is IOException -> "No hay conexión con el servidor. Reintenta en unos segundos."
            else -> "Ocurrió un error inesperado."
        }
    }
}

// Ejecuta una llamada a la API y devuelve un Result con el valor o con el mensaje de error
suspend fun <T> llamarApi(bloque: suspend () -> T): Result<T> {
    return try {
        Result.success(bloque())
    } catch (e: HttpException) {
        Result.failure(Exception(ErroresRed.mensaje(e)))
    } catch (e: IOException) {
        Result.failure(Exception(ErroresRed.mensaje(e)))
    }
}
