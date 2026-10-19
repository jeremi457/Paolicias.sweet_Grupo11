package com.example.myappnavigation.ui.screen

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ProductoCatalogo(
    val id: Int,
    val nombre: String,
    val precioBase: String,
    val colorFondo: Color
)

@Composable
fun CatalogScreen(
    onProductoClick: (ProductoCatalogo) -> Unit = {}
) {
    // 16 opciones de productos para el catálogo
    val productos = listOf(
        ProductoCatalogo(1, "Tortas personalizadas", "Desde $25.000", Color(0xFFFDE8ED)),
        ProductoCatalogo(2, "Tortas clásicas", "Desde $18.000", Color(0xFFEFE3D8)),
        ProductoCatalogo(3, "Cupcakes", "Desde $12.000", Color(0xFFFDE8ED)),
        ProductoCatalogo(4, "Mesa dulce", "Desde $45.000", Color(0xFFFFF3D6)),
        ProductoCatalogo(5, "Coffee break", "Desde $35.000", Color(0xFFEBE6F8)),
        ProductoCatalogo(6, "Menú de catering", "Desde $60.000", Color(0xFFE2F0D9)),
        ProductoCatalogo(7, "Galletas decoradas", "Desde $10.000", Color(0xFFFDE8ED)),
        ProductoCatalogo(8, "Mini pasteles", "Desde $15.000", Color(0xFFFFF3D6)),
        ProductoCatalogo(9, "Postres en shot", "Desde $14.000", Color(0xFFEBE6F8)),
        ProductoCatalogo(10, "Tartas de fruta", "Desde $16.000", Color(0xFFE2F0D9)),
        ProductoCatalogo(11, "Cheesecake", "Desde $20.000", Color(0xFFEFE3D8)),
        ProductoCatalogo(12, "Pie de limón", "Desde $14.000", Color(0xFFFFF3D6)),
        ProductoCatalogo(13, "Muffins salados", "Desde $12.000", Color(0xFFFDE8ED)),
        ProductoCatalogo(14, "Empanaditas cóctel", "Desde $18.000", Color(0xFFEFE3D8)),
        ProductoCatalogo(15, "Canapés variados", "Desde $22.000", Color(0xFFEBE6F8)),
        ProductoCatalogo(16, "Tablas picoteo", "Desde $30.000", Color(0xFFE2F0D9))
    )

    Scaffold(
        containerColor = Color(0xFFF5F6FA)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Header Azul con Isotipo y Título
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF4C5BF5),
                        shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                    )
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(Color.White, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Paolicias\nSweet",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4C5BF5),
                            textAlign = TextAlign.Center,
                            lineHeight = 10.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "Catálogo",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Grid ordenado de 2 en 2 mediante desplazamiento vertical
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // .chunked(2) divide la lista en parejas de a 2 elementos por fila
                productos.chunked(2).forEach { parDeProductos ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        for (producto in parDeProductos) {
                            Box(modifier = Modifier.weight(1f)) {
                                TarjetaProducto(
                                    producto = producto,
                                    onClick = { onProductoClick(producto) }
                                )
                            }
                        }

                        // Relleno de espacio si el número total fuera impar
                        if (parDeProductos.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaProducto(
    producto: ProductoCatalogo,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Área de Imagen (Espacio reservado con color de fondo)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(
                        color = producto.colorFondo,
                        shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "[Imagen]",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Texto de Nombre y Precio Base
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = producto.nombre,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF222222),
                    maxLines = 2,
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = producto.precioBase,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}