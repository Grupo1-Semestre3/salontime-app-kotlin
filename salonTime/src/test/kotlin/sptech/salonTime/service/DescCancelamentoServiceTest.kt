package sptech.salonTime.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import sptech.salonTime.dto.NovaDescricaoCancelamentoDto
import sptech.salonTime.entidade.Agendamento
import sptech.salonTime.entidade.DescCancelamento
import sptech.salonTime.mapper.DescCancelamentoMapper
import sptech.salonTime.repository.DescCancelamentoRepository

class DescCancelamentoServiceTest {

    private val repository = mock(DescCancelamentoRepository::class.java)
    private val service = DescCancelamentoService(repository)

    @Test
    fun `listar deve retornar todos cancelamentos`() {
        val agendamento = mock(Agendamento::class.java)

        val cancelamentos = listOf(
            DescCancelamento(1, "Cancelamento 1", agendamento),
            DescCancelamento(2, "Cancelamento 2", agendamento)
        )

        `when`(repository.findAll()).thenReturn(cancelamentos)

        val expectedDtos = cancelamentos.map { DescCancelamentoMapper.toDTO(it) }

        val result = service.listar()

        assertEquals(expectedDtos, result)
    }


    @Test
    fun `listar por id deve retornar cancelamento`() {
        val agendamento = mock(Agendamento::class.java)

        val cancelamento = DescCancelamento(1, "Cancelamento 1", agendamento)

        `when`(repository.findById(1)).thenReturn(java.util.Optional.of(cancelamento))

        val expectedDto = DescCancelamentoMapper.toDTO(cancelamento)
        val result = service.listarPorId(1)

        assertEquals(expectedDto, result)
        verify(repository).findById(1)
    }


    @Test
    fun `criar deve salvar e retornar cancelamento`() {
        val cancelamento = DescCancelamento(1, "Cancelamento 1",mock(Agendamento::class.java))
        `when`(repository.save(cancelamento)).thenReturn(cancelamento)
        val result = service.criar(cancelamento)
        assertEquals(cancelamento, result)
    }

    @Test
    fun `atualizar deve atualizar e retornar cancelamento`() {
        val agendamento = mock(Agendamento::class.java)
        val cancelamentoExistente = DescCancelamento(1, "Cancelamento 1", agendamento)
        val cancelamentoAtualizado = DescCancelamento(1, "Cancelamento Atualizado", agendamento)

        `when`(repository.findById(1)).thenReturn(java.util.Optional.of(cancelamentoExistente))
        `when`(repository.save(any(DescCancelamento::class.java))).thenReturn(cancelamentoAtualizado)

        val expectedDto = DescCancelamentoMapper.toDTO(cancelamentoAtualizado)
        val result = service.atualizar(1, cancelamentoAtualizado)

        assertEquals(expectedDto, result)
        verify(repository).findById(1)
        verify(repository).save(any(DescCancelamento::class.java))
    }

    @Test
    fun `atualizar descricao deve atualizar e retornar cancelamento`() {
        val agendamento = mock(Agendamento::class.java)

        val cancelamentoExistente = DescCancelamento(1, "Cancelamento 1", agendamento)
        val novaDescricao = NovaDescricaoCancelamentoDto(descricao = "Cancelamento Atualizado")

        val cancelamentoAtualizado = cancelamentoExistente.copy(descricao = novaDescricao.descricao)

        `when`(repository.findById(1)).thenReturn(java.util.Optional.of(cancelamentoExistente))
        `when`(repository.save(any(DescCancelamento::class.java))).thenReturn(cancelamentoAtualizado)

        val expectedDto = DescCancelamentoMapper.toDTO(cancelamentoAtualizado)

        val result = service.atualizarDescricao(1, novaDescricao)

        // Comparar o DTO inteiro (preferível)
        assertEquals(expectedDto, result)

        // OU comparar só a descrição, se o resto não for importante
        // assertEquals(novaDescricao.descricao, result.descricao)

        verify(repository).findById(1)
        verify(repository).save(cancelamentoExistente)
    }


    @Test
    fun `deletar deve remover cancelamento`() {
        val cancelamento = DescCancelamento(1, "Cancelamento 1",mock(Agendamento::class.java))
        `when`(repository.findById(1)).thenReturn(java.util.Optional.of(cancelamento))

        service.deletar(1)
    }

}