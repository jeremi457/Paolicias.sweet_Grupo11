package com.example.myappnavigation.ui.screen

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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

@Composable
fun Principal(
    userName: String = "María González",
    onNavigateToCatalog: () -> Unit = {},
    onNavigateToQuoter: () -> Unit = {},
    onNavigateToReservations: () -> Unit = {},
    onNavigateToUploadReceipt: () -> Unit = {}
) {
    Scaffold(
        containerColor = Color(0xFFF5F6FA)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 1. Encabezado Azul Superior (Isotipo + Saludo)
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
                    // Isotipo circular Paolicias Sweet
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .background(Color.White, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Paolicias\nSweet",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4C5BF5),
                            textAlign = TextAlign.Center,
                            lineHeight = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Saludo de bienvenida
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "¡Bienvenida!",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 13.sp
                        )
                        Text(
                            text = userName,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // 2. Contenido Central (Banner + Opciones)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Banner destacado
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFDE8ED), shape = RoundedCornerShape(16.dp))
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Tortas,\npastelería y\ncatering",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF232323),
                        lineHeight = 22.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Lista de botones de opciones del menú
                BotonOpcionMenu(
                    texto = "Catálogo",
                    onClick = onNavigateToCatalog
                )
                Spacer(modifier = Modifier.height(10.dp))

                BotonOpcionMenu(
                    texto = "Cotizar evento",
                    onClick = onNavigateToQuoter
                )
                Spacer(modifier = Modifier.height(10.dp))

                BotonOpcionMenu(
                    texto = "Mis reservas",
                    onClick = onNavigateToReservations
                )
                Spacer(modifier = Modifier.height(10.dp))

                BotonOpcionMenu(
                    texto = "Subir comprobante",
                    onClick = onNavigateToUploadReceipt
                )
            }

            // 3. Barra de Navegación Inferior (Basada en Row y Text)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF4C5BF5))
                    .padding(vertical = 12.dp, horizontal = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Inicio",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Catálogo",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Mis reservas",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Perfil",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun BotonOpcionMenu(
    texto: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color(0xFF222222)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = texto,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = ">",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4C5BF5)
            )
        }
    }
}