package com.example.kotlinpetmanager.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.kotlinpetmanager.model.Paciente
import java.util.UUID

class PacienteViewModel : ViewModel() {

    var listaPacientes = mutableStateOf(listOf<Paciente>())
        private set

    fun agregarPaciente(
        nombre: String,
        especie: String,
        raza: String,
        edadStr: String,
        contactoEmergencia: String,
        tipoConsulta: String,
        esUrgencia: Boolean
    ): Boolean {
        if (nombre.isBlank() || especie.isBlank() || raza.isBlank() ||
            edadStr.isBlank() || contactoEmergencia.isBlank() || tipoConsulta.isBlank()) {
            return false
        }

        val edad = edadStr.toIntOrNull() ?: return false
        val idUnico = "PAC-" + UUID.randomUUID().toString().take(6).uppercase()

        // 🌟 AQUÍ ESTÁ LA SOLUCIÓN:
        // Será urgencia si activaste el switch O si elegiste "Urgencia / Emergencia" en el menú desplegable.
        val esUrgenciaReal = esUrgencia|| (tipoConsulta == "Urgencia / Emergencia")

        // 15 segundos si es urgencia, o los minutos correspondientes convertidos si es normal
        val tiempoEspera = if (esUrgenciaReal) {
            15//
        } else {
            when (tipoConsulta) {
                "Vacunación" -> 5 * 60
                "Control General" -> 8 * 60
                "Desparasitación" -> 10 * 60
                "Estética y Baño" -> 15 * 60
                "Cirugía" -> 1 * 60
                else -> 10 * 60                 // Mínimo 10 minutos
            }
        }

        val nuevoPaciente = Paciente(
            id = idUnico,
            nombre = nombre.trim(),
            especie = especie.trim(),
            raza = raza.trim(),
            edad = edad,
            contactoEmergencia = contactoEmergencia.trim(),
            tipoConsulta = tipoConsulta.trim(),
            esUrgencia = esUrgenciaReal,
            tiempoEsperaMinutos = if (esUrgenciaReal) 15 else (tiempoEspera / 60)
        )

        listaPacientes.value = listOf(nuevoPaciente) + listaPacientes.value
        return true
    }
}