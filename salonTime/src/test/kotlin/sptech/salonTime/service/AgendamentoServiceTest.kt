package sptech.salonTime.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import sptech.salonTime.dto.CadastroAgendamentoDto
import sptech.salonTime.entidade.*
import sptech.salonTime.exception.*
import sptech.salonTime.mapper.AgendamentoMapper
import sptech.salonTime.repository.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.LocalDateTime
import java.sql.Date
import java.util.*

class AgendamentoServiceTest {

    private val repository = mock(AgendamentoRepository::class.java)
    private val pagamentoRepository = mock(PagamentoRepository::class.java)
    private val usuarioRepository = mock(UsuarioRepository::class.java)
    private val statusAgendamentoRepository = mock(StatusAgendamentoRepository::class.java)
    private val servicoRepository = mock(ServicoRepository::class.java)
    private val cupomRepository = mock(CupomRepository::class.java)
    private val funcionamentoRepository = mock(FuncionamentoRepository::class.java)
    private val horarioExcecaoRepository = mock(HorarioExcecaoRepository::class.java)
    private val funcionarioCompetenciaRepository = mock(FuncionarioCompetenciaRepository::class.java)
    private val cupomDestinadoRepository = mock(CupomDestinadoRepository::class.java)
    private val repositoryCupomDestinadoRepository = mock(CupomDestinadoRepository::class.java)
    private val emailService = mock(EmailService::class.java)

    private val service = AgendamentoService(
        repository,
        pagamentoRepository,
        usuarioRepository,
        statusAgendamentoRepository,
        servicoRepository,
        cupomRepository,
        funcionamentoRepository,
        horarioExcecaoRepository,
        funcionarioCompetenciaRepository,
        repositoryCupomDestinadoRepository,
        cupomDestinadoRepository,
        emailService
    )

    @Test
    fun `listar retorna todos os agendamentos`() {
        val tipoUsuario = TipoUsuario(1, "CLIENTE")
        val usuario = Usuario(1, tipoUsuario, "Cliente", "11999999999", "12345678901", "cliente@email.com", "senha123", Date.valueOf("2000-01-01"), LocalDateTime.now(), null, false, true)
        val servico = Servico(1, "Corte", 100.0, LocalTime.of(1, 0), "ATIVO", false, "desc", null)
        val status = StatusAgendamento(1, "AGENDADO")
        val pagamento = Pagamento(1, "Pagamento")
        val agendamento = Agendamento(1, servico, usuario, usuario, null, status, pagamento, LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(11, 0), 100.0)
        val agendamentos = listOf(agendamento)
        `when`(repository.listarTudo()).thenReturn(agendamentos)

        val result = service.listar()

        assertEquals(agendamentos.map { AgendamentoMapper.toDto(it) }, result)
        verify(repository).listarTudo()
    }

    @Test
    fun `listarPorId retorna agendamento existente`() {
        val tipoUsuario = TipoUsuario(1, "CLIENTE")
        val usuario = Usuario(1, tipoUsuario, "Cliente", "11999999999", "12345678901", "cliente@email.com", "senha123", Date.valueOf("2000-01-01"), LocalDateTime.now(), null, false, true)
        val servico = Servico(1, "Corte", 100.0, LocalTime.of(1, 0), "ATIVO", false, "desc", null)
        val status = StatusAgendamento(1, "AGENDADO")
        val pagamento = Pagamento(1, "Pagamento")
        val agendamento = Agendamento(1, servico, usuario, usuario, null, status, pagamento, LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(11, 0), 100.0)
        `when`(repository.findById(1)).thenReturn(Optional.of(agendamento))

        val result = service.listarPorId(1)

        assertEquals(AgendamentoMapper.toDto(agendamento), result)
    }

    @Test
    fun `listarPorId lança exceção se não existir`() {
        `when`(repository.findById(1)).thenReturn(Optional.empty())

        assertThrows<AgendamentoNaoEncontradoException> {
            service.listarPorId(1)
        }
    }

