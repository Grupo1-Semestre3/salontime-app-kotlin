package sptech.salonTime.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import sptech.salonTime.entidade.DescCancelamento
import sptech.salonTime.repository.DescCancelamentoRepository

class DescCancelamentoServiceTest {

    private val repository = mock(DescCancelamentoRepository::class.java)
    private val service = DescCancelamentoService(repository)

    @Test
    fun `listar deve retornar todos cancelamentos`() {
         val cancelamentos = listOf(DescCancelamento(1, "Cancelamento 1",1), DescCancelamento(2, "Cancelamento 2",2))
         `when`(repository.findAll()).thenReturn(cancelamentos)
         val result = service.listar()
         assertEquals(cancelamentos, result)
    }

    @Test
    fun `listar por id deve retornar cancelamento`() {
        val cancelamento = DescCancelamento(1, "Cancelamento 1",1)
        `when`(repository.findById(1)).thenReturn(java.util.Optional.of(cancelamento))
        val result = service.listarPorId(1)
        assertEquals(cancelamento, result)
    }

    @Test
    fun `criar deve salvar e retornar cancelamento`() {
        val cancelamento = DescCancelamento(1, "Cancelamento 1",1)
        `when`(repository.save(cancelamento)).thenReturn(cancelamento)
        val result = service.criar(cancelamento)
        assertEquals(cancelamento, result)
    }

    @Test
    fun `atualizar deve atualizar e retornar cancelamento`() {
        val cancelamentoExistente = DescCancelamento(1, "Cancelamento 1",1)
        val cancelamentoAtualizado = DescCancelamento(1, "Cancelamento Atualizado",1)

        `when`(repository.findById(1)).thenReturn(java.util.Optional.of(cancelamentoExistente))
        `when`(repository.save(cancelamentoAtualizado)).thenReturn(cancelamentoAtualizado)

        val result = service.atualizar(1, cancelamentoAtualizado)
        assertEquals(cancelamentoAtualizado, result)
    }

    @Test
    fun `atualizar descricao deve atualizar e retornar cancelamento`() {
        val cancelamentoExistente = DescCancelamento(1, "Cancelamento 1",1)
        val novaDescricao = "Nova Descricao"

        `when`(repository.findById(1)).thenReturn(java.util.Optional.of(cancelamentoExistente))
        `when`(repository.save(cancelamentoExistente)).thenReturn(cancelamentoExistente.apply { descricao = novaDescricao })

        val result = service.atualizarDescricao(1, novaDescricao)
        assertEquals(novaDescricao, result?.descricao)
    }

    @Test
    fun `deletar deve remover cancelamento`() {
        val cancelamento = DescCancelamento(1, "Cancelamento 1",1)
        `when`(repository.findById(1)).thenReturn(java.util.Optional.of(cancelamento))

        service.deletar(1)
    }

}