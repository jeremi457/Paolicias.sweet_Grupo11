package com.example.myappnavigation.ui.screen

import android.graphics.BitmapFactory
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myappnavigation.model.EstadoReserva
import com.example.myappnavigation.ui.viewmodel.AdminReservasViewModel
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.ui.platform.LocalContext

@Composable
fun AdminReservasScreen(
    onBackClick: () -> Unit = {},
    viewModel: AdminReservasViewModel = viewModel()
) {
    val formatoMoneda =
        NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CL"))
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.cargarReservas()
    }
    // Ventana con la imagen del comprobante (aparece cuando hay bytes que mostrar)
    val bytes = viewModel.comprobante
    if (bytes != null) {
        val imagen = remember(bytes) {
            BitmapFactory.decodeByteArray(bytes, 0,
                bytes.size)?.asImageBitmap()
        }
        AlertDialog(
            onDismissRequest = { viewModel.cerrarComprobante() },
            confirmButton = {
                TextButton(onClick = { viewModel.cerrarComprobante() }) {
                    Text("Cerrar")
                }
            },
            title = { Text("Comprobante de transferencia") },
            text = {
                if (imagen != null) {
                    Image(
                        bitmap = imagen,
                        contentDescription = "Comprobante de transferencia",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp)
                    )
                } else {
                    Text("No se pudo mostrar la imagen.")
                }
            }
        )
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
                        text = "Todas las reservas",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
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
                if (viewModel.reservas.isEmpty() && !viewModel.cargando &&
                    viewModel.errorMessage == null) {
                    Text(text = "Aún no hay reservas.", color = Color.Gray)
                }
                viewModel.reservas.forEach { reserva ->
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp))
                    {
                        TarjetaReserva(
                            reserva = reserva,
                            formatoMoneda = formatoMoneda,
                            onSubirComprobanteClick = {},
                            mostrarBotonPago = false,
                            mostrarCliente = true
                        )
                        // Solo se puede revisar un pago cuyo comprobante ya fue enviado
                        if (reserva.estado ==
                            EstadoReserva.COMPROBANTE_ENVIADO) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = {
                                        viewModel.verComprobante(reserva.id) },
                                    shape = RoundedCornerShape(10.dp),
                                    colors =
                                        ButtonDefaults.buttonColors(containerColor = Color(0xFFEEF0FE)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "Ver comprobante",
                                        color = Color(0xFF4C5BF5),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                                Button(
                                    onClick = { viewModel.validar(reserva.id, true, context) },
                                    shape = RoundedCornerShape(10.dp),
                                    colors =
                                        ButtonDefaults.buttonColors(containerColor = Color(0xFF4C5BF5)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "Aprobar",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                                Button(
                                    onClick = { viewModel.validar(reserva.id, false, context) },
                                    shape = RoundedCornerShape(10.dp),
                                    colors =
                                        ButtonDefaults.buttonColors(containerColor = Color(0xFFFDE0E0)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "Rechazar",
                                        color = Color(0xFFC62828),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

