package com.example.myappnavigation.data.repository

import com.example.myappnavigation.data.remote.ApiConfig
import com.example.myappnavigation.data.remote.AuthApi
import com.example.myappnavigation.data.remote.LoginRequest
import com.example.myappnavigation.data.remote.LoginResponse
import com.example.myappnavigation.data.remote.RetrofitFactory
import com.example.myappnavigation.data.remote.llamarApi

class AuthRepository {
    private val api = RetrofitFactory.crear(ApiConfig.AUTH_URL, AuthApi::class.java)

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return llamarApi { api.login(LoginRequest(email, password)) }
    }
}
