package com.example.myappnavigation.util

object FechaUtil {
    // "15/03/2027" -> "2027-03-15"
    fun aIso(fecha: String): String {
        val partes = fecha.split("/")
        return if (partes.size == 3) "${partes[2]}-${partes[1]}-${partes[0]}"
        else fecha
    }
    // "2027-03-15" -> "15/03/2027"
    fun aVista(iso: String): String {
        val partes = iso.split("-")
        return if (partes.size == 3) "${partes[2]}/${partes[1]}/${partes[0]}"
        else iso
    }
}