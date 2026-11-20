package com.example.myappnavigation.model

import com.example.myappnavigation.R


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
}

