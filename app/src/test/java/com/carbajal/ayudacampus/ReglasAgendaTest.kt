package com.carbajal.ayudacampus

import com.carbajal.ayudacampus.model.validarHorarioAtencion
import org.junit.Test

class ReglasAgendaTest {
    @Test fun aceptaAtencionFuturaConLugar() {
        validarHorarioAtencion(2000, 3000, "Lab 3", 1000)
    }
    @Test(expected = IllegalArgumentException::class) fun rechazaAtencionPasada() {
        validarHorarioAtencion(900, 3000, "Lab 3", 1000)
    }
    @Test(expected = IllegalArgumentException::class) fun rechazaInicioEnElPresente() {
        validarHorarioAtencion(1000, 3000, "Lab 3", 1000)
    }
    @Test(expected = IllegalArgumentException::class) fun rechazaDuracionCero() {
        validarHorarioAtencion(2000, 2000, "Lab 3", 1000)
    }
    @Test(expected = IllegalArgumentException::class) fun rechazaDuracionNegativa() {
        validarHorarioAtencion(3000, 2000, "Lab 3", 1000)
    }
    @Test(expected = IllegalArgumentException::class) fun rechazaLugarVacio() {
        validarHorarioAtencion(2000, 3000, "   ", 1000)
    }
}
