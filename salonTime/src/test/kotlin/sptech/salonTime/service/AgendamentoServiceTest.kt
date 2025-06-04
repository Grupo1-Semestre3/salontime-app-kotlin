package sptech.salonTime.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import sptech.salonTime.dto.AgendamentoDto
import sptech.salonTime.dto.CadastroAgendamentoDto
import sptech.salonTime.dto.UsuarioPublicoDto
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
    private val cupomRepository = mock(CupomRepository::class.java)
    private val service = AgendamentoService(
        repository,
        pagamentoRepository,
        usuarioRepository,
        statusAgendamentoRepository,
        servicoRepository,
        cupomRepository
    )

    @Test
    fun `listar deve retornar todos os agendamentos`() {
        val agendamentos = listOf(
            Agendamento(1, null, null, null, null, null)
        )
        `when`(repository.listarTudo()).thenReturn(agendamentos)

        val result = service.listar()

        assertEquals(agendamentos.map { AgendamentoMapper.toDto(it) }, result)
        verify(repository).listarTudo()
    }

    @Test
    fun `listarPorId deve retornar o agendamento correto`() {
        val agendamento = Agendamento(1, null, null, null, null, null)
        `when`(repository.findById(1)).thenReturn(Optional.of(agendamento))

        val result = service.listarPorId(1)

        assertEquals(AgendamentoMapper.toDto(agendamento), result)
    }

    @Test
    fun `cadastrar deve salvar e retornar o agendamento`() {
        val cadastroDto = CadastroAgendamentoDto(
            usuario = 1,
            servico = 1,
            funcionario = 2,
            statusAgendamento = 1,
            pagamento = 1,
            data = LocalDate.now(),
            inicio = LocalTime.of(10, 0),
            fim = LocalTime.of(11, 0),
            preco = 100.0
        )

        val usuario = Usuario()
        val servico = Servico()
        val funcionario = Usuario().apply {
            tipoUsuario = TipoUsuario(3, "FUNCIONARIO")
        }
        val status = StatusAgendamento(1, "AGENDADO")
        val pagamento = Pagamento(1, "Pagamento")
        val agendamento = Agendamento(
            id = 1, servico = servico, usuario = usuario, funcionario = funcionario,
            statusAgendamento = status, pagamento = pagamento, data = cadastroDto.data,
            inicio = cadastroDto.inicio, fim = cadastroDto.fim, preco = cadastroDto.preco
        )

        `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario))
        `when`(usuarioRepository.findById(2)).thenReturn(Optional.of(funcionario))
        `when`(servicoRepository.findById(1)).thenReturn(Optional.of(servico))
        `when`(statusAgendamentoRepository.findById(1)).thenReturn(Optional.of(status))
        `when`(pagamentoRepository.findById(1)).thenReturn(Optional.of(pagamento))
        `when`(repository.save(any(Agendamento::class.java))).thenReturn(agendamento)
        `when`(repository.existeConflitoDeAgendamento(any(), any(), any())).thenReturn(0)

        val result = service.cadastrar(cadastroDto)

        assertEquals(AgendamentoMapper.toDto(agendamento), result)
        verify(repository).save(any(Agendamento::class.java))

    }

    @Test
    fun `atualizarAtributo deve atualizar o atributo correto`() {
        val agendamento = Agendamento(1, null, null, null, null, null)
        `when`(repository.findById(1)).thenReturn(Optional.of(agendamento))
        `when`(repository.save(any(Agendamento::class.java))).thenReturn(agendamento.copy(preco = 150.0))

        val result = service.atualizarAtributo(1, "preco", "150.0")

        assertEquals(150.0, result?.preco)
        verify(repository).save(any(Agendamento::class.java))
    }

    @Test
    fun `atualizarStatus deve atualizar o status corretamente`() {
        val agendamento = Agendamento(1, null, null, null, null, null)
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
        val agendamento = Agendamento(1, null, null, null, null, null)
        val novoValor = 150.0
        `when`(repository.findById(1)).thenReturn(Optional.of(agendamento))
        `when`(repository.save(any(Agendamento::class.java))).thenReturn(agendamento.copy(preco = novoValor))

        val result = service.atualizarValor(1, novoValor)

        assertEquals(novoValor, result?.preco)
        verify(repository).save(any(Agendamento::class.java))
    }
}