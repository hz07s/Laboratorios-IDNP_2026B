package com.example.campusnavigator_compose

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SeleccionViewModel : ViewModel() {
    private val _edificio = MutableStateFlow("Ninguno")
    val edificio: StateFlow<String> = _edificio.asStateFlow()

    fun seleccionar(nombre: String) {
        _edificio.value = nombre
    }
}