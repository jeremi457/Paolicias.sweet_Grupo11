package com.example.myappnavigation.ui.screen

import android.Manifest
import android.content.Context
import android.media.AudioAttributes
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.annotation.RequiresPermission

@RequiresPermission(Manifest.permission.VIBRATE)
fun vibrar(context: Context, milisegundos: Long = 400) {
    try {
        @Suppress("DEPRECATION")
        val vibrador = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator

        if ((vibrador == null) || !vibrador.hasVibrator()) {
            Log.e("Vibracion", "El dispositivo no cuenta con motor de vibración o es nulo.")
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()

            val efecto = VibrationEffect.createOneShot(milisegundos, 255)
            @Suppress("DEPRECATION")
            vibrador.vibrate(efecto, audioAttributes)
        } else {
            @Suppress("DEPRECATION")
            vibrador.vibrate(milisegundos)
        }
        Log.d("Vibracion", ">>> VIBRACIÓN EJECUTADA CORRECTAMENTE ($milisegundos ms) <<<")
    } catch (e: Throwable) {
        Log.e("Vibracion", "Error al ejecutar la vibración: ${e.message}", e)
    }
}