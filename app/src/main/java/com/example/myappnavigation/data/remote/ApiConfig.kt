package com.example.myappnavigation.data.remote

object ApiConfig {

    private const val USE_RENDER = true

    private const val LOCAL_HOST = "http://10.0.2.2"

    val AUTH_URL = if (USE_RENDER) {
        "https://auth-service-740w.onrender.com/"
    } else {
        "$LOCAL_HOST:8081/"
    }

    // Reemplaza estas 3 URLs a medida que despliegues cada microservicio en Render
    val CATALOGO_URL = if (USE_RENDER) {
        "https://catalogo-service-m6mq.onrender.com/"
    } else {
        "$LOCAL_HOST:8082/"
    }

    val RESERVAS_URL = if (USE_RENDER) {
        "https://TU-RESERVAS-SERVICE.onrender.com/"
    } else {
        "$LOCAL_HOST:8083/"
    }

    val PAGOS_URL = if (USE_RENDER) {
        "https://TU-PAGOS-SERVICE.onrender.com/"
    } else {
        "$LOCAL_HOST:8084/"
    }
}
