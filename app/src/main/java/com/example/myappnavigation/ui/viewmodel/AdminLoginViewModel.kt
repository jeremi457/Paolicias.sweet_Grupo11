package com.example.myappnavigation.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myappnavigation.data.repository.AuthRepository
import com.example.myappnavigation.data.session.SesionManager
import com.example.myappnavigation.model.LoginModel
import kotlinx.coroutines.launch

class AdminLoginViewModel : ViewModel() {
    private val repository = AuthRepository()
    var uiState by mutableStateOf(LoginModel())
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var cargando by mutableStateOf(false)
        private set

    fun onEmailChanged(email: String) {
        uiState = uiState.copy(email = email)
        errorMessage = null
    }

    fun onPasswordChanged(password: String) {
        uiState = uiState.copy(password = password)
        errorMessage = null
    }

    fun onLoginClick(context: Context, onSuccess: () -> Unit) {
        val email = uiState.email.trim()
        val password = uiState.password
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Ingresa correo y contraseña."
            return
        }
        val appContext = context.applicationContext
        cargando = true
        viewModelScope.launch {
            val resultado = repository.login(email, password)
            cargando = false
            if (resultado.isSuccess) {
                val respuesta = resultado.getOrThrow()
                SesionManager.guardar(
                    appContext,
                    respuesta.rol,
                    respuesta.nombre,
                    email,
                    respuesta.token
                )
                onSuccess()
            } else {
                errorMessage = resultado.exceptionOrNull()?.message
            }
        }
    }
}
