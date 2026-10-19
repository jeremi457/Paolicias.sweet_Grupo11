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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ReservaScreen(
    onBackClick: () -> Unit = {},
    onReservarClick: (fecha: String, horario: String, asistentes: Int, telefono: String, observaciones: String, total: Int) -> Unit = { _, _, _, _, _, _ -> }
) {
    // Estados del formulario
    var fecha by remember { mutableStateOf("25/11/2026") }
    var horario by remember { mutableStateOf("Tarde") }
    var asistentes by remember { mutableIntStateOf(50) } // Inicializado en 50 como el mockup
    var telefono by remember { mutableStateOf("12345") }
    var observaciones by remember { mutableStateOf("Dos invitados celíacos") }

    // Validación de teléfono (exactamente 9 dígitos)
    val esTelefonoInvalido = telefono.length != 9 || !telefono.all { it.isDigit() }

    // Cálculo estimado (ejemplo: $3.000 por asistente)
    val precioPorPersona = 3000
    val totalEstimado = asistentes * precioPorPersona
    val abonoEstimado = totalEstimado / 2

    // Formateador de moneda chilena
    val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "CL"))

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
                        shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
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
                        text = "Reservar evento",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // 2. Formulario con Scroll
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Fecha y Horario
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Fecha", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = fecha,
                            onValueChange = { fecha = it },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Horario", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = horario,
                            onValueChange = { horario = it },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Asistentes (Contador de 10 en 10 hasta 100)
                Column {
                    Text(text = "Asistentes", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Botón Restar (-)
                        Button(
                            onClick = { if (asistentes > 10) asistentes -= 10 },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEEF0FE)),
                            modifier = Modifier.size(54.dp)
                        ) {
                            Text("-", fontSize = 24.sp, color = Color(0xFF4C5BF5), fontWeight = FontWeight.Bold)
                        }

                        // Número de Asistentes
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier
                                .weight(1f)
                                .height(54.dp)
                                .padding(horizontal = 12.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$asistentes",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E1E1E)
                                )
                            }
                        }

                        // Botón Sumar (+)
                        Button(
                            onClick = { if (asistentes < 100) asistentes += 10 },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEEF0FE)),
                            modifier = Modifier.size(54.dp)
                        ) {
                            Text("+", fontSize = 22.sp, color = Color(0xFF4C5BF5), fontWeight = FontWeight.Bold)
                        }
                    }

                    Text(
                        text = "De 10 en 10, máximo 100",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Teléfono de contacto (con alerta de error visual)
                Column {
                    Text(text = "Teléfono de contacto", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = telefono,
                        onValueChange = { telefono = it },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        isError = esTelefonoInvalido,
                        trailingIcon = {
                            if (esTelefonoInvalido) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(Color(0xFFD32F2F), shape = CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("!", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (esTelefonoInvalido) Color(0xFFD32F2F) else Color(0xFF4C5BF5),
                            unfocusedBorderColor = if (esTelefonoInvalido) Color(0xFFD32F2F) else Color.Gray
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (esTelefonoInvalido) {
                        Text(
                            text = "Ingresa un teléfono válido de 9 dígitos",
                            color = Color(0xFFD32F2F),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                // Observaciones
                Column {
                    Text(text = "Observaciones", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = observaciones,
                        onValueChange = { observaciones = it },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                    )
                }

                // Card de Resumen (Total estimado y Abono 50%)
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Total estimado", color = Color.Gray, fontSize = 14.sp)
                            Text(
                                text = formatoMoneda.format(totalEstimado),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFF222222)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Abono 50% para reservar", color = Color(0xFF4C5BF5), fontSize = 14.sp)
                            Text(
                                text = formatoMoneda.format(abonoEstimado),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFF4C5BF5)
                            )
                        }
                    }
                }

                // Botón Confirmar Reserva
                Button(
                    onClick = {
                        if (!esTelefonoInvalido) {
                            onReservarClick(fecha, horario, asistentes, telefono, observaciones, totalEstimado)
                        }
                    },
                    enabled = !esTelefonoInvalido,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4C5BF5)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Text(
                        text = "Reservar y subir comprobante",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // 3. Barra de Navegación Inferior
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
                    Text(text = "Inicio", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                    Text(text = "Catálogo", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                    Text(text = "Mis reservas", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(text = "Perfil", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                }
            }
        }
    }
}