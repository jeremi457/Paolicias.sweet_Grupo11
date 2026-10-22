package com.example.myappnavigation.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.example.myappnavigation.R

data class ProductoCatalogo(
    val id: Int,
    val nombre: String,
    val precioBase: Int,
    val colorFondo: Color,
    @DrawableRes val imagenRes: Int
)

object CatalogoData {
    val productos = listOf(
        ProductoCatalogo(1, "Tortas personalizadas", 25000, Color(0xFFFDE8ED), R.drawable.torta),
        ProductoCatalogo(2, "Tortas clásicas", 18000, Color(0xFFEFE3D8), R.drawable.torta),
        ProductoCatalogo(3, "Cupcakes", 12000, Color(0xFFFDE8ED), R.drawable.cup),
        ProductoCatalogo(4, "Mesa dulce", 45000, Color(0xFFFFF3D6), R.drawable.mesa),
        ProductoCatalogo(5, "Coffee break", 35000, Color(0xFFEBE6F8), R.drawable.coffe),
        ProductoCatalogo(6, "Menú de catering", 60000, Color(0xFFE2F0D9), R.drawable.catering),
        ProductoCatalogo(7, "Galletas decoradas", 10000, Color(0xFFFDE8ED), R.drawable.galleta),
        ProductoCatalogo(8, "Mini pasteles", 15000, Color(0xFFFFF3D6), R.drawable.mini),
    )

    fun buscarPorId(id: Int): ProductoCatalogo? = productos.firstOrNull { it.id == id }
}