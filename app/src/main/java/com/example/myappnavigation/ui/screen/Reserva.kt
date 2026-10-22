package com.example.myappnavigation.ui.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myappnavigation.model.Tarifario
import com.example.myappnavigation.ui.components.IconoError
import com.example.myappnavigation.ui.components.MensajeError
import com.example.myappnavigation.ui.viewmodel.ReservaViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ReservaScreen(
    productoId: Int,
    onBackClick: () -> Unit = {},
    onReservaGuardada: (reservaId: Int) -> Unit = {},
    viewModel: ReservaViewModel = viewModel()
) {
    val state = viewModel.uiState
    val context = LocalContext.current
    val formatoMoneda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CL"))

    // Carga el producto elegido en el catálogo
    LaunchedEffect(productoId) {
        viewModel.seleccionarProducto(productoId)
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
                // Producto seleccionado en el catálogo
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFDE8ED)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Producto seleccionado", color = Color.Gray, fontSize = 12.sp)
                        Text(
                            text = state.productoNombre,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF222222)
                        )
                        Text(
                            text = "Desde ${formatoMoneda.format(state.precioBase)} por cada 10 asistentes",
                            fontSize = 13.sp,
                            color = Color(0xFF555555)
                        )
                    }
                }

                // Fecha
                CampoFormulario(
                    titulo = "Fecha del evento",
                    valor = state.fecha,
                    placeholder = "dd/mm/aaaa",
                    error = state.fechaError,
                    onValueChange = { viewModel.onFechaChanged(it) }
                )

                // Horario (tres opciones)
                Column {
                    Text(text = "Horario", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Mañana", "Tarde", "Noche").forEach { opcion ->
                            val seleccionado = state.horario == opcion
                            Button(
                                onClick = { viewModel.onHorarioChanged(opcion) },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (seleccionado) Color(0xFF4C5BF5) else Color(0xFFEEF0FE),
                                    contentColor = if (seleccionado) Color.White else Color(0xFF4C5BF5)
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(opcion, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    MensajeError(state.horarioError)
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
                        Button(
                            onClick = { viewModel.onAsistentesMenos() },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEEF0FE)),
                            modifier = Modifier.size(54.dp)
                        ) {
                            Text("-", fontSize = 24.sp, color = Color(0xFF4C5BF5), fontWeight = FontWeight.Bold)
                        }

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
                                    text = "${state.asistentes}",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E1E1E)
                                )
                            }
                        }

                        Button(
                            onClick = { viewModel.onAsistentesMas() },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEEF0FE)),
                            modifier = Modifier.size(54.dp)
                        ) {
                            Text("+", fontSize = 22.sp, color = Color(0xFF4C5BF5), fontWeight = FontWeight.Bold)
                        }
                    }

                    Text(
                        text = "De ${Tarifario.PASO} en ${Tarifario.PASO}, máximo ${Tarifario.MAX_ASISTENTES}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Teléfono
                CampoFormulario(
                    titulo = "Teléfono de contacto",
                    valor = state.telefono,
                    placeholder = "912345678",
                    error = state.telefonoError,
                    keyboardType = KeyboardType.Phone,
                    onValueChange = { viewModel.onTelefonoChanged(it) }
                )

                // Observaciones (opcional)
                CampoFormulario(
                    titulo = "Observaciones (opcional)",
                    valor = state.observaciones,
                    placeholder = "Alergias, celíacos, vegetarianos...",
                    error = state.observacionesError,
                    singleLine = false,
                    modifier = Modifier.height(110.dp),
                    onValueChange = { viewModel.onObservacionesChanged(it) }
                )

                // Resumen: los montos cambian con animación al variar los asistentes
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
                            AnimatedContent(targetState = state.total, label = "total") { total ->
                                Text(
                                    text = formatoMoneda.format(total),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color(0xFF222222)
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Abono 50% para reservar", color = Color(0xFF4C5BF5), fontSize = 14.sp)
                            AnimatedContent(targetState = state.abono, label = "abono") { abono ->
                                Text(
                                    text = formatoMoneda.format(abono),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color(0xFF4C5BF5)
                                )
                            }
                        }
                    }
                }

                // Botón Confirmar Reserva
                Button(
                    onClick = {
                        if (viewModel.validarFormulario()) {
                            viewModel.guardarReserva(context) { reservaId ->
                                Toast.makeText(context, "¡Reserva registrada!", Toast.LENGTH_SHORT).show()
                                onReservaGuardada(reservaId)
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
                        text = "Reservar y subir comprobante",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// Campo con título, borde rojo + ícono "!" y mensaje animado cuando hay error
@Composable
private fun CampoFormulario(
    titulo: String,
    valor: String,
    placeholder: String,
    error: String?,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column {
        Text(text = titulo, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = valor,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            isError = error != null,
            trailingIcon = {
                if (error != null) {
                    IconoError()
                }
            },
            shape = RoundedCornerShape(12.dp),
            modifier = modifier.fillMaxWidth()
        )
        MensajeError(error)
    }
}