    @Test
    fun `cadastrar salva e retorna agendamento com cupom válido`() {
        val tipoUsuario = TipoUsuario(1, "CLIENTE")
        val usuario = Usuario(1, tipoUsuario, "Cliente", "11999999999", "12345678901", "cliente@email.com", "senha123", Date.valueOf("2000-01-01"), LocalDateTime.now(), null, false, true)
        val funcionarioTipo = TipoUsuario(3, "FUNCIONARIO")
        val funcionario = Usuario(2, funcionarioTipo, "Funcionario", "11999999998", "12345678902", "func@email.com", "senha123", Date.valueOf("1990-01-01"), LocalDateTime.now(), null, false, true)
        val servico = Servico(1, "Corte", 100.0, LocalTime.of(1, 0), "ATIVO", false, "desc", null)
        val status = StatusAgendamento(1, "AGENDADO")
        val pagamento = Pagamento(1, "Pagamento")
        val cupom = Cupom(1, "CUPOM10", "desc", "CUPOM10", true, LocalDate.now(), LocalDate.now().plusDays(2), "TODOS", 10)
        val cadastroDto = CadastroAgendamentoDto(
            usuario = 1,
            servico = 1,
            funcionario = 2,
            statusAgendamento = 1,
            pagamento = 1,
            cupom = "CUPOM10",
            data = LocalDate.now().plusDays(1),
            inicio = LocalTime.of(10, 0),
            preco = 100.0
        )
        val agendamento = Agendamento(
            id = 1, servico = servico, usuario = usuario, funcionario = funcionario, cupom = cupom,
            statusAgendamento = status, pagamento = pagamento, data = cadastroDto.data,
            inicio = cadastroDto.inicio, fim = cadastroDto.inicio.plusHours(1), preco = cadastroDto.preco
        )

        `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario))
        `when`(servicoRepository.findById(1)).thenReturn(Optional.of(servico))
        `when`(usuarioRepository.buscarIdsFuncionarios()).thenReturn(listOf(2))
        `when`(usuarioRepository.buscasFuncionariosDisponiveisPorData(any(), any(), any(), any())).thenReturn(listOf(2))
        `when`(usuarioRepository.findById(2)).thenReturn(Optional.of(funcionario))
        `when`(statusAgendamentoRepository.findById(1)).thenReturn(Optional.of(status))
        `when`(pagamentoRepository.findById(1)).thenReturn(Optional.of(pagamento))
        `when`(cupomRepository.findByCodigo("CUPOM10")).thenReturn(cupom)
        `when`(repository.existeConflitoDeAgendamento(any(), any(), any())).thenReturn(0)
        `when`(repository.save(any())).thenReturn(agendamento)
        `when`(repositoryCupomDestinadoRepository.findByCupomAndUsuario(any(), any())).thenReturn(null)
        `when`(cupomDestinadoRepository.save(any())).thenReturn(CupomDestinado(1,cupom, usuario, true))

        val result = service.cadastrar(cadastroDto)

        assertEquals(AgendamentoMapper.toDto(agendamento), result)
        verify(repository).save(any())
        usuario.email?.let {
            verify(emailService).enviarEmail(
                nome = eq(usuario.nome),
                servico = eq(servico.nome),
                data = eq(cadastroDto.data.toString()),
                hora = eq(cadastroDto.inicio.toString().substring(0, 5)),
                assunto = eq("Confirmação de Agendamento"),
                destinatario = it,
                destinoTipo = eq("Funcionario")
            )
        }
    }

    @Test
    fun `cadastrar lança exceção se cupom expirado`() {
        val tipoUsuario = TipoUsuario(1, "CLIENTE")
        val usuario = Usuario(1, tipoUsuario, "Cliente", "11999999999", "12345678901", "cliente@email.com", "senha123", Date.valueOf("2000-01-01"), LocalDateTime.now(), null, false, true)
        val servico = Servico(1, "Corte", 100.0, LocalTime.of(1, 0), "ATIVO", false, "desc", null)
        val cupom = Cupom(1, "CUPOM10", "desc", "CUPOM10", true, LocalDate.now(), LocalDate.now().minusDays(1), "TODOS", 10)
        val cadastroDto = CadastroAgendamentoDto(
            usuario = 1, servico = 1, funcionario = 2, statusAgendamento = 1, pagamento = 1,
            cupom = "CUPOM10", data = LocalDate.now().plusDays(1), inicio = LocalTime.of(10, 0), preco = 100.0
        )

        `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario))
        `when`(servicoRepository.findById(1)).thenReturn(Optional.of(servico))
        `when`(cupomRepository.findByCodigo("CUPOM10")).thenReturn(cupom)

        assertThrows<CupomNaoEncontradoException> {
            service.cadastrar(cadastroDto)
        }
    }

    @Test
    fun `atualizarAtributo atualiza preco corretamente`() {
        val tipoUsuario = TipoUsuario(1, "CLIENTE")
        val usuario = Usuario(1, tipoUsuario, "Cliente", "11999999999", "12345678901", "cliente@email.com", "senha123", Date.valueOf("2000-01-01"), LocalDateTime.now(), null, false, true)
        val servico = Servico(1, "Corte", 100.0, LocalTime.of(1, 0), "ATIVO", false, "desc", null)
        val status = StatusAgendamento(1, "AGENDADO")
        val pagamento = Pagamento(1, "Pagamento")
        val agendamento = Agendamento(1, servico, usuario, usuario, null, status, pagamento, LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(11, 0), 100.0)
        `when`(repository.findById(1)).thenReturn(Optional.of(agendamento))
        `when`(repository.save(any())).thenReturn(agendamento.copy(preco = 150.0))

        val result = service.atualizarAtributo(1, "preco", "150.0")

        assertEquals(150.0, result.preco)
        verify(repository).save(any())
    }

    @Test
    fun `atualizarAtributo lança exceção para atributo inválido`() {
        val tipoUsuario = TipoUsuario(1, "CLIENTE")
        val usuario = Usuario(1, tipoUsuario, "Cliente", "11999999999", "12345678901", "cliente@email.com", "senha123", Date.valueOf("2000-01-01"), LocalDateTime.now(), null, false, true)
        val servico = Servico(1, "Corte", 100.0, LocalTime.of(1, 0), "ATIVO", false, "desc", null)
        val status = StatusAgendamento(1, "AGENDADO")
        val pagamento = Pagamento(1, "Pagamento")
        val agendamento = Agendamento(1, servico, usuario, usuario, null, status, pagamento, LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(11, 0), 100.0)
        `when`(repository.findById(1)).thenReturn(Optional.of(agendamento))

        assertThrows<AtributoInvalidoAoAtualizarException> {
            service.atualizarAtributo(1, "invalido", "valor")
        }
    }

    @Test
    fun `atualizarStatus atualiza status corretamente`() {
        val tipoUsuario = TipoUsuario(1, "CLIENTE")
        val usuario = Usuario(1, tipoUsuario, "Cliente", "11999999999", "12345678901", "cliente@email.com", "senha123", Date.valueOf("2000-01-01"), LocalDateTime.now(), null, false, true)
        val servico = Servico(1, "Corte", 100.0, LocalTime.of(1, 0), "ATIVO", false, "desc", null)
        val status = StatusAgendamento(1, "AGENDADO")
        val novoStatus = StatusAgendamento(2, "Novo Status")
        val pagamento = Pagamento(1, "Pagamento")
        val agendamento = Agendamento(1, servico, usuario, usuario, null, status, pagamento, LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(11, 0), 100.0)
        `when`(repository.findById(1)).thenReturn(Optional.of(agendamento))
        `when`(statusAgendamentoRepository.findById(2)).thenReturn(Optional.of(novoStatus))
        `when`(repository.save(any())).thenReturn(agendamento.copy(statusAgendamento = novoStatus))

        val result = service.atualizarStatus(1, 2)

        assertEquals(novoStatus.id, result?.statusAgendamento?.id)
        verify(repository).save(any())
    }

    @Test
    fun `atualizarStatus lança exceção se agendamento não existe`() {
        `when`(repository.findById(1)).thenReturn(Optional.empty())

        assertThrows<AgendamentoNaoEncontradoException> {
            service.atualizarStatus(1, 2)
        }
    }

    @Test
    fun `atualizarValor atualiza valor corretamente`() {
        val tipoUsuario = TipoUsuario(1, "CLIENTE")
        val usuario = Usuario(1, tipoUsuario, "Cliente", "11999999999", "12345678901", "cliente@email.com", "senha123", Date.valueOf("2000-01-01"), LocalDateTime.now(), null, false, true)
        val servico = Servico(1, "Corte", 100.0, LocalTime.of(1, 0), "ATIVO", false, "desc", null)
        val status = StatusAgendamento(1, "AGENDADO")
        val pagamento = Pagamento(1, "Pagamento")
        val agendamento = Agendamento(1, servico, usuario, usuario, null, status, pagamento, LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(11, 0), 100.0)
        val novoValor = 150.0
        `when`(repository.findById(1)).thenReturn(Optional.of(agendamento))
        `when`(repository.save(any())).thenReturn(agendamento.copy(preco = novoValor))
        `when`(pagamentoRepository.findById(any())).thenReturn(Optional.of(pagamento))

        val result = service.atualizarValor(1, novoValor)

        assertEquals(novoValor, result?.preco)
        verify(repository).save(any())
    }

    @Test
    fun `atualizarValor lança exceção se agendamento não existe`() {
        `when`(repository.findById(1)).thenReturn(Optional.empty())

        assertThrows<AgendamentoNaoEncontradoException> {
            service.atualizarValor(1, 100.0)
        }
    }
}
