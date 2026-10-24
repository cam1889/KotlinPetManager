package com.example.kotlinpetmanager.model

data class Paciente(
    val id: String,
    val nombre: String,
    val especie: String,
    val raza: String,
    val edad: Int,
    val contactoEmergencia: String,
    val tipoConsulta: String,
    val esUrgencia: Boolean,
    val tiempoEsperaMinutos: Int
)