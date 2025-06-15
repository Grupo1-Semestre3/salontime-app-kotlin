package sptech.salonTime.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import sptech.salonTime.dto.DescCancelamentoDto
import sptech.salonTime.entidade.Agendamento
import sptech.salonTime.entidade.DescCancelamento
import sptech.salonTime.mapper.DescCancelamentoMapper
import sptech.salonTime.repository.AgendamentoRepository
import sptech.salonTime.repository.DescCancelamentoRepository
import java.util.*

class DescCancelamentoServiceTest {

    private val repositorio = mock(DescCancelamentoRepository::class.java)
    private val agendamentoRepositorio = mock(AgendamentoRepository::class.java)

    private val servico = DescCancelamentoService(repositorio, agendamentoRepositorio)

    @Test
    fun `listar deve retornar lista vazia quando não existem cancelamentos`() {
        `when`(repositorio.findAll()).thenReturn(List<DescCancelamento>(0) { DescCancelamento(0, "", Agendamento()) })

        val resultado = servico.listar()

        assertNotNull(resultado)
        verify(repositorio).findAll()
    }

    @Test
    fun `criar deve salvar e retornar cancelamento quando agendamento existe`() {
        val agendamento = Agendamento().apply { id = 1 }
        val descricaoCancelamento = DescCancelamento(0, "Descrição", agendamento)
        `when`(agendamentoRepositorio.findById(1)).thenReturn(Optional.of(agendamento))
        `when`(repositorio.save(descricaoCancelamento)).thenReturn(descricaoCancelamento)

        val resultado = servico.criar(descricaoCancelamento)

        assertNotNull(resultado)
        assertEquals(descricaoCancelamento.descricao, resultado?.descricao)
        verify(agendamentoRepositorio).findById(1)
        verify(repositorio).save(descricaoCancelamento)
    }

    @Test
    fun `deletar deve não fazer nada quando cancelamento não for encontrado`() {
        val id = 999
        `when`(repositorio.findById(id)).thenReturn(Optional.empty())

        servico.deletar(id)

        verify(repositorio).findById(id)
        verify(repositorio, never()).delete(any())
    }

}
