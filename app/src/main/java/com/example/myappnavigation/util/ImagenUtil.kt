package com.example.myappnavigation.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream

object ImagenUtil {
    // Reduce la imagen a un máximo de 1280 px de ancho y la comprime como JPEG
    fun comprimir(context: Context, uri: Uri, anchoMaximo: Int = 1280, calidad: Int = 70): ByteArray? {

        val medidas = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, medidas)
        }
        var muestreo = 1
        while (medidas.outWidth / (muestreo * 2) >= anchoMaximo) {
            muestreo *= 2
        }
        val opciones = BitmapFactory.Options().apply { inSampleSize =
            muestreo }
        val original = context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, opciones)
        } ?: return null
        val imagen = if (original.width > anchoMaximo) {
            val alto = (original.height * anchoMaximo.toFloat() /
                    original.width).toInt()
            Bitmap.createScaledBitmap(original, anchoMaximo, alto, true)
        } else {
            original
        }
        val salida = ByteArrayOutputStream()
        imagen.compress(Bitmap.CompressFormat.JPEG, calidad, salida)
        return salida.toByteArray()
    }
}