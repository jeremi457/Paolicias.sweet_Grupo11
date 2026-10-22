package com.example.myappnavigation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BarraInferior(
    seleccionado: String,
    onInicioClick: () -> Unit,
    onCatalogoClick: () -> Unit,
    onReservasClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF4C5BF5))
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ItemBarra("Inicio", seleccionado == "Inicio", onInicioClick)
            ItemBarra("Catálogo", seleccionado == "Catálogo", onCatalogoClick)
            ItemBarra("Mis reservas", seleccionado == "Mis reservas", onReservasClick)
        }
    }
}

@Composable
private fun ItemBarra(texto: String, activo: Boolean, onClick: () -> Unit) {
    Text(
        text = texto,
        color = if (activo) Color.White else Color.White.copy(alpha = 0.7f),
        fontWeight = if (activo) FontWeight.Bold else FontWeight.Normal,
        fontSize = 12.sp,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp)
    )
}