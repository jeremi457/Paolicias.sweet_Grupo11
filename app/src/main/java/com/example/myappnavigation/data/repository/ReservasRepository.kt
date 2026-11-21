package com.example.myappnavigation.data.repository

import com.example.myappnavigation.data.remote.ApiConfig
import com.example.myappnavigation.data.remote.ReservaDto
import com.example.myappnavigation.data.remote.ReservaRequest
import com.example.myappnavigation.data.remote.ReservasApi
import com.example.myappnavigation.data.remote.RetrofitFactory
import com.example.myappnavigation.data.remote.llamarApi

class ReservasRepository {
    private val api = RetrofitFactory.crear(ApiConfig.RESERVAS_URL,
        ReservasApi::class.java)
    suspend fun crear(request: ReservaRequest): Result<ReservaDto> =
        llamarApi { api.crear(request) }
    suspend fun listarPorEmail(email: String): Result<List<ReservaDto>> =
        llamarApi { api.listarPorEmail(email) }
    suspend fun listarTodas(): Result<List<ReservaDto>> = llamarApi {
        api.listarTodas() }
    suspend fun obtener(id: Int): Result<ReservaDto> = llamarApi {
        api.obtener(id) }
}