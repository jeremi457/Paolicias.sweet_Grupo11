package com.example.myappnavigation.model

data class ProductoFormModel(
    val nombre: String = "",
    val precio: String = "",
    val imagen: String = "torta",
    val nombreError: String? = null,
    val precioError: String? = null
)
