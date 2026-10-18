package com.example.campusnavigator_compose

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

// Plano virtual: todas las coordenadas están en estas unidades y se escalan al Canvas
const val ANCHO_PLANO = 1000f
const val ALTO_PLANO = 700f

data class Marcador(
    val offset: Offset,
    val radio: Float,
    val titulo: String,
    val descripcion: String
)

data class Ambiente(
    val topLeft: Offset,
    val size: Size,
    val color: Color
)
