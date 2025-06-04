package sptech.salonTime.service

import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import sptech.salonTime.dto.AvaliacaoDto
import sptech.salonTime.dto.CadastroUsuarioDto
import sptech.salonTime.dto.avalicao.AtualizarAvaliacaoDto
import sptech.salonTime.dto.avalicao.CadastroAvaliacaoDto
import sptech.salonTime.entidade.*
import sptech.salonTime.mapper.AvaliacaoMapper
import sptech.salonTime.mapper.UsuarioMapper
import sptech.salonTime.repository.AvaliacaoRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.test.assertEquals

class AvaliacaoServiceTest {

    private val repository = mock(AvaliacaoRepository::class.java)
    private val service = AvaliacaoService(repository)

    @Test
    fun `listar deve retornar todas as avaliacoes`() {
        val usuarioDto = CadastroUsuarioDto("usuario", "email@email.com", "senha", "11999999999")
        val usuario = UsuarioMapper.toEntity(usuarioDto)?.apply { tipoUsuario = TipoUsuario(2, "CLIENTE") }

        val funcionario = UsuarioMapper.toEntity(usuarioDto)?.apply { tipoUsuario = TipoUsuario(2, "CLIENTE") }
        val servico = Servico(1, "Servico", 50.0, LocalTime.of(1, 0), "ATIVO", true)

        val agendamento = Agendamento(
            id = 10,
            servico = servico,
            usuario = usuario,
            funcionario = funcionario,
            statusAgendamento = StatusAgendamento(),
            pagamento = Pagamento(),
            data = LocalDate.of(2025, 6, 1),
            inicio = LocalTime.of(10, 0),
            fim = LocalTime.of(11, 0),
            preco = 100.0
        )

        val avaliacao = Avaliacao(
            id = 1,
            agendamento = agendamento,
            usuario = usuario,
            notaServico = 5,
            descricaoServico = "Ótimo serviço",
            dataHorario = LocalDateTime.of(2025, 6, 1, 10, 0)
        )

        val avaliacaoDto = AvaliacaoDto(
            id = 1,
            nomeServico = "Servico",
            dataAgendamento = "2025-06-01",
            agendamentoId = 10,
            nomeFuncionario = "usuario",
            nomeUsuario = "usuario",
            notaServico = 5,
            descricaoServico = "Ótimo serviço",
            dataHorario = LocalDateTime.of(2025, 6, 1, 10, 0)
        )

        `when`(repository.findAll()).thenReturn(listOf(avaliacao))

        val result = service.listar()

        assertEquals(listOf(avaliacaoDto), result)
        verify(repository).findAll()
    }

    @Test
    fun `buscarPorId deve retornar avaliacao existente`() {
        val usuarioDto = CadastroUsuarioDto("usuario", "email@email.com", "senha", "11999999999")
        val usuario = UsuarioMapper.toEntity(usuarioDto)?.apply { tipoUsuario = TipoUsuario(2, "CLIENTE") }
        val funcionario = UsuarioMapper.toEntity(usuarioDto)?.apply { tipoUsuario = TipoUsuario(2, "CLIENTE") }
        val servico = Servico(1, "Corte", 50.0, LocalTime.of(1, 0), "ATIVO", true)


        val agendamento = Agendamento(
            id = 10,
            servico = servico,
            usuario = usuario,
            funcionario = funcionario,
            statusAgendamento = StatusAgendamento(),
            pagamento = Pagamento(),
            data = LocalDate.of(2025, 6, 1),
            inicio = LocalTime.of(10, 0),
            fim = LocalTime.of(11, 0),
            preco = 100.0
        )

        val avaliacao = Avaliacao(
            id = 1,
            agendamento = agendamento,
            usuario = usuario,
            notaServico = 5,
            descricaoServico = "Ótimo serviço",
            dataHorario = LocalDateTime.of(2025, 6, 1, 10, 0)
        )

        `when`(repository.findById(1)).thenReturn(java.util.Optional.of(avaliacao))

        val result = service.buscarPorId(1)

        val expected = AvaliacaoDto(
            id = 1,
            nomeServico = "Corte",
            dataAgendamento = "2025-06-01",
            agendamentoId = 10,
            nomeFuncionario = "usuario",
            nomeUsuario = "usuario",
            notaServico = 5,
            descricaoServico = "Ótimo serviço",
            dataHorario = LocalDateTime.of(2025, 6, 1, 10, 0)
        )

        assertEquals(expected, result)
        verify(repository).findById(1)
    }

    @Test
    fun `cadastrar deve criar nova avaliacao`() {
        val agendamento = Agendamento()
        val usuario = Usuario()

        val cadastroDto = CadastroAvaliacaoDto(
            agendamento = agendamento,
            usuario = usuario,
            notaServico = 5,
            descricaoServico = "Ótimo serviço",
            dataHorario = LocalDateTime.of(2025, 6, 1, 10, 0)
        )

        val savedAvaliacao = Avaliacao(
            id = 1,
            agendamento = agendamento,
            usuario = usuario,
            notaServico = 5,
            descricaoServico = "Ótimo serviço",
            dataHorario = cadastroDto.dataHorario
        )

        `when`(repository.save(any(Avaliacao::class.java))).thenReturn(savedAvaliacao)

        val result = service.cadastrar(cadastroDto)

        assertEquals(savedAvaliacao, result)
        verify(repository).save(any(Avaliacao::class.java))
    }

    @Test
    fun `atualizar deve atualizar avaliacao existente`() {
        val agendamento = Agendamento(1, null, null, null, null, null, null, LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(11, 0), 100.0)

        val avaliacaoExistente = Avaliacao(
            id = 1,
            agendamento = agendamento,
            usuario = Usuario(),
            notaServico = 5,
            descricaoServico = "Ótimo serviço",
            dataHorario = LocalDateTime.now()
        )

        val atualizarDto = AtualizarAvaliacaoDto(
            notaServico = 4,
            descricaoServico = "Bom serviço"
        )

        val avaliacaoAtualizada = avaliacaoExistente.copy(
            notaServico = atualizarDto.notaServico,
            descricaoServico = atualizarDto.descricaoServico
        )

        `when`(repository.findById(1)).thenReturn(java.util.Optional.of(avaliacaoExistente))
        `when`(repository.save(any(Avaliacao::class.java))).thenReturn(avaliacaoAtualizada)

        val result = service.atualizar(1, atualizarDto)

        val expected = AvaliacaoDto(
            id = 1,
            nomeServico = "Serviço não informado",
            dataAgendamento = LocalDate.now().toString(),
            agendamentoId = 1,
            nomeFuncionario = "Funcionário não informado",
            nomeUsuario = "Usuário não informado",
            notaServico = 4,
            descricaoServico = "Bom serviço",
            dataHorario = result.dataHorario // Use o mesmo valor gerado no resultado
        )

        assertEquals(expected, result)
        verify(repository).findById(1)
        verify(repository).save(any(Avaliacao::class.java))
    }

    @Test
    fun `deletar deve remover avaliacao existente`() {
        val avaliacaoExistente = Avaliacao(
            id = 1,
            agendamento = Agendamento(),
            usuario = Usuario(),
            notaServico = 5,
            descricaoServico = "Ótimo serviço",
            dataHorario = LocalDateTime.now()
        )

        `when`(repository.findById(1)).thenReturn(java.util.Optional.of(avaliacaoExistente))

        service.deletar(1)

        verify(repository).findById(1)
        verify(repository).delete(avaliacaoExistente)
    }
}
