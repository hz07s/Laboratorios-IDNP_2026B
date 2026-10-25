package com.example.campusnavigator_compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlanoAmbiente(viewModel: PlanoViewModel) {
    val ambientes by viewModel.ambientes.collectAsState()
    val marcadores by viewModel.marcadores.collectAsState()
    val marcadorSeleccionado by viewModel.marcadorSeleccionado.collectAsState()

    val textMeasurer = rememberTextMeasurer()
    val estiloEtiqueta = TextStyle(
        color = Color(0xFF1C1B1F),
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .aspectRatio(ANCHO_PLANO / ALTO_PLANO)
            .pointerInput(Unit) {
                detectTapGestures { toque ->
                    val escala = size.width / ANCHO_PLANO
                    viewModel.tocarPlano(toque / escala)
                }
            }
    ) {
        val escala = size.width / ANCHO_PLANO

        ambientes.forEach { ambiente ->
            drawRect(
                color = ambiente.color,
                topLeft = ambiente.topLeft * escala,
                size = ambiente.size * escala
            )
        }

        drawRect(
            color = Color(0xFF424242),
            topLeft = Offset(20f, 20f) * escala,
            size = Size(960f, 660f) * escala,
            style = Stroke(width = 8f)
        )

        val colorPared = Color(0xFF424242)
        drawLine(
            color = colorPared,
            start = Offset(240f, 20f) * escala,
            end = Offset(240f, 680f) * escala,
            strokeWidth = 6f
        )
        drawLine(
            color = colorPared,
            start = Offset(660f, 20f) * escala,
            end = Offset(660f, 680f) * escala,
            strokeWidth = 6f
        )
        drawLine(
            color = colorPared,
            start = Offset(240f, 300f) * escala,
            end = Offset(660f, 300f) * escala,
            strokeWidth = 6f
        )
        drawLine(
            color = colorPared,
            start = Offset(660f, 420f) * escala,
            end = Offset(980f, 420f) * escala,
            strokeWidth = 6f
        )

        marcadores.forEach { marcador ->
            val centro = marcador.offset * escala
            drawCircle(
                color = Color.Red,
                radius = marcador.radio * escala,
                center = centro
            )
            val etiqueta = textMeasurer.measure(
                text = marcador.titulo,
                style = estiloEtiqueta,
                constraints = Constraints(maxWidth = (240f * escala).toInt())
            )
            drawText(
                textLayoutResult = etiqueta,
                topLeft = Offset(
                    centro.x - etiqueta.size.width / 2f,
                    centro.y + (marcador.radio + 6f) * escala
                )
            )
        }
    }

    marcadorSeleccionado?.let { marcador ->
        AlertDialog(
            onDismissRequest = { viewModel.cerrarDialogo() },
            title = { Text(marcador.titulo) },
            text = { Text(marcador.descripcion) },
            confirmButton = {
                TextButton(onClick = { viewModel.cerrarDialogo() }) {
                    Text("Cerrar")
                }
            }
        )
    }
}