package com.example.myappnavigation.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myappnavigation.data.remote.ProductoDto
import com.example.myappnavigation.data.repository.CatalogoRepository
import com.example.myappnavigation.model.ProductoFormModel
import kotlinx.coroutines.launch

class ProductoFormViewModel : ViewModel() {
    private val repository = CatalogoRepository()
    var uiState by mutableStateOf(ProductoFormModel())
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var guardando by mutableStateOf(false)
        private set

    private var productoActual: ProductoDto? = null
    fun cargarProducto(id: Int) {
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
        uiState = uiState.copy(nombre = valor, nombreError =
            validarNombre(valor))
    }
    fun onPrecioChanged(valor: String) {
        val soloDigitos = valor.filter { it.isDigit() }.take(7)
        uiState = uiState.copy(precio = soloDigitos, precioError =
            validarPrecio(soloDigitos))
    }
    fun onImagenChanged(clave: String) {
        uiState = uiState.copy(imagen = clave)
    }
    fun validarFormulario(): Boolean {
        val s = uiState
        val nombreError = validarNombre(s.nombre)
        val precioError = validarPrecio(s.precio)
        uiState = s.copy(nombreError = nombreError, precioError = precioError)
        return nombreError == null && precioError == null
    }
    fun guardarProducto(onSuccess: () -> Unit) {
        if (guardando) return
        val s = uiState
        val actual = productoActual
        guardando = true
        errorMessage = null

        viewModelScope.launch{
            val resultado = if (actual == null) {
                repository.crear(s.nombre.trim(), s.precio.toInt(), s.imagen)
            } else {
                repository.actualizar(actual.id, s.nombre.trim(),
                    s.precio.toInt(), s.imagen)
            }
            guardando = false
            if (resultado.isSuccess) {
                onSuccess()
            } else {
                errorMessage = resultado.exceptionOrNull()?.message
            }
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

