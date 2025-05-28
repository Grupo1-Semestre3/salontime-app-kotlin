package sptech.salonTime.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import sptech.salonTime.entidade.DiaSemana
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
            Funcionamento(1, DiaSemana.MONDAY, LocalTime.of(18, 0), LocalTime.of(21, 0), true),
            Funcionamento(2, DiaSemana.TUESDAY, LocalTime.of(17, 0), LocalTime.of(20, 0), false)
        )
        `when`(repository.findAll()).thenReturn(funcionamentos)

        val result = service.listar()

        assertEquals(funcionamentos, result)
        verify(repository).findAll()
    }

    @Test
    fun `editar deve atualizar o inicio`() {
        val funcionamento = Funcionamento(1, DiaSemana.THURSDAY, LocalTime.of(18, 0), LocalTime.of(20, 0), true)
        val novoInicio = "09:00"
        `when`(repository.findById(1)).thenReturn(Optional.of(funcionamento))
        `when`(repository.save(any(Funcionamento::class.java))).thenReturn(funcionamento.copy(inicio = LocalTime.parse(novoInicio)))

        val result = service.editar(1, "inicio", novoInicio)

        assertEquals(LocalTime.parse(novoInicio), result.inicio)
        verify(repository).save(any(Funcionamento::class.java))
    }

    @Test
    fun `editar deve atualizar o fim`() {
        val funcionamento = Funcionamento(1, DiaSemana.SUNDAY, LocalTime.of(18, 0), LocalTime.of(22, 0), true)
        val novoFim = "19:00"
        `when`(repository.findById(1)).thenReturn(Optional.of(funcionamento))
        `when`(repository.save(any(Funcionamento::class.java))).thenReturn(funcionamento.copy(fim = LocalTime.parse(novoFim)))

        val result = service.editar(1, "fim", novoFim)

        assertEquals(LocalTime.parse(novoFim), result.fim)
        verify(repository).save(any(Funcionamento::class.java))
    }

    @Test
    fun `editar deve atualizar o status aberto`() {
        val funcionamento = Funcionamento(1, DiaSemana.MONDAY, LocalTime.of(18, 0), LocalTime.of(19, 0), true)
        val novoAberto = "false"
        `when`(repository.findById(1)).thenReturn(Optional.of(funcionamento))
        `when`(repository.save(any(Funcionamento::class.java))).thenReturn(funcionamento.copy(aberto = novoAberto.toBoolean()))

        val result = service.editar(1, "aberto", novoAberto)

        result.aberto?.let { assertFalse(it) }
        verify(repository).save(any(Funcionamento::class.java))
    }

    @Test
    fun `editar deve atualizar a capacidade`() {
        val funcionamento = Funcionamento(1, DiaSemana.SUNDAY, LocalTime.of(18, 0), LocalTime.of(19, 0), true)
        val novaCapacidade = "15"
        `when`(repository.findById(1)).thenReturn(Optional.of(funcionamento))
        `when`(repository.save(any(Funcionamento::class.java))).thenReturn(funcionamento.copy(capacidade = novaCapacidade.toInt()))

        val result = service.editar(1, "capacidade", novaCapacidade)

        assertEquals(15, result.capacidade)
        verify(repository).save(any(Funcionamento::class.java))
    }

}