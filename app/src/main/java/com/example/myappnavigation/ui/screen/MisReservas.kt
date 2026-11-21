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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myappnavigation.ui.components.BarraInferior
import com.example.myappnavigation.ui.viewmodel.ReservasViewModel
import java.text.NumberFormat
import java.util.Locale
import com.example.myappnavigation.data.remote.ReservaDto
import com.example.myappnavigation.model.EstadoReserva
import com.example.myappnavigation.util.FechaUtil

@Composable
fun ReservationsScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToCatalog: () -> Unit = {},
    onSubirComprobanteClick: (reservaId: Int) -> Unit = {},
    viewModel: ReservasViewModel = viewModel()
) {
    val reservas = viewModel.reservas
    val formatoMoneda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CL"))

    // Lee las reservas guardadas en Room al abrir la pantalla
    LaunchedEffect(Unit) {
        viewModel.cargarReservas()
    }

    Scaffold(
        containerColor = Color(0xFFF5F6FA)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 1. Header Azul
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF4C5BF5),
                        shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                    )
                    .padding(vertical = 20.dp, horizontal = 20.dp)
            ) {
                Text(
                    text = "Mis reservas",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // 2. Lista de reservas o estado vacío
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (viewModel.cargando) {
                    Text(text = "Cargando reservas...", color = Color.Gray)
                }
                if (viewModel.errorMessage != null) {
                    Text(text = viewModel.errorMessage!!, color = Color.Red)
                    Button(onClick = { viewModel.cargarReservas() }) {
                        Text("Reintentar")
                    }
                }

                if (reservas.isEmpty() && !viewModel.cargando && viewModel.errorMessage == null) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Aún no tienes reservas",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF222222)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Elige un producto del catálogo y cotiza tu evento especial.",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Button(
                                onClick = onNavigateToCatalog,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4C5BF5))
                            ) {
                                Text("Ver catálogo", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                } else {
                    reservas.forEach { reserva ->
                        TarjetaReserva(
                            reserva = reserva,
                            formatoMoneda = formatoMoneda,
                            onSubirComprobanteClick = { onSubirComprobanteClick(reserva.id) }
                        )
                    }
                }
            }

            // 3. Barra de navegación inferior
            BarraInferior(
                seleccionado = "Mis reservas",
                onInicioClick = onNavigateToHome,
                onCatalogoClick = onNavigateToCatalog,
                onReservasClick = {}
            )
        }
    }
}

@Composable
fun TarjetaReserva(
    reserva: ReservaDto,
    formatoMoneda: NumberFormat,
    onSubirComprobanteClick: () -> Unit,
    mostrarBotonPago: Boolean = true,
    mostrarCliente: Boolean = false
) {
    val puedePagar = reserva.estado == EstadoReserva.PENDIENTE_PAGO ||
            reserva.estado == EstadoReserva.RECHAZADA
    val (colorFondo, colorTexto) = when (reserva.estado) {
        EstadoReserva.CONFIRMADA -> Color(0xFFE2F0D9) to Color(0xFF2E7D32)
        EstadoReserva.RECHAZADA -> Color(0xFFFDE0E0) to Color(0xFFC62828)
        EstadoReserva.COMPROBANTE_ENVIADO -> Color(0xFFDDE3FF) to
                Color(0xFF3F4BC4)
        else -> Color(0xFFFFF3D6) to Color(0xFFB78103)
    }
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "Evento ${FechaUtil.aVista(reserva.fecha)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF222222)
                )
                Box(
                    modifier = Modifier
                        .background(color = colorFondo, shape =
                            RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = EstadoReserva.texto(reserva.estado),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorTexto
                    )}
            }
            Text(
                text = reserva.productoNombre,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF222222)
            )
            if (mostrarCliente) {
                Text(
                    text = "Cliente: ${reserva.clienteEmail} • Tel.${reserva.telefono}",
                fontSize = 13.sp,
                color = Color(0xFF555555)
                )
            }
            Text(
                text = "Horario: ${reserva.horario} • ${reserva.asistentes} asistentes",
                        fontSize = 14.sp,
                color = Color.Gray
            )
            if (!reserva.observaciones.isNullOrBlank()) {
                Text(
                    text = "Notas: \"${reserva.observaciones}\"",
                    fontSize = 13.sp,
                    color = Color(0xFF555555)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Total:${formatoMoneda.format(reserva.total)}", fontSize = 13.sp, color =Color.Gray)
                    Text(
                        text = "Abono 50%:${formatoMoneda.format(reserva.abono)}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4C5BF5)
                    )
                }
                // Solo el cliente, y solo si aún no pagó, ve el botón de subir el comprobante
                if (mostrarBotonPago && puedePagar) {
                    Button(
                        onClick = onSubirComprobanteClick,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor =
                            Color(0xFFEEF0FE))
                    ) {
                        Text(
                            text = if (reserva.estado == EstadoReserva.RECHAZADA) "Subir otro" else "Subir pago",
                            color = Color(0xFF4C5BF5),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

