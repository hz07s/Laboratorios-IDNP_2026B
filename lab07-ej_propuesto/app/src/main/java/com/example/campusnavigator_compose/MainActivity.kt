package com.example.campusnavigator_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.campusnavigator_compose.ui.theme.CampusNavigator_ComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CampusNavigator_ComposeTheme {
                MainScreen()
            }
        }
    }
}

sealed class Screen(val route: String, val label: String) {
    object Home : Screen("home", "Home")
    object Edificios : Screen("edificios", "Edificios")
    object Mapa : Screen("mapa", "Mapa")
    object Perfil : Screen("perfil", "Perfil")

    object Detalle : Screen("detalle/{id}", "Detalle") {
        fun crearRuta(id: Int) = "detalle/$id"
    }

    object Plano : Screen("plano", "Plano")
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val seleccionViewModel: SeleccionViewModel = viewModel()
    val planoViewModel: PlanoViewModel = viewModel()

    val items = listOf(Screen.Home, Screen.Edificios, Screen.Mapa, Screen.Perfil)

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { screen ->
                    val icon = when (screen) {
                        Screen.Home -> Icons.Default.Home
                        Screen.Edificios -> Icons.Default.List
                        Screen.Mapa -> Icons.Default.Place
                        Screen.Perfil -> Icons.Default.Person
                        Screen.Detalle -> Icons.Default.Info
                        Screen.Plano -> Icons.Default.Place
                    }
                    val seleccionado = currentRoute == screen.route ||
                            (screen == Screen.Edificios && currentRoute == Screen.Detalle.route) ||
                            (screen == Screen.Mapa && currentRoute == Screen.Plano.route)

                    NavigationBarItem(
                        selected = seleccionado,
                        onClick = { navController.navigate(screen.route) },
                        icon = { Icon(icon, contentDescription = screen.label) },
                        label = { Text(screen.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(viewModel = seleccionViewModel)
            }
            composable(Screen.Edificios.route) {
                EdificiosScreen(
                    viewModel = seleccionViewModel,
                    onVerDetalle = { id ->
                        navController.navigate(Screen.Detalle.crearRuta(id))
                    }
                )
            }
            composable(
                route = Screen.Detalle.route,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id")
                val lugar = listaLugares.find { it.id == id }

                if (lugar != null) {
                    DetalleLugarScreen(
                        nombre = lugar.nombre,
                        descripcion = lugar.descripcion,
                        imagen = painterResource(id = lugar.imagen),
                        colorFondo = lugar.colorFondo,
                        onVerPlano = { navController.navigate(Screen.Plano.route) }
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Lugar no encontrado")
                    }
                }
            }
            composable(Screen.Mapa.route) {
                MapaScreen(
                    onVerPlano = { navController.navigate(Screen.Plano.route) }
                )
            }
            composable(Screen.Plano.route) {
                PlanoScreen(viewModel = planoViewModel)
            }
            composable(Screen.Perfil.route) {
                PerfilScreen()
            }
        }
    }
}

@Composable
fun HomeScreen(viewModel: SeleccionViewModel) {
    val edificioSeleccionado by viewModel.edificio.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Bienvenido a la app de Edificios")
        Text(text = "Último edificio consultado: $edificioSeleccionado")
    }
}

enum class EstiloTarjeta(val etiqueta: String) {
    LISTA("Lista"),
    COMPACTA("Compacta"),
    ETIQUETA("Etiqueta")
}

@Composable
fun EdificiosScreen(
    viewModel: SeleccionViewModel,
    onVerDetalle: (Int) -> Unit
) {
    var estilo by remember { mutableStateOf(EstiloTarjeta.LISTA) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            EstiloTarjeta.entries.forEach { opcion ->
                if (opcion == estilo) {
                    Button(
                        onClick = { estilo = opcion },
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        modifier = Modifier.weight(1f)
                    ) { Text(opcion.etiqueta) }
                } else {
                    OutlinedButton(
                        onClick = { estilo = opcion },
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        modifier = Modifier.weight(1f)
                    ) { Text(opcion.etiqueta) }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(listaLugares, key = { it.id }) { lugar ->
                val alPulsar = Modifier.clickable {
                    viewModel.seleccionar(lugar.nombre)
                    onVerDetalle(lugar.id)
                }
                val imagen = painterResource(id = lugar.imagen)

                when (estilo) {
                    EstiloTarjeta.LISTA -> TarjetaLugar(
                        nombre = lugar.nombre,
                        descripcion = lugar.descripcion,
                        imagen = imagen,
                        colorFondo = lugar.colorFondo,
                        modifier = alPulsar
                    )
                    EstiloTarjeta.COMPACTA -> TarjetaCompacta(
                        nombre = lugar.nombre,
                        imagen = imagen,
                        modifier = alPulsar
                    )
                    EstiloTarjeta.ETIQUETA -> TarjetaEtiqueta(
                        nombre = lugar.nombre,
                        descripcion = lugar.descripcion,
                        categoria = lugar.categoria,
                        imagen = imagen,
                        modifier = alPulsar
                    )
                }
            }
        }
    }
}

@Composable
fun DetalleLugarScreen(
    nombre: String,
    descripcion: String,
    imagen: Painter,
    colorFondo: Color = Color(0xFFE3F2FD),
    onVerPlano: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = colorFondo,
        contentColor = Color(0xFF1C1B1F),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Image(
                painter = imagen,
                contentDescription = nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(220.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = descripcion,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onVerPlano) {
                Text("Ver en el plano")
            }
        }
    }
}

@Composable
fun MapaScreen(onVerPlano: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mapa de ubicaciones",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        TarjetaCompacta(
            nombre = "Plano interior del campus",
            imagen = painterResource(id = R.drawable.biblioteca),
            modifier = Modifier.clickable { onVerPlano() }
        )
    }
}

@Composable
fun PlanoScreen(viewModel: PlanoViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Plano interior del campus",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
        )
        Text(
            text = "Toca un marcador para ver su información",
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        PlanoAmbiente(viewModel = viewModel)
    }
}

@Composable
fun PerfilScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Perfil de usuario (pendiente de integrar)")
    }
}