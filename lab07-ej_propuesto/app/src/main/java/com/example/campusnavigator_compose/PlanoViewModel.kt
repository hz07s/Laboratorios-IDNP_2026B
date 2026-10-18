package com.example.campusnavigator_compose

import android.app.Application
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlanoViewModel(application: Application) : AndroidViewModel(application) {

    private val _ambientes = MutableStateFlow<List<Ambiente>>(emptyList())
    val ambientes: StateFlow<List<Ambiente>> = _ambientes.asStateFlow()

    private val _marcadores = MutableStateFlow<List<Marcador>>(emptyList())
    val marcadores: StateFlow<List<Marcador>> = _marcadores.asStateFlow()

    private val _marcadorSeleccionado = MutableStateFlow<Marcador?>(null)
    val marcadorSeleccionado: StateFlow<Marcador?> = _marcadorSeleccionado.asStateFlow()

    init {
        val contexto = getApplication<Application>()
        _ambientes.value = PlanoRepositorio.cargarAmbientes(contexto)
        _marcadores.value = PlanoRepositorio.cargarMarcadores(contexto)
    }

    // posicion está en unidades del plano virtual (ya dividida entre la escala)
    fun tocarPlano(posicion: Offset) {
        val tocado = _marcadores.value.firstOrNull { marcador ->
            (posicion - marcador.offset).getDistance() <= marcador.radio
        }
        if (tocado != null) {
            _marcadorSeleccionado.value = tocado
        }
    }

    fun cerrarDialogo() {
        _marcadorSeleccionado.value = null
    }
}