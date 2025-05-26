package sptech.salonTime.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import sptech.salonTime.entidade.Funcionamento
import sptech.salonTime.repository.FuncionamentoRepository
import java.time.LocalTime
import java.util.*

class FuncionamentoServiceTest {

    private val repository = mock(FuncionamentoRepository::class.java)
    private val service = FuncionamentoService(repository)

    @Test
    fun `listar deve retornar todos os funcionamentos`() {
        val funcionamentos = listOf(
            Funcionamento(1, LocalTime.of(8, 0), LocalTime.of(18, 0), true, 10),
            Funcionamento(2, LocalTime.of(9, 0), LocalTime.of(17, 0), false, 5)
        )
        `when`(repository.findAll()).thenReturn(funcionamentos)

        val result = service.listar()

        assertEquals(funcionamentos, result)
        verify(repository).findAll()
    }

    @Test
    fun `editar deve atualizar o inicio`() {
        val funcionamento = Funcionamento(1, LocalTime.of(8, 0), LocalTime.of(18, 0), true, 10)
        val novoInicio = "09:00"
        `when`(repository.findById(1)).thenReturn(Optional.of(funcionamento))
        `when`(repository.save(any(Funcionamento::class.java))).thenReturn(funcionamento.copy(inicio = LocalTime.parse(novoInicio)))

        val result = service.editar(1, "inicio", novoInicio)

        assertEquals(LocalTime.parse(novoInicio), result.inicio)
        verify(repository).save(any(Funcionamento::class.java))
    }

    @Test
    fun `editar deve atualizar o fim`() {
        val funcionamento = Funcionamento(1, LocalTime.of(8, 0), LocalTime.of(18, 0), true, 10)
        val novoFim = "19:00"
        `when`(repository.findById(1)).thenReturn(Optional.of(funcionamento))
        `when`(repository.save(any(Funcionamento::class.java))).thenReturn(funcionamento.copy(fim = LocalTime.parse(novoFim)))

        val result = service.editar(1, "fim", novoFim)

        assertEquals(LocalTime.parse(novoFim), result.fim)
        verify(repository).save(any(Funcionamento::class.java))
    }

    @Test
    fun `editar deve atualizar o status aberto`() {
        val funcionamento = Funcionamento(1, LocalTime.of(8, 0), LocalTime.of(18, 0), true, 10)
        val novoAberto = "false"
        `when`(repository.findById(1)).thenReturn(Optional.of(funcionamento))
        `when`(repository.save(any(Funcionamento::class.java))).thenReturn(funcionamento.copy(aberto = novoAberto.toBoolean()))

        val result = service.editar(1, "aberto", novoAberto)

        assertFalse(result.aberto)
        verify(repository).save(any(Funcionamento::class.java))
    }

    @Test
    fun `editar deve atualizar a capacidade`() {
        val funcionamento = Funcionamento(1, LocalTime.of(8, 0), LocalTime.of(18, 0), true, 10)
        val novaCapacidade = "15"
        `when`(repository.findById(1)).thenReturn(Optional.of(funcionamento))
        `when`(repository.save(any(Funcionamento::class.java))).thenReturn(funcionamento.copy(capacidade = novaCapacidade.toInt()))

        val result = service.editar(1, "capacidade", novaCapacidade)

        assertEquals(15, result.capacidade)
        verify(repository).save(any(Funcionamento::class.java))
    }

    @Test
    fun `editar deve lançar excecao para atributo invalido`() {
        val funcionamento = Funcionamento(1, LocalTime.of(8, 0), LocalTime.of(18, 0), true, 10)
        `when`(repository.findById(1)).thenReturn(Optional.of(funcionamento))

        val exception = assertThrows(IllegalArgumentException::class.java) {
            service.editar(1, "invalido", "valor")
        }

        assertEquals("Atributo inválido: invalido", exception.message)
    }

    @Test
    fun `editar deve lançar excecao quando funcionamento nao encontrado`() {
        `when`(repository.findById(1)).thenReturn(Optional.empty())

        val exception = assertThrows(IllegalArgumentException::class.java) {
            service.editar(1, "inicio", "09:00")
        }

        assertEquals("Funcionamento não encontrado", exception.message)
    }
}