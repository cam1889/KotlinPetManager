package com.example.kotlinpetmanager

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.kotlinpetmanager.model.Paciente
import com.example.kotlinpetmanager.ui.theme.KotlinpetmanagerTheme
import com.example.kotlinpetmanager.viewmodel.PacienteViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    private val viewModel: PacienteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinpetmanagerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PantallaPrincipal(viewModel = viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal(viewModel: PacienteViewModel) {
    val context = LocalContext.current

    // Estados para los campos del formulario
    var nombre by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var contactoEmergencia by remember { mutableStateOf("") }
    var tipoConsulta by remember { mutableStateOf("") }
    var esUrgencia by remember { mutableStateOf(false) }

    // Estados para los Menús Desplegables (Dropdowns)
    var expandedEspecie by remember { mutableStateOf(false) }
    val listaEspecies = listOf("Perro", "Gato", "Ave", "Conejo", "Roedor", "Reptil", "Otro")

    var expandedTipoConsulta by remember { mutableStateOf(false) }
    val listaTiposConsulta = listOf("Vacunación", "Control General", "Urgencia / Emergencia", "Desparasitación", "Estética y Baño", "Cirugía")

    val pacientes by viewModel.listaPacientes
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("KotlinPet Manager 🐾") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        // Contenedor principal con Scroll vertical
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Formulario organizado verticalmente
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Registrar Nuevo Paciente",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                // 1. Nombre
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del Paciente") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // 2. Especie (Menú Desplegable)
                ExposedDropdownMenuBox(
                    expanded = expandedEspecie,
                    onExpandedChange = { expandedEspecie = !expandedEspecie },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = especie,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Seleccione la Especie") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEspecie) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedEspecie,
                        onDismissRequest = { expandedEspecie = false }
                    ) {
                        listaEspecies.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    especie = opcion
                                    expandedEspecie = false
                                }
                            )
                        }
                    }
                }

                // 3. Raza
                OutlinedTextField(
                    value = raza,
                    onValueChange = { raza = it },
                    label = { Text("Raza") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // 4. Edad
                OutlinedTextField(
                    value = edad,
                    onValueChange = { edad = it },
                    label = { Text("Edad (años)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )

                // 5. Contacto de Emergencia
                OutlinedTextField(
                    value = contactoEmergencia,
                    onValueChange = { contactoEmergencia = it },
                    label = { Text("Contacto de Emergencia") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true
                )

                // 6. Tipo de Consulta (Menú Desplegable)
                ExposedDropdownMenuBox(
                    expanded = expandedTipoConsulta,
                    onExpandedChange = { expandedTipoConsulta = !expandedTipoConsulta },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = tipoConsulta,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Seleccione el Tipo de Consulta") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTipoConsulta) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedTipoConsulta,
                        onDismissRequest = { expandedTipoConsulta = false }
                    ) {
                        listaTiposConsulta.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    tipoConsulta = opcion
                                    expandedTipoConsulta = false
                                }
                            )
                        }
                    }
                }

                // 7. Switch de Urgencia (Booleano independiente)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("¿Requiere atención inmediata (Urgencia)?")
                    Switch(
                        checked = esUrgencia,
                        onCheckedChange = { esUrgencia = it }
                    )
                }

                // Botón de Registro
                Button(
                    onClick = {
                        val exito = viewModel.agregarPaciente(
                            nombre = nombre,
                            especie = especie,
                            raza = raza,
                            edadStr = edad,
                            contactoEmergencia = contactoEmergencia,
                            tipoConsulta = tipoConsulta,
                            esUrgencia = esUrgencia
                        )
                        if (exito) {
                            Toast.makeText(context, "Paciente registrado con éxito", Toast.LENGTH_SHORT).show()
                            // Limpiar formulario
                            nombre = ""
                            especie = ""
                            raza = ""
                            edad = ""
                            contactoEmergencia = ""
                            tipoConsulta = ""
                            esUrgencia = false
                        } else {
                            Toast.makeText(context, "Por favor complete todos los campos correctamente", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Registrar Paciente")
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            // Lista Dinámica de Pacientes Registrados
            Text(
                text = "Pacientes Registrados (${pacientes.size})",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            // Renderizado de las tarjetas
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                pacientes.forEach { paciente ->
                    ItemPacienteCard(paciente = paciente)
                }
            }
        }
    }
}

@Composable
fun ItemPacienteCard(paciente: Paciente) {
    // Si es urgencia real, toma 15 segundos; si no, toma los minutos del paciente convertidos a segundos
    val segundosIniciales = if (paciente.esUrgencia) {
        15
    } else {
        paciente.tiempoEsperaMinutos * 60
    }

    var segundosRestantes by remember { mutableStateOf(segundosIniciales) }
    var puedeIngresar by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = paciente.id) {
        while (segundosRestantes > 0) {
            delay(1000L)
            segundosRestantes--
        }
        puedeIngresar = true
    }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (puedeIngresar)
                MaterialTheme.colorScheme.tertiaryContainer
            else if (paciente.esUrgencia) // Solo se pinta de rojo si realmente es urgencia
                MaterialTheme.colorScheme.errorContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "ID: ${paciente.id}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary
                )

                if (puedeIngresar) {
                    Text(
                        text = "🟢 ¡LISTO!",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    val minutos = segundosRestantes / 60
                    val segundos = segundosRestantes % 60
                    Text(
                        text = String.format("⏱️ Espera: %02d:%02d", minutos, segundos),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            Text(text = "Nombre: ${paciente.nombre}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Especie: ${paciente.especie} | Raza: ${paciente.raza}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Edad: ${paciente.edad} años", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Tipo de Consulta: ${paciente.tipoConsulta}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Contacto: ${paciente.contactoEmergencia}", style = MaterialTheme.typography.bodySmall)

            if (puedeIngresar) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "✨ ¡El tiempo de espera ha terminado, ya puede ingresar a la consulta!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            } else if (paciente.esUrgencia) {
                Text(
                    text = "🚨 ¡ATENCIÓN INMEDIATA / URGENCIA!",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}