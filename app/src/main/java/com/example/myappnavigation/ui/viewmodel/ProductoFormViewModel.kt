package com.example.myappnavigation.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myappnavigation.data.database.AppDatabase
import com.example.myappnavigation.data.local.entity.ProductoEntity
import com.example.myappnavigation.model.ProductoFormModel
import kotlinx.coroutines.launch

class ProductoFormViewModel: ViewModel() {

    var uiState by mutableStateOf(ProductoFormModel())
        private set

    private var productoActual: ProductoEntity? = null
    fun cargarProducto(context: Context, id: Int) {
        if (id == 0 || productoActual != null) return
        val appContext = context.applicationContext

        viewModelScope.launch {
            val producto =
                AppDatabase.getDatabase(appContext).productoDao().obtenerPorId(id)
            if (producto != null) {
                productoActual = producto
                uiState = uiState.copy(
                    nombre = producto.nombre,
                    precio = producto.precioBase.toString(),
                    imagen = producto.imagen
                )
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
        uiState = s.copy(nombreError = nombreError, precioError =
            precioError)
        return nombreError == null && precioError == null
    }

    fun guardarProducto(context: Context, onSuccess: () -> Unit) {
        val s = uiState
        val appContext = context.applicationContext
        viewModelScope.launch {
            val dao = AppDatabase.getDatabase(appContext).productoDao()
            val actual = productoActual
            if (actual == null) {
                dao.insertar(
                    ProductoEntity(
                        nombre = s.nombre.trim(),
                        precioBase = s.precio.toInt(),
                        imagen = s.imagen
                    )
                )
            } else {
                dao.actualizar(
                    actual.copy(
                        nombre = s.nombre.trim(),
                        precioBase = s.precio.toInt(),
                        imagen = s.imagen
                    )
                )
            }
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
