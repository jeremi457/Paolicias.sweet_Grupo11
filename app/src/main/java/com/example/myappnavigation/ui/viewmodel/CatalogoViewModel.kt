package com.example.myappnavigation.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myappnavigation.data.remote.ProductoDto
import com.example.myappnavigation.data.repository.CatalogoRepository
import kotlinx.coroutines.launch

class CatalogoViewModel : ViewModel() {

    private val repository = CatalogoRepository()
    var productos by mutableStateOf<List<ProductoDto>>(emptyList())
        private set
    var cargando by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    fun cargarProductos() {
        cargando = true
        errorMessage = null
        viewModelScope.launch {
            val resultado = repository.listarActivos()
            cargando = false
            if (resultado.isSuccess) {
                productos = resultado.getOrThrow()
            } else {
                errorMessage = resultado.exceptionOrNull()?.message
            }
        }
    }
}
