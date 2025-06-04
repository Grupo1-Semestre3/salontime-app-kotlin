package sptech.salonTime.controller

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import sptech.salonTime.entidade.HorarioExcecao
import sptech.salonTime.service.HorarioExcecaoService
import java.time.LocalDate
import java.time.LocalTime

class HorarioExcecaoControllerTest {

    private val service = mock(HorarioExcecaoService::class.java)
    private val controller = HorarioExcecaoController(service)

    @Test
    fun listar() {
        val horarioExcecao = HorarioExcecao(1, LocalDate.now(), LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(18, 0), true, 10, null)
        `when`(service.listar()).thenReturn(listOf(horarioExcecao))

        val response = controller.listar()

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(1, response.body?.size)
        verify(service).listar()
    }

    @Test
    fun inserir() {
        val horarioExcecao = HorarioExcecao(1, LocalDate.now(), LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(18, 0), true, 10, null)
        `when`(service.salvar(horarioExcecao)).thenReturn(horarioExcecao)

        val response = controller.inserir(horarioExcecao)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(horarioExcecao, response.body)
        verify(service).salvar(horarioExcecao)
    }

    @Test
    fun editar() {
        val horarioExcecao = HorarioExcecao(1, LocalDate.now(), LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(18, 0), true, 10, null)
        `when`(service.editar(1, horarioExcecao)).thenReturn(horarioExcecao)

        val response = controller.editar(1, horarioExcecao)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(horarioExcecao, response.body)
        verify(service).editar(1, horarioExcecao)
    }

    @Test
    fun editarAberto() {
        val horarioExcecao = HorarioExcecao(1, LocalDate.now(), LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(18, 0), true, 10, null)
        `when`(service.editarAberto(1, false)).thenReturn(horarioExcecao.apply { aberto = false })

        val response = controller.editarAberto(1, false)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertFalse(response.body?.aberto ?: true)
        verify(service).editarAberto(1, false)
    }

}