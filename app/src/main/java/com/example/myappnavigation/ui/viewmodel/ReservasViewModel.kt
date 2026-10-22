package com.example.myappnavigation.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myappnavigation.data.database.AppDatabase
import com.example.myappnavigation.data.local.entity.EstadoReserva
import com.example.myappnavigation.data.local.entity.ReservaEntity
import kotlinx.coroutines.launch

/**
 * ViewModel para la gestion y consulta de reservas registradas y carga de comprobantes.
 */
class ReservasViewModel : ViewModel() {

    var reservas by mutableStateOf<List<ReservaEntity>>(emptyList())
        private set

    var reservaActual by mutableStateOf<ReservaEntity?>(null)
        private set

    var imagenUri by mutableStateOf<Uri?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun cargarReservas(context: Context) {
        val appContext = context.applicationContext
        viewModelScope.launch {
            reservas = AppDatabase.getDatabase(appContext).reservaDao().obtenerTodas()
        }
    }

    fun cargarReserva(context: Context, id: Int) {
        val appContext = context.applicationContext
        viewModelScope.launch {
            reservaActual = AppDatabase.getDatabase(appContext).reservaDao().obtenerPorId(id)
        }
    }

    fun onImagenSeleccionada(uri: Uri) {
        imagenUri = uri
        errorMessage = null
    }

    fun confirmarComprobante(context: Context, onSuccess: () -> Unit) {
        val reserva = reservaActual ?: return
        val uri = imagenUri
        if (uri == null) {
            errorMessage = "Debes seleccionar una imagen del comprobante"
            return
        }

        val appContext = context.applicationContext
        viewModelScope.launch {
            AppDatabase.getDatabase(appContext).reservaDao().actualizarComprobante(
                id = reserva.id,
                uri = uri.toString(),
                estado = EstadoReserva.COMPROBANTE_ENVIADO
            )
            onSuccess()
        }
    }
}
