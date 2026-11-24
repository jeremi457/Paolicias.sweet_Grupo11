package com.example.myappnavigation.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myappnavigation.data.remote.ProductoDto
import com.example.myappnavigation.data.repository.CatalogoRepository
import com.example.myappnavigation.model.ProductoFormModel
import com.example.myappnavigation.util.ImagenUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductoFormViewModel : ViewModel() {
    private val repository = CatalogoRepository()
    var uiState by mutableStateOf(ProductoFormModel())
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var guardando by mutableStateOf(false)
        private set
    var imagenUri by mutableStateOf<Uri?>(null)
        private set

    private var productoActual: ProductoDto? = null
    private var esNuevo = true

    fun cargarProducto(id: Int) {
        esNuevo = id == 0
        if (id == 0 || productoActual != null) return
        viewModelScope.launch {
            val resultado = repository.obtener(id)
            if (resultado.isSuccess) {
                val producto = resultado.getOrThrow()
                productoActual = producto
                uiState = uiState.copy(
                    nombre = producto.nombre,
                    precio = producto.precioBase.toString(),
                    imagen = producto.imagen
                )
            } else {
                errorMessage = resultado.exceptionOrNull()?.message
            }
        }
    }

    fun onNombreChanged(valor: String) {
        uiState = uiState.copy(nombre = valor, nombreError = validarNombre(valor))
    }

    fun onPrecioChanged(valor: String) {
        val soloDigitos = valor.filter { it.isDigit() }.take(7)
        uiState = uiState.copy(precio = soloDigitos, precioError = validarPrecio(soloDigitos))
    }

    fun onImagenSeleccionada(uri: Uri) {
        imagenUri = uri
        errorMessage = null
    }

    fun validarFormulario(): Boolean {
        val s = uiState
        val nombreError = validarNombre(s.nombre)
        val precioError = validarPrecio(s.precio)
        uiState = s.copy(nombreError = nombreError, precioError = precioError)
        val faltaFoto = esNuevo && imagenUri == null
        errorMessage = if (faltaFoto) "Elige una foto del producto" else null
        return nombreError == null && precioError == null && !faltaFoto
    }

    fun guardarProducto(context: Context, onSuccess: () -> Unit) {
        if (guardando) return
        val s = uiState
        val uri = imagenUri
        val appContext = context.applicationContext
        guardando = true
        errorMessage = null

        viewModelScope.launch {
            var bytes: ByteArray? = null
            if (uri != null) {
                bytes = withContext(Dispatchers.IO) { ImagenUtil.comprimir(appContext, uri, 800) }
                if (bytes == null) {
                    guardando = false
                    errorMessage = "No se pudo leer la imagen. Intenta con otra."
                    return@launch
                }
            }

            val actual = productoActual
            val resultado = if (actual == null) {
                repository.crear(s.nombre.trim(), s.precio.toInt(), "torta")
            } else {
                repository.actualizar(actual.id, s.nombre.trim(), s.precio.toInt(), actual.imagen)
            }
            if (resultado.isFailure) {
                guardando = false
                errorMessage = resultado.exceptionOrNull()?.message
                return@launch
            }

            val producto = resultado.getOrThrow()
            productoActual = producto

            if (bytes != null) {
                val subida = repository.subirImagen(producto.id, bytes)
                if (subida.isFailure) {
                    guardando = false
                    errorMessage = subida.exceptionOrNull()?.message
                    return@launch
                }
            }

            guardando = false
            onSuccess()
        }
    }

    private fun validarNombre(nombre: String): String? {
        if (nombre.isBlank()) return "Ingresa el nombre del producto"
        if (nombre.length > 40) return "Máximo 40 caracteres"
        return null
    }

    private fun validarPrecio(precio: String): String? {
        if (precio.isBlank()) return "Ingresa el precio base"
        if (precio.toInt() <= 0) return "El precio debe ser mayor a 0"
        return null
    }
}