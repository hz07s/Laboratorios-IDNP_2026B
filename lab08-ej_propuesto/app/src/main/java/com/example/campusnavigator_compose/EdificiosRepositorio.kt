package com.example.campusnavigator_compose

import android.content.Context

object EdificiosRepositorio {

    private const val ARCHIVO = "edificios.txt"

    // Formato de cada línea: id;nombre;categoria;descripcion;imagen
    fun cargarEdificios(context: Context): List<Edificio> =
        runCatching {
            context.assets.open(ARCHIVO).bufferedReader(Charsets.UTF_8).useLines { lineas ->
                lineas
                    .drop(1)                      // cabecera
                    .filter { it.isNotBlank() }
                    .mapNotNull { linea -> convertirLinea(linea) }
                    .toList()
            }
        }.getOrDefault(emptyList())

    // Devuelve null si la línea está incompleta o el id no es un número
    private fun convertirLinea(linea: String): Edificio? {
        val c = linea.split(";").map { it.trim() }
        if (c.size < 5) return null
        val id = c[0].toIntOrNull() ?: return null
        return Edificio(
            id = id,
            nombre = c[1],
            categoria = c[2],
            descripcion = c[3],
            imagen = c[4]
        )
    }
}
