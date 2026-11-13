package com.example.myappnavigation.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myappnavigation.data.local.entity.ProductoEntity
import com.example.myappnavigation.model.CatalogoData
import com.example.myappnavigation.ui.viewmodel.AdminProductosViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AdminProductosScreen(
    onNuevoClick: () -> Unit = {},
    onEditarClick: (productoId: Int) -> Unit = {},
    onCerrarSesionClick: () -> Unit = {},
    viewModel: AdminProductosViewModel = viewModel()
) {
    val context = LocalContext.current
    val productos = viewModel.productos
    val formatoMoneda =
        NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CL"))

    LaunchedEffect(Unit) {
        viewModel.cargarProductos(context)
    }

    Scaffold(
        containerColor = Color(0xFFF5F6FA)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF4C5BF5),
                        shape = RoundedCornerShape(bottomStart = 28.dp,
                            bottomEnd = 28.dp)
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Text(
                        text = "Panel de administración",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Productos del catálogo",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
// 2. Lista de productos
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onNuevoClick,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor =
                        Color(0xFF4C5BF5)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Text(
                        text = "Nuevo producto",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                productos.forEach { producto ->
                    TarjetaProductoAdmin(
                        producto = producto,
                        formatoMoneda = formatoMoneda,
                        onEditarClick = { onEditarClick(producto.id) },
                        onCambiarEstadoClick = {
                            viewModel.cambiarActivo(context, producto) }
                    )
                }
            }
// 3. Cerrar sesión
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Button(
                    onClick = onCerrarSesionClick,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor =
                        Color(0xFFEEF0FE)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = "Cerrar sesión",
                        color = Color(0xFF4C5BF5),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun TarjetaProductoAdmin(
    producto: ProductoEntity,
    formatoMoneda: NumberFormat,
    onEditarClick: () -> Unit,
    onCambiarEstadoClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id =
                        CatalogoData.imagenPorClave(producto.imagen)),
                    contentDescription = producto.nombre,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = producto.nombre,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF222222)
                    )
                    Text(
                        text = "Desde${formatoMoneda.format(producto.precioBase)} c/10 pers.",
                    fontSize = 12.sp,
                    color = Color.Gray
                    )
                }

                Box(
                    modifier = Modifier
                        .background(
                            color = if (producto.activo) Color(0xFFE2F0D9)
                            else Color(0xFFEEEEEE),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (producto.activo) "Activo" else "Inactivo",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (producto.activo) Color(0xFF2E7D32) else
                            Color(0xFF757575)
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onEditarClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor =
                        Color(0xFFEEF0FE)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Editar",
                        color = Color(0xFF4C5BF5),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
                Button(
                    onClick = onCambiarEstadoClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor =
                        Color(0xFFEEF0FE)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = if (producto.activo) "Desactivar" else
                            "Activar",
                        color = Color(0xFF4C5BF5),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
