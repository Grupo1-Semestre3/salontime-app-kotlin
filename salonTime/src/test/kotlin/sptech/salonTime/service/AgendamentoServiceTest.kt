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
        val usuario = Usuario(
            1, tipoUsuario, "Cliente", "11999999999", "12345678901",
            "cliente@email.com", "senha123",
            Date.valueOf("2000-01-01"), LocalDateTime.now(), null,
            false, true
        )

        val servico = Servico(
            1,
            "Corte",
            100.0,
            LocalTime.of(1, 0),
            "ATIVO",
            false,
            "desc",
            null
        )

        val status = StatusAgendamento(1, "AGENDADO")
        val pagamento = Pagamento(1, "Pagamento")

        val agendamento = Agendamento(
            1,
            servico,
            usuario,
            usuario,
            null,
            status,
            pagamento,
            LocalDate.now(),
            LocalTime.of(10, 0),
            LocalTime.of(11, 0),
            100.0
        )

        `when`(repository.findById(1)).thenReturn(Optional.of(agendamento))

        val result = service.listarPorId(1)

        assertEquals(AgendamentoMapper.toDto(agendamento), result)
        verify(repository).findById(1)
    }

    @Test
    fun `listarPorId lança exceção se não existir`() {
        `when`(repository.findById(1)).thenReturn(Optional.empty())

        assertThrows<AgendamentoNaoEncontradoException> {
            service.listarPorId(1)
        }
    }

    @Test
    fun `cadastrar retorna agendamento salvo com sucesso`() {
        // ARRANGE
        val tipoCliente = TipoUsuario(1, "CLIENTE")
        val tipoFuncionario = TipoUsuario(3, "FUNCIONARIO")

        val usuario = Usuario(
            1, tipoCliente, "Cliente", "11999999999", "12345678901",
            "cliente@email.com", "senha123",
            Date.valueOf("2000-01-01"), LocalDateTime.now(), null,
            false, true
        )

        val funcionario = Usuario(
            2, tipoFuncionario, "Funcionario", "11888888888", "98765432100",
            "func@email.com", "senha456",
            Date.valueOf("1995-05-05"), LocalDateTime.now(), null,
            false, true
        )

        val servico = Servico(
            10,
            "Corte",
            100.0,
            LocalTime.of(1, 0), // tempo
            "ATIVO",
            false,
            "desc",
            null
        )

        val status = StatusAgendamento(1, "AGENDADO")
        val pagamento = Pagamento(1, "DINHEIRO")

        // --- DTO atualizado com seu formato ---
        val dto = CadastroAgendamentoDto(
            usuario = 1,
            servico = 10,
            funcionario = 2,   // AGORA ALINHADO AO DTO
            statusAgendamento = 1,
            pagamento = 1,
            cupom = null,
            data = LocalDate.now().plusDays(1),
            inicio = LocalTime.of(10, 0),
            preco = 100.0
        )

        val fim = LocalTime.of(11, 0)

        val agendamentoSalvo = Agendamento(
            50,
            servico,
            usuario,
            funcionario,
            null,
            status,
            pagamento,
            dto.data,
            dto.inicio,
            fim,
            100.0
        )

        // ---------- MOCKS (mesma estrutura que você usa) ----------
        `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario))
        `when`(usuarioRepository.findById(2)).thenReturn(Optional.of(funcionario)) // funcionário

        `when`(servicoRepository.findById(10)).thenReturn(Optional.of(servico))
        `when`(statusAgendamentoRepository.findById(1)).thenReturn(Optional.of(status))
        `when`(pagamentoRepository.findById(1)).thenReturn(Optional.of(pagamento))

        // Sem conflito
        `when`(repository.existeConflitoDeAgendamento(dto.data, dto.inicio, fim))
            .thenReturn(0)

        // Funcionários disponíveis
        `when`(usuarioRepository.buscarIdsFuncionarios())
            .thenReturn(listOf(2))

        `when`(
            usuarioRepository.buscasFuncionariosDisponiveisPorData(
                dto.data,
                dto.inicio,
                fim,
                listOf(2)
            )
        ).thenReturn(listOf(2))

        `when`(repository.save(any())).thenReturn(agendamentoSalvo)

        // ACT
        val result = service.cadastrar(dto)

        // ASSERT
        assertEquals(50, result.id)
        assertEquals("Corte", result.servico?.nome)
        assertEquals("FUNCIONARIO", result.funcionario?.tipoUsuario?.descricao)

        verify(repository).save(any())
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
