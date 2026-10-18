package com.example.campusnavigator_compose

import android.content.Context
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

object PlanoRepositorio {

    fun cargarAmbientes(context: Context): List<Ambiente> =
        leerFilas(context, "ambientes.csv").map { c ->
            Ambiente(
                topLeft = Offset(c[0].toFloat(), c[1].toFloat()),
                size = Size(c[2].toFloat(), c[3].toFloat()),
                color = Color(android.graphics.Color.parseColor(c[4]))
            )
        }

    fun cargarMarcadores(context: Context): List<Marcador> =
        leerFilas(context, "marcadores.csv").map { c ->
            Marcador(
                offset = Offset(c[2].toFloat(), c[3].toFloat()),
                radio = c[4].toFloat(),
                titulo = c[0],
                descripcion = c[1]
            )
        }

    private fun leerFilas(context: Context, nombreArchivo: String): List<List<String>> =
        runCatching {
            context.assets.open(nombreArchivo).bufferedReader(Charsets.UTF_8).useLines { lineas ->
                lineas
                    .drop(1)
                    .filter { it.isNotBlank() }
                    .map { linea -> linea.split(";").map { it.trim() } }
                    .toList()
            }
        }.getOrDefault(emptyList())
}