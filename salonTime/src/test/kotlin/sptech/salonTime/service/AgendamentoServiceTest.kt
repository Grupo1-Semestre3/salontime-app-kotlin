package sptech.salonTime.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import sptech.salonTime.dto.AgendamentoDto
import sptech.salonTime.dto.CadastroAgendamentoDto
import sptech.salonTime.entidade.*
import sptech.salonTime.exception.*
import sptech.salonTime.mapper.AgendamentoMapper
import sptech.salonTime.repository.*
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class AgendamentoServiceTest {

    private val repository = mock(AgendamentoRepository::class.java)
    private val pagamentoRepository = mock(PagamentoRepository::class.java)
    private val usuarioRepository = mock(UsuarioRepository::class.java)
    private val statusAgendamentoRepository = mock(StatusAgendamentoRepository::class.java)
    private val servicoRepository = mock(ServicoRepository::class.java)
    private val service = AgendamentoService(
        repository,
        pagamentoRepository,
        usuarioRepository,
        statusAgendamentoRepository,
        servicoRepository
    )

    @Test
    fun `listar deve retornar todos os agendamentos`() {
        val agendamentos = listOf(
            Agendamento(1, null, null, null, null, null, LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(11, 0), 100.0)
        )
        `when`(repository.listarTudo()).thenReturn(agendamentos)

        val result = service.listar()

        assertEquals(agendamentos.map { AgendamentoMapper.toDto(it) }, result)
        verify(repository).listarTudo()
    }

    @Test
    fun `listarPorId deve retornar o agendamento correto`() {
        val agendamento = Agendamento(1, null, null, null, null, null, LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(11, 0), 100.0)
        `when`(repository.findById(1)).thenReturn(Optional.of(agendamento))

        val result = service.listarPorId(1)

        assertEquals(AgendamentoMapper.toDto(agendamento), result)
        verify(repository).findById(1)
    }

    @Test
    fun `cadastrar deve salvar e retornar o agendamento`() {
        val cadastroDto = CadastroAgendamentoDto(1, 1, 1, 1, 1, LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(11, 0), 100.0)
        val usuario = Usuario()
        val servico = Servico()
        val funcionario = Usuario()
        val status = StatusAgendamento(1, "Status")
        val pagamento = Pagamento(1, "Pagamento")
        val agendamento = Agendamento(1, servico, usuario, funcionario, status, pagamento, cadastroDto.data, cadastroDto.inicio, cadastroDto.fim, cadastroDto.preco)

        `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario))
        `when`(servicoRepository.findById(1)).thenReturn(Optional.of(servico))
        `when`(usuarioRepository.findById(2)).thenReturn(Optional.of(funcionario))
        `when`(statusAgendamentoRepository.findById(1)).thenReturn(Optional.of(status))
        `when`(pagamentoRepository.findById(1)).thenReturn(Optional.of(pagamento))
        `when`(repository.save(any(Agendamento::class.java))).thenReturn(agendamento)

        val result = service.cadastrar(cadastroDto)

        assertEquals(AgendamentoMapper.toDto(agendamento), result)
        verify(repository).save(any(Agendamento::class.java))
    }

    @Test
    fun `atualizarAtributo deve atualizar o atributo correto`() {
        val agendamento = Agendamento(1, null, null, null, null, null, LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(11, 0), 100.0)
        val novoValor = "2023-12-25"
        `when`(repository.findById(1)).thenReturn(Optional.of(agendamento))
        `when`(repository.save(any(Agendamento::class.java))).thenReturn(agendamento.copy(data = LocalDate.parse(novoValor)))

        val result = service.atualizarAtributo(1, "data", novoValor)

        assertEquals(LocalDate.parse(novoValor), result.data)
        verify(repository).save(any(Agendamento::class.java))
    }

    @Test
    fun `atualizarStatus deve atualizar o status corretamente`() {
        val agendamento = Agendamento(1, null, null, null, null, null, LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(11, 0), 100.0)
        val status = StatusAgendamento(2, "Novo Status")
        `when`(repository.findById(1)).thenReturn(Optional.of(agendamento))
        `when`(statusAgendamentoRepository.findById(2)).thenReturn(Optional.of(status))
        `when`(repository.save(any(Agendamento::class.java))).thenReturn(agendamento.copy(statusAgendamento = status))

        val result = service.atualizarStatus(1, 2)

        assertEquals(status.id, result?.statusAgendamento?.id)
        verify(repository).save(any(Agendamento::class.java))
    }

    @Test
    fun `atualizarValor deve atualizar o valor corretamente`() {
        val agendamento = Agendamento(1, null, null, null, null, null, LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(11, 0), 100.0)
        val novoValor = 150.0
        `when`(repository.findById(1)).thenReturn(Optional.of(agendamento))
        `when`(repository.save(any(Agendamento::class.java))).thenReturn(agendamento.copy(preco = novoValor))

        val result = service.atualizarValor(1, novoValor)

        assertEquals(novoValor, result?.preco)
        verify(repository).save(any(Agendamento::class.java))
    }
}