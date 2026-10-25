package com.example.campusnavigator_compose

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import java.text.Normalizer

const val TODAS = "Todas"

data class Edificio(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val descripcion: String,
    val imagen: String // nombre del drawable o "-" si no tiene foto
)

// Ícono de Material Icons según la categoría (ejercicio 2)
fun iconoCategoria(categoria: String): ImageVector = when (categoria) {
    "Estudio" -> Icons.Default.LocalLibrary
    "Académico" -> Icons.Default.School
    "Cultura" -> Icons.Default.AccountBalance
    "Eventos" -> Icons.Default.Event
    "Servicios" -> Icons.Default.Restaurant
    "Investigación" -> Icons.Default.Science
    else -> Icons.Default.Apartment
}

// Color de fondo de la tarjeta según la categoría (reemplaza a colorFondo de Lugar)
fun colorCategoria(categoria: String): Color = when (categoria) {
    "Estudio" -> Color(0xFFE3F2FD)
    "Académico" -> Color(0xFFE8F5E9)
    "Cultura" -> Color(0xFFF3E5F5)
    "Eventos" -> Color(0xFFFFFDE7)
    "Servicios" -> Color(0xFFFFF3E0)
    "Investigación" -> Color(0xFFE0F7FA)
    else -> Color(0xFFECEFF1)
}

// Convierte el nombre escrito en el archivo en un recurso de res/drawable
@DrawableRes
fun imagenEdificio(nombre: String): Int? = when (nombre) {
    "biblioteca" -> R.drawable.biblioteca
    "pabellon_a" -> R.drawable.pabellon_a
    "pabellon_b" -> R.drawable.pabellon_b
    "auditorio" -> R.drawable.auditorio
    "comedor" -> R.drawable.comedor
    else -> null
}

// Quita tildes y pasa a minúsculas: "Ingeniería" y "ingenieria" quedan iguales
fun normalizar(texto: String): String =
    Normalizer.normalize(texto, Normalizer.Form.NFD)
        .replace(Regex("\\p{Mn}+"), "")
        .lowercase()

// Filtro combinado: por nombre (texto) y por categoría (menú desplegable)
fun filtrarEdificios(
    edificios: List<Edificio>,
    texto: String,
    categoria: String
): List<Edificio> {
    val busqueda = normalizar(texto.trim())
    return edificios.filter { edificio ->
        val coincideNombre = normalizar(edificio.nombre).contains(busqueda)
        val coincideCategoria = categoria == TODAS || edificio.categoria == categoria
        coincideNombre && coincideCategoria
    }
}
