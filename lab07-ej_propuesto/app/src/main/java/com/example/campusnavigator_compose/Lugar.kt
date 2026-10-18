package com.example.campusnavigator_compose

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

data class Lugar(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val categoria: String,
    @DrawableRes val imagen: Int,
    val colorFondo: Color
)

val listaLugares = listOf(
    Lugar(
        id = 1,
        nombre = "Biblioteca Central",
        descripcion = "Zona de estudio y préstamo de libros",
        categoria = "Estudio",
        imagen = R.drawable.biblioteca,
        colorFondo = Color(0xFFE3F2FD)
    ),
    Lugar(
        id = 2,
        nombre = "Pabellón A",
        descripcion = "Aulas de clases teóricas y oficinas de docentes",
        categoria = "Aulas",
        imagen = R.drawable.pabellon_a,
        colorFondo = Color(0xFFE8F5E9)
    ),
    Lugar(
        id = 3,
        nombre = "Pabellón B",
        descripcion = "Laboratorios de cómputo y salas de práctica",
        categoria = "Laboratorios",
        imagen = R.drawable.pabellon_b,
        colorFondo = Color(0xFFF3E5F5)
    ),
    Lugar(
        id = 4,
        nombre = "Auditorio",
        descripcion = "Conferencias, ceremonias y eventos académicos",
        categoria = "Eventos",
        imagen = R.drawable.auditorio,
        colorFondo = Color(0xFFFFFDE7)
    ),
    Lugar(
        id = 5,
        nombre = "Comedor Universitario",
        descripcion = "Servicio de alimentación para estudiantes",
        categoria = "Servicios",
        imagen = R.drawable.comedor,
        colorFondo = Color(0xFFFFF3E0)
    )
)