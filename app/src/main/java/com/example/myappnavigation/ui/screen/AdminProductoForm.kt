package com.example.myappnavigation.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myappnavigation.model.CatalogoData
import com.example.myappnavigation.ui.viewmodel.ProductoFormViewModel
import com.example.myappnavigation.ui.components.MensajeError
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.asImageBitmap
import com.example.myappnavigation.ui.components.ImagenProducto

@Composable
fun AdminProductoFormScreen(
    productoId: Int,
    onBackClick: () -> Unit = {},
    onGuardado: () -> Unit = {},
    viewModel: ProductoFormViewModel = viewModel()
) {
    val state = viewModel.uiState
    val context = LocalContext.current
    val galeria = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.onImagenSeleccionada(uri)
        }
    }

    val vistaPrevia = remember(viewModel.imagenUri) {
        viewModel.imagenUri?.let { uri ->
            val opciones = BitmapFactory.Options().apply { inSampleSize = 4 }
            context.contentResolver.openInputStream(uri)?.use { entrada ->
                BitmapFactory.decodeStream(entrada, null, opciones)
            }?.asImageBitmap()
        }
    }


    LaunchedEffect(productoId) {
        viewModel.cargarProducto(productoId)
    }
    Scaffold(
        containerColor = Color(0xFFF5F6FA)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
// 1. Header Azul con botón de volver
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF4C5BF5),
                        shape = RoundedCornerShape(bottomStart = 28.dp,
                            bottomEnd = 28.dp)
                    )
                    .padding(vertical = 20.dp, horizontal = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "<",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier
                            .clickable { onBackClick() }
                            .padding(horizontal = 8.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (productoId == 0) "Nuevo producto" else
                            "Editar producto",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
// 2. Formulario
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CampoFormulario(
                    titulo = "Nombre del producto",
                    valor = state.nombre,
                    placeholder = "Ej: Brownie",
                    error = state.nombreError,
                    onValueChange = { viewModel.onNombreChanged(it) }
                )
                CampoFormulario(
                    titulo = "Precio base (por cada 10 asistentes)",
                    valor = state.precio,
                    placeholder = "25000",
                    error = state.precioError,
                    keyboardType = KeyboardType.Number,
                    onValueChange = { viewModel.onPrecioChanged(it) }
                )

                Column {
                    Text(
                        text = "Foto del producto",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Button(
                        onClick = {
                            galeria.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEEF0FE),
                            contentColor = Color(0xFF4C5BF5)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (vistaPrevia != null || productoId != 0) "Cambiar foto" else "Elegir foto de la galería",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (vistaPrevia != null) {
                    Image(
                        bitmap = vistaPrevia,
                        contentDescription = "Vista previa del producto",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                } else if (productoId != 0) {
                    ImagenProducto(
                        productoId = productoId,
                        clave = state.imagen,
                        descripcion = "Vista previa del producto",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
                MensajeError(viewModel.errorMessage)
                Button(
                    onClick = {
                        if (viewModel.validarFormulario()) {
                            viewModel.guardarProducto(context) {
                                Toast.makeText(context, "Producto guardado",
                                    Toast.LENGTH_SHORT).show()
                                onGuardado()
                            }
                        }
                    },
                    enabled = !viewModel.guardando,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4C5BF5)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Text(
                        text = "Guardar producto",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


