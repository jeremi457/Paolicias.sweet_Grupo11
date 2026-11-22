package com.example.myappnavigation.ui.screen

import android.app.DatePickerDialog
import android.content.Context
import java.util.Calendar
import java.util.Locale

fun abrirCalendario(context: Context, onFechaElegida: (String) -> Unit) {
    val hoy = Calendar.getInstance()
    val dialogo = DatePickerDialog(
        context,
        { _, anio, mes, dia ->
            onFechaElegida(String.format(Locale.US, "%02d/%02d/%04d", dia, mes + 1, anio))
        },
        hoy.get(Calendar.YEAR),
        hoy.get(Calendar.MONTH),
        hoy.get(Calendar.DAY_OF_MONTH)
    )
    dialogo.datePicker.minDate = System.currentTimeMillis() - 1000
    dialogo.show()
}