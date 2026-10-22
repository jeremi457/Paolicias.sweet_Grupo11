package com.example.myappnavigation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IconoError() {
    Box(
        modifier = Modifier
            .size(24.dp)
            .background(Color(0xFFD32F2F), shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text("!", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
fun MensajeError(mensaje: String?) {
    AnimatedVisibility(visible = mensaje != null) {
        Text(
            text = mensaje ?: "",
            color = Color(0xFFD32F2F),
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}