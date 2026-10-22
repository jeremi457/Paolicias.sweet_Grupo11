package com.example.myappnavigation.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myappnavigation.data.database.AppDatabase
import com.example.myappnavigation.data.local.entity.ReservaEntity
import com.example.myappnavigation.model.CatalogoData
import com.example.myappnavigation.model.ReservaModel
import com.example.myappnavigation.model.Tarifario
import com.example.myappnavigation.ui.screen.vibrar
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * ViewModel encargado del formulario de cotizacion y reserva de eventos.
 * Centraliza y desacopla la logica de validacion y calculo de presupuesto.
 */
class ReservaViewModel : ViewModel() {

    var uiState by mutableStateOf(ReservaModel())
        private set

    var guardando by mutableStateOf(false)
        private set

    fun seleccionarProducto(productoId: Int) {
        if (uiState.productoId == productoId) return
        val producto = CatalogoData.buscarPorId(productoId)
        uiState = uiState.copy(
            productoId = productoId,
            productoNombre = producto?.nombre ?: "",
            precioBase = producto?.precioBase ?: 0
        )
    }

    fun onFechaChanged(valor: String) {
        val fecha = valor.take(10)
        uiState = uiState.copy(fecha = fecha, fechaError = validarFecha(fecha))
    }

    fun onHorarioChanged(valor: String) {
        uiState = uiState.copy(horario = valor, horarioError = null)
    }

    fun onAsistentesMas() {
        if (uiState.asistentes < Tarifario.MAX_ASISTENTES) {
            uiState = uiState.copy(asistentes = uiState.asistentes + Tarifario.PASO)
        }
    }

    fun onAsistentesMenos() {
        if (uiState.asistentes > Tarifario.PASO) {
            uiState = uiState.copy(asistentes = uiState.asistentes - Tarifario.PASO)
        }
    }

    fun onTelefonoChanged(valor: String) {
        val soloDigitos = valor.filter { it.isDigit() }.take(9)
        uiState = uiState.copy(telefono = soloDigitos, telefonoError = validarTelefono(soloDigitos))
    }

    fun onObservacionesChanged(valor: String) {
        uiState = uiState.copy(observaciones = valor, observacionesError = validarObservaciones(valor))
    }

    fun validarFormulario(): Boolean {
        val s = uiState
        val fechaError = validarFecha(s.fecha)
        val horarioError = if (s.horario.isBlank()) "Selecciona un horario" else null
        val telefonoError = validarTelefono(s.telefono)
        val observacionesError = validarObservaciones(s.observaciones)

        uiState = s.copy(
            fechaError = fechaError,
            horarioError = horarioError,
            telefonoError = telefonoError,
            observacionesError = observacionesError
        )
        return fechaError == null && horarioError == null &&
                telefonoError == null && observacionesError == null &&
                s.precioBase > 0
    }

    fun guardarReserva(context: Context, onSuccess: (Int) -> Unit) {
        if (guardando) return
        val s = uiState
        val appContext = context.applicationContext
        guardando = true

        viewModelScope.launch {
            val db = AppDatabase.getDatabase(appContext)
            val id = db.reservaDao().insertar(
                ReservaEntity(
                    productoId = s.productoId,
                    productoNombre = s.productoNombre,
                    fecha = s.fecha,
                    horario = s.horario,
                    asistentes = s.asistentes,
                    telefono = s.telefono,
                    observaciones = s.observaciones,
                    total = s.total,
                    abono = s.abono
                )
            ).toInt()

            guardando = false
            try {
                vibrar(appContext, 200)
            } catch (e: Exception) {
                // Silencioso si no hay hardware de vibracion
            }
            onSuccess(id)
        }
    }

    // Reglas de validacion de negocio desacopladas de la interfaz
    private fun validarFecha(fecha: String): String? {
        if (fecha.isBlank()) return "Ingresa la fecha del evento"
        if (!Regex("""^\d{2}/\d{2}/\d{4}$""").matches(fecha)) return "Usa el formato dd/mm/aaaa"

        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        formato.isLenient = false
        val fechaEvento = try {
            formato.parse(fecha)
        } catch (e: ParseException) {
            null
        }
        if (fechaEvento == null) return "La fecha no existe en el calendario"

        val hoy = Calendar.getInstance()
        hoy.set(Calendar.HOUR_OF_DAY, 0)
        hoy.set(Calendar.MINUTE, 0)
        hoy.set(Calendar.SECOND, 0)
        hoy.set(Calendar.MILLISECOND, 0)
        if (fechaEvento.before(hoy.time)) return "La fecha no puede ser anterior a hoy"
        return null
    }

    private fun validarTelefono(telefono: String): String? {
        if (telefono.isBlank()) return "Ingresa un teléfono de contacto"
        if (telefono.length != 9) return "Ingresa un teléfono válido de 9 dígitos"
        return null
    }

    private fun validarObservaciones(texto: String): String? {
        if (texto.length > 200) return "Máximo 200 caracteres"
        return null
    }
}
