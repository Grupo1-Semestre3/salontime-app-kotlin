package sptech.salonTime.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import sptech.salonTime.dto.EmailDto
import sptech.salonTime.dto.SenhaDto
import sptech.salonTime.entidade.Pagamento
import sptech.salonTime.entidade.Usuario
import sptech.salonTime.exception.UsuarioNaoEncontradoException
import sptech.salonTime.repository.PagamentoRepository
import sptech.salonTime.repository.UsuarioRepository
import java.util.*

class PagamentoServiceTest {
    private val repository = mock(PagamentoRepository::class.java)
    private val service = PagamentoService(repository)

    @Test
    fun `listar should return all payments`() {
        val pagamentos = listOf(Pagamento(1, "CREDITO",100.0), Pagamento(2, "CREDITO",200.0))
        `when`(repository.findAll()).thenReturn(pagamentos)

        val result = service.listar()

        assertEquals(pagamentos, result)
        verify(repository).findAll()
    }

    @Test
    fun `salvar should save and return the payment`() {
        val pagamento = Pagamento(1, "CREDITO",100.0)
        `when`(repository.save(pagamento)).thenReturn(pagamento)

        val result = service.criar(pagamento)

        assertEquals(pagamento, result)
        verify(repository).save(pagamento)
    }

    @Test
    fun `listarPorId should return payment by id`() {
        val pagamento = Pagamento(1, "PIX",100.0)
        `when`(repository.findById(1)).thenReturn(Optional.of(pagamento))

        val result = service.listarPorId(1)

        assertEquals(pagamento, result)
        verify(repository).findById(1)
    }

    @Test
    fun `excluir should delete payment by id`() {
        `when`(repository.existsById(1)).thenReturn(true)

        repository.deleteById(1)

        verify(repository).deleteById(1)
    }

    @Test
    fun `atualizar should update and return the payment`() {
        val pagamento = Pagamento(1, "DEBITO",100.0)
        val pagamentoAtualizado = Pagamento(1, "PIX",150.0)
        `when`(repository.findById(1)).thenReturn(Optional.of(pagamento))
        `when`(repository.save(pagamentoAtualizado)).thenReturn(pagamentoAtualizado)

        val result = service.atualizar(1, pagamentoAtualizado)

        assertEquals(pagamentoAtualizado, result)
        verify(repository).save(pagamentoAtualizado)
    }
}