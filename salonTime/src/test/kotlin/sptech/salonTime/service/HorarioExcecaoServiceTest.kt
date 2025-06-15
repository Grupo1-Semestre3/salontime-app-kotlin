package sptech.salonTime.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import sptech.salonTime.entidade.HorarioExcecao
import sptech.salonTime.repository.HorarioExcecaoRepository
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class HorarioExcecaoServiceTest {

    private val repository = mock(HorarioExcecaoRepository::class.java)
    private val service = HorarioExcecaoService(repository)

    @Test
    fun `listar deve retornar todos os horarios de excecao`() {
        val horariosExcecao = listOf(
            HorarioExcecao(1, LocalDate.now(), LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(18, 0), true, 10, null),
            HorarioExcecao(2, LocalDate.now(), LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(17, 0), false, 5, null)
        )

        `when`(repository.findAll()).thenReturn(horariosExcecao)

        val result = service.listar()

        assertEquals(horariosExcecao, result)
        verify(repository).findAll()
    }

    @Test
    fun `editarAberto deve atualizar o campo aberto de um horario de excecao`() {
        val horarioExcecao = HorarioExcecao(1, LocalDate.now(), LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(18, 0), false, 10, null)

        `when`(repository.findById(1)).thenReturn(Optional.of(horarioExcecao))
        `when`(repository.save(horarioExcecao)).thenReturn(horarioExcecao.apply { aberto = true })

        val result = service.editarAberto(1, true)

        result.aberto?.let { assertTrue(it) }
        verify(repository).save(horarioExcecao)
    }

    @Test
    fun `editar deve atualizar um horario de excecao existente`() {
        val horarioExcecaoExistente = HorarioExcecao(1, LocalDate.of(2023, 10, 1), LocalDate.of(2023, 10, 2), LocalTime.of(8, 0), LocalTime.of(18, 0), false, 10, null)
        val horarioExcecaoAtualizado = HorarioExcecao(1, LocalDate.of(2023, 10, 3), LocalDate.of(2023, 10, 4), LocalTime.of(9, 0), LocalTime.of(17, 0), true, 15, null)

        `when`(repository.findById(1)).thenReturn(Optional.of(horarioExcecaoExistente))
        `when`(repository.save(horarioExcecaoExistente)).thenReturn(horarioExcecaoAtualizado)

        val result = service.editar(1, horarioExcecaoAtualizado)

        assertEquals(horarioExcecaoAtualizado.dataInicio, result.dataInicio)
        assertEquals(horarioExcecaoAtualizado.dataFim, result.dataFim)
        assertEquals(horarioExcecaoAtualizado.inicio, result.inicio)
        assertEquals(horarioExcecaoAtualizado.fim, result.fim)
        assertEquals(horarioExcecaoAtualizado.capacidade, result.capacidade)
        assertEquals(horarioExcecaoAtualizado.aberto, result.aberto)
        verify(repository).save(horarioExcecaoExistente)
    }

    @Test
    fun `salvar deve salvar um novo horario de excecao`() {
        val horarioExcecao = HorarioExcecao(1, LocalDate.of(2023, 10, 1), LocalDate.of(2023, 10, 2), LocalTime.of(8, 0), LocalTime.of(18, 0), true, 10, null)

        `when`(repository.save(horarioExcecao)).thenReturn(horarioExcecao)

        val result = service.salvar(horarioExcecao)

        assertEquals(horarioExcecao, result)
        verify(repository).save(horarioExcecao)
    }
}