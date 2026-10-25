package com.example.campusnavigator_compose

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EdificiosViewModel(application: Application) : AndroidViewModel(application) {

    private val _edificios = MutableStateFlow<List<Edificio>>(emptyList())
    val edificios: StateFlow<List<Edificio>> = _edificios.asStateFlow()

    private val _texto = MutableStateFlow("")
    val texto: StateFlow<String> = _texto.asStateFlow()

    private val _categoria = MutableStateFlow(TODAS)
    val categoria: StateFlow<String> = _categoria.asStateFlow()

    init {
        _edificios.value = EdificiosRepositorio.cargarEdificios(getApplication<Application>())
    }

    // Opciones del menú desplegable, tomadas de los datos del archivo
    fun categorias(): List<String> =
        listOf(TODAS) + _edificios.value.map { it.categoria }.distinct().sorted()

    fun cambiarTexto(nuevo: String) {
        _texto.value = nuevo
    }

    fun cambiarCategoria(nueva: String) {
        _categoria.value = nueva
    }

    fun buscarPorId(id: Int?): Edificio? = _edificios.value.find { it.id == id }
}
