package com.example.campusnavigator_compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Campo de búsqueda por nombre + menú desplegable de categoría (ejercicio 3)
@Composable
fun BuscadorEdificios(
    texto: String,
    onTextoChange: (String) -> Unit,
    categoria: String,
    categorias: List<String>,
    onCategoriaChange: (String) -> Unit
) {
    var menuAbierto by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = texto,
            onValueChange = onTextoChange,
            label = { Text("Buscar edificio") },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (texto.isNotEmpty()) {
                    IconButton(onClick = { onTextoChange("") }) {
                        Icon(Icons.Default.Close, contentDescription = "Borrar texto")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box {
            OutlinedButton(
                onClick = { menuAbierto = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.FilterList, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Categoría: $categoria")
                Spacer(modifier = Modifier.weight(1f))
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
            DropdownMenu(
                expanded = menuAbierto,
                onDismissRequest = { menuAbierto = false }
            ) {
                categorias.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            onCategoriaChange(opcion)
                            menuAbierto = false
                        }
                    )
                }
            }
        }
    }
}

// Fila del ejercicio resuelto con un ícono de Material Icons a la izquierda (ejercicio 2)
@Composable
fun FilaEdificio(
    edificio: Edificio,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CardDefaults.shape)
            .then(modifier)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = colorCategoria(edificio.categoria)
            ) {
                Icon(
                    imageVector = iconoCategoria(edificio.categoria),
                    contentDescription = edificio.categoria,
                    tint = Color(0xFF37474F),
                    modifier = Modifier
                        .padding(10.dp)
                        .size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = edificio.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = edificio.categoria,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = edificio.descripcion,
                    fontSize = 14.sp
                )
            }
        }
    }
}

// Imagen para las tarjetas del Lab 06: foto si existe en res/drawable;
// si no, el ícono de la categoría (imagen circular) o un fondo de color (imagen de fondo)
@Composable
fun pintorEdificio(edificio: Edificio, comoFondo: Boolean): Painter {
    val recurso = imagenEdificio(edificio.imagen)
    return when {
        recurso != null -> painterResource(id = recurso)
        comoFondo -> ColorPainter(Color(0xFF546E7A))
        else -> rememberVectorPainter(iconoCategoria(edificio.categoria))
    }
}

@Composable
fun MensajeSinResultados() {
    Text(
        text = "No se encontraron edificios con ese criterio",
        color = Color.Gray,
        fontSize = 14.sp,
        modifier = Modifier.padding(top = 24.dp)
    )
}
