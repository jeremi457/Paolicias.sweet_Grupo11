package com.example.myappnavigation.data.remote

object ApiConfig {
    // Emulador de Android Studio: 10.0.2.2 apunta al computador donde corre Docker.
    // Celular real: cambia HOST por la IP del computador, por ejemplo http://192.168.1.50
    // Nube: usa la URL https de cada servicio.
    private const val HOST = "http://10.0.2.2"
    const val AUTH_URL = "$HOST:8081/"
    const val CATALOGO_URL = "$HOST:8082/"
    const val RESERVAS_URL = "$HOST:8083/"
    const val PAGOS_URL = "$HOST:8084/"
}
