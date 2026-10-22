package com.example.myappnavigation.model

import androidx.compose.ui.graphics.Color

data class ProductoCatalogo(
    val id: Int,
    val nombre: String,
    val precioBase: Int,
    val colorFondo: Color
)

object CatalogoData {
    val productos = listOf(
        ProductoCatalogo(1, "Tortas personalizadas", 25000, Color(0xFFFDE8ED)),
        ProductoCatalogo(2, "Tortas clásicas", 18000, Color(0xFFEFE3D8)),
        ProductoCatalogo(3, "Cupcakes", 12000, Color(0xFFFDE8ED)),
        ProductoCatalogo(4, "Mesa dulce", 45000, Color(0xFFFFF3D6)),
        ProductoCatalogo(5, "Coffee break", 35000, Color(0xFFEBE6F8)),
        ProductoCatalogo(6, "Menú de catering", 60000, Color(0xFFE2F0D9)),
        ProductoCatalogo(7, "Galletas decoradas", 10000, Color(0xFFFDE8ED)),
        ProductoCatalogo(8, "Mini pasteles", 15000, Color(0xFFFFF3D6)),
    )

    fun buscarPorId(id: Int): ProductoCatalogo? = productos.firstOrNull { it.id == id }
}