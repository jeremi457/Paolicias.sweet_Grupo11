package com.example.myappnavigation.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myappnavigation.data.remote.ReservaDto
import com.example.myappnavigation.data.repository.ReservasRepository
import com.example.myappnavigation.data.session.SesionActual
import kotlinx.coroutines.launch

class ReservasViewModel : ViewModel() {
    private val repository = ReservasRepository()
    var reservas by mutableStateOf<List<ReservaDto>>(emptyList())
        private set
    var reservaActual by mutableStateOf<ReservaDto?>(null)
        private set
    var imagenUri by mutableStateOf<Uri?>(null)
        private set
    var cargando by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    // El administrador ve todas las reservas; el cliente, solo las suyas
    fun cargarReservas() {
        cargando = true
        errorMessage = null
        viewModelScope.launch {
            val resultado = if (SesionActual.esAdmin()) {
                repository.listarTodas()
            } else {
                repository.listarPorEmail(SesionActual.email ?: "")
            }
            cargando = false
            if (resultado.isSuccess) {
                reservas = resultado.getOrThrow()
            } else {
                errorMessage = resultado.exceptionOrNull()?.message
            }
        }
    }
    fun cargarReserva(id: Int) {
        viewModelScope.launch {
            val resultado = repository.obtener(id)
            if (resultado.isSuccess) {
                reservaActual = resultado.getOrThrow()
            } else {
                errorMessage = resultado.exceptionOrNull()?.message
            }
        }
    }
    fun onImagenSeleccionada(uri: Uri) {
        imagenUri = uri
        errorMessage = null
    }
    // Se completa en la Guía 4, con pagos-service
    fun confirmarComprobante(context: Context, onSuccess: () -> Unit) {
        errorMessage = "El envío del comprobante se activa en la Guía 4(pagos-service)."
    }
}