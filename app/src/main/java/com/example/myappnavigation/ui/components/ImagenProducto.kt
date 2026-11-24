package com.example.myappnavigation.ui.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.myappnavigation.data.remote.ApiConfig
import com.example.myappnavigation.model.CatalogoData
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ImagenProducto(
    productoId: Int,
    clave: String,
    descripcion: String,
    modifier: Modifier = Modifier
) {
    if (clave == "foto") {
        val foto by produceState<ImageBitmap?>(initialValue = null, productoId, clave) {
            value = withContext(Dispatchers.IO) {
                try {
                    val conexion = URL("${ApiConfig.CATALOGO_URL}productos/$productoId/imagen").openConnection()
                    conexion.connectTimeout = 15000
                    conexion.readTimeout = 90000
                    val bytes = conexion.getInputStream().use { it.readBytes() }
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
                } catch (e: Exception) {
                    null
                }
            }
        }
        val cargada = foto
        if (cargada != null) {
            Image(
                bitmap = cargada,
                contentDescription = descripcion,
                contentScale = ContentScale.Crop,
                modifier = modifier
            )
        } else {
            Box(modifier = modifier.background(Color(0xFFEEF0FE)))
        }
    } else {
        Image(
            painter = painterResource(id = CatalogoData.imagenPorClave(clave)),
            contentDescription = descripcion,
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    }
}