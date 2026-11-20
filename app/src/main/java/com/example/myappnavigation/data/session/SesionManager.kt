package com.example.myappnavigation.data.session

import android.content.Context
import com.example.myappnavigation.data.database.AppDatabase
import com.example.myappnavigation.data.local.entity.SesionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Copia en memoria de la sesión activa (la lee el interceptor de Retrofit)
object SesionActual {
    var rol: String? = null
    var nombre: String? = null
    var email: String? = null
    var token: String? = null

    fun activa(): Boolean = rol != null
    fun esAdmin(): Boolean = rol == "ADMIN"
}

object SesionManager {
    suspend fun guardar(
        context: Context,
        rol: String,
        nombre: String,
        email: String,
        token: String?
    ) {
        SesionActual.rol = rol
        SesionActual.nombre = nombre
        SesionActual.email = email
        SesionActual.token = token
        AppDatabase.getDatabase(context).sesionDao()
            .guardar(SesionEntity(rol = rol, nombre = nombre, email = email, token = token))
    }

    // Se llama al abrir la app para recuperar la sesión guardada
    suspend fun restaurar(context: Context) {
        val sesion = AppDatabase.getDatabase(context).sesionDao().obtener()
        if (sesion != null) {
            SesionActual.rol = sesion.rol
            SesionActual.nombre = sesion.nombre
            SesionActual.email = sesion.email
            SesionActual.token = sesion.token
        }
    }

    fun cerrar(context: Context) {
        SesionActual.rol = null
        SesionActual.nombre = null
        SesionActual.email = null
        SesionActual.token = null
        val appContext = context.applicationContext
        CoroutineScope(Dispatchers.IO).launch {
            AppDatabase.getDatabase(appContext).sesionDao().borrar()
        }
    }
}
