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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import com.example.myappnavigation.ui.viewmodel.ReservasViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AdminReservasScreen(
    onBackClick: () -> Unit = {},
    viewModel: ReservasViewModel = viewModel()
) {
    val formatoMoneda =
        NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CL"))
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
                    TarjetaReserva(
                        reserva = reserva,
                        formatoMoneda = formatoMoneda,
                        onSubirComprobanteClick = {},
                        mostrarBotonPago = false,
                        mostrarCliente = true
                    )
                }
            }
        }
    }
}