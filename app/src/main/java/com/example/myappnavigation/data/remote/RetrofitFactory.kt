package com.example.myappnavigation.data.remote

import com.example.myappnavigation.data.session.SesionActual
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitFactory {
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val cliente = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val path = original.url.encodedPath
            val token = SesionActual.token

            // Evita enviar un token viejo/expirado al hacer login o registro
            val esRutaPublica = path.contains("/login") || path.contains("/register")

            val peticion = if (!token.isNullOrBlank() && !esRutaPublica) {
                original.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
            } else {
                original
            }
            chain.proceed(peticion)
        }
        .addInterceptor(logging)
        .connectTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .build()

    fun <T> crear(baseUrl: String, api: Class<T>): T {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(cliente)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)
    }
}
