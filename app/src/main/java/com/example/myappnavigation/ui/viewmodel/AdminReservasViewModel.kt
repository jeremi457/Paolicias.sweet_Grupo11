package com.example.myappnavigation.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myappnavigation.data.remote.ReservaDto
import com.example.myappnavigation.data.repository.PagosRepository
import com.example.myappnavigation.data.repository.ReservasRepository
import kotlinx.coroutines.launch

class AdminReservasViewModel : ViewModel() {
    private val reservasRepository = ReservasRepository()
    private val pagosRepository = PagosRepository()
    var reservas by mutableStateOf<List<ReservaDto>>(emptyList())
        private set
    var cargando by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    // Bytes de la imagen que se está mostrando (null = ninguna)
    var comprobante by mutableStateOf<ByteArray?>(null)
        private set

    fun cargarReservas() {
        cargando = true
        errorMessage = null
        viewModelScope.launch {
            val resultado = reservasRepository.listarTodas()
            cargando = false
            if (resultado.isSuccess) {
                reservas = resultado.getOrThrow()
            } else {
                errorMessage = resultado.exceptionOrNull()?.message
            }
        }
    }
    fun verComprobante(reservaId: Int) {
        errorMessage = null
        viewModelScope.launch {
            val resultado = pagosRepository.obtenerComprobante(reservaId)
            if (resultado.isSuccess) {
                comprobante = resultado.getOrThrow()
            } else {
                errorMessage = resultado.exceptionOrNull()?.message
            }
        }
    }
    fun cerrarComprobante() {
        comprobante = null
    }
    // aprobado = true confirma la reserva; false la rechaza
    fun validar(reservaId: Int, aprobado: Boolean) {
        errorMessage = null
        viewModelScope.launch {
            val resultado = pagosRepository.validar(reservaId, aprobado)
            if (resultado.isSuccess) {
                cargarReservas()
            } else {
                errorMessage = resultado.exceptionOrNull()?.message
            }
        }
    }
}