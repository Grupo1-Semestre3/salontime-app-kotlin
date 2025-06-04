package sptech.salonTime.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import sptech.salonTime.entidade.HorarioExcecao
import sptech.salonTime.entidade.Usuario
import sptech.salonTime.entidade.TipoUsuario
import sptech.salonTime.repository.HorarioExcecaoRepository
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class HorarioExcecaoServiceTest {

    private val repository = mock(HorarioExcecaoRepository::class.java)
    private val service = HorarioExcecaoService(repository)

    @Test
    fun `editar deve atualizar um horario de excecao existente`() {
        val tipoUsuario = TipoUsuario(1, "Funcionario")
        val funcionario = Usuario(1, tipoUsuario).apply { this.tipoUsuario = tipoUsuario }

        val horarioExcecaoExistente = HorarioExcecao(
            id = 1,
            dataInicio = LocalDate.of(2023, 10, 1),
            dataFim = LocalDate.of(2023, 10, 2),
            inicio = LocalTime.of(8, 0),
            fim = LocalTime.of(18, 0),
            aberto = false,
            capacidade = 10,
            funcionario = funcionario
        )

        val horarioExcecaoAtualizado = HorarioExcecao(
            id = 1,
            dataInicio = LocalDate.of(2023, 10, 3),
            dataFim = LocalDate.of(2023, 10, 4),
            inicio = LocalTime.of(9, 0),
            fim = LocalTime.of(17, 0),
            aberto = true,
            capacidade = 15,
            funcionario = funcionario
        )

        `when`(repository.findById(1)).thenReturn(Optional.of(horarioExcecaoExistente))
        `when`(repository.save(horarioExcecaoExistente)).thenReturn(horarioExcecaoAtualizado)

        val result = service.editar(1, horarioExcecaoAtualizado)

        assertEquals(horarioExcecaoAtualizado.dataInicio, result.dataInicio)
        assertEquals(horarioExcecaoAtualizado.dataFim, result.dataFim)
        assertEquals(horarioExcecaoAtualizado.inicio, result.inicio)
        assertEquals(horarioExcecaoAtualizado.fim, result.fim)
        assertEquals(horarioExcecaoAtualizado.capacidade, result.capacidade)
        assertEquals(horarioExcecaoAtualizado.funcionario, result.funcionario)
        verify(repository).save(horarioExcecaoExistente)
    }

    @Test
    fun `salvar deve salvar um novo horario de excecao`() {
        val tipoUsuario = TipoUsuario(1, "Funcionario")
        val funcionario = Usuario(1, tipoUsuario).apply { this.tipoUsuario = tipoUsuario }

        val horarioExcecao = HorarioExcecao(
            id = 1,
            dataInicio = LocalDate.of(2023, 10, 1),
            dataFim = LocalDate.of(2023, 10, 2),
            inicio = LocalTime.of(8, 0),
            fim = LocalTime.of(18, 0),
            aberto = true,
            capacidade = 10,
            funcionario = funcionario
        )

        `when`(repository.save(horarioExcecao)).thenReturn(horarioExcecao)

        val result = service.salvar(horarioExcecao)

        assertEquals(horarioExcecao, result)
        verify(repository).save(horarioExcecao)
    }
}