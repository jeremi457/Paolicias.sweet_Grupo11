package com.example.myappnavigation.model

import com.example.myappnavigation.R
import com.example.myappnavigation.data.local.dao.ProductoDao
import com.example.myappnavigation.data.local.entity.ProductoEntity


object CatalogoData {

    val clavesImagen = listOf("torta", "cup", "mesa", "coffe", "catering",
        "galleta", "mini")

    fun imagenPorClave(clave: String): Int = when (clave) {
        "torta" -> R.drawable.torta
        "cup" -> R.drawable.cup
        "mesa" -> R.drawable.mesa
        "coffe" -> R.drawable.coffe
        "catering" -> R.drawable.catering
        "galleta" -> R.drawable.galleta
        "mini" -> R.drawable.mini
        else -> R.drawable.torta
    }

    val productosIniciales = listOf(
        ProductoEntity(nombre = "Tortas personalizadas", precioBase = 25000,
            imagen = "torta"),
        ProductoEntity(nombre = "Tortas clásicas", precioBase = 18000, imagen
        = "torta"),
        ProductoEntity(nombre = "Cupcakes", precioBase = 12000, imagen =
            "cup"),
        ProductoEntity(nombre = "Mesa dulce", precioBase = 45000, imagen =
            "mesa"),
        ProductoEntity(nombre = "Coffee break", precioBase = 35000, imagen =
            "coffe"),
        ProductoEntity(nombre = "Menú de catering", precioBase = 60000,
            imagen = "catering"),
        ProductoEntity(nombre = "Galletas decoradas", precioBase = 10000,
            imagen = "galleta"),
        ProductoEntity(nombre = "Mini pasteles", precioBase = 15000, imagen =
            "mini")
    )

    suspend fun sembrarSiVacio(dao: ProductoDao) {
        if (dao.contar() == 0) {
            dao.insertarTodos(productosIniciales)
        }
    }
}

