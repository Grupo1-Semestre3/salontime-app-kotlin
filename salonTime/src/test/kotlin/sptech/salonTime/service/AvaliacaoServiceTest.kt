package sptech.salonTime.service

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import sptech.salonTime.dto.AvaliacaoDto
import sptech.salonTime.dto.avalicao.AtualizarAvaliacaoDto
import sptech.salonTime.dto.avalicao.CadastroAvaliacaoDto
import sptech.salonTime.entidade.Agendamento
import sptech.salonTime.entidade.Avaliacao
import sptech.salonTime.entidade.Usuario
import sptech.salonTime.mapper.AvaliacaoMapper
import sptech.salonTime.repository.AgendamentoRepository
import sptech.salonTime.repository.AvaliacaoRepository
import sptech.salonTime.repository.UsuarioRepository
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class AvaliacaoServiceTest {

    private lateinit var avaliacaoRepository: AvaliacaoRepository
    private lateinit var usuarioRepository: UsuarioRepository
    private lateinit var agendamentoRepository: AgendamentoRepository
    private lateinit var service: AvaliacaoService

    private val agendamento = Agendamento(id = 1)
    private val usuario = mock(Usuario::class.java).apply {
        `when`(id).thenReturn(1)
    }
    private val dataHora = LocalDateTime.now()

    private val avaliacao = Avaliacao(
        id = 1,
        agendamento = agendamento,
        usuario = usuario,
        notaServico = 5,
        descricaoServico = "Muito bom",
        dataHorario = dataHora
    )

    private val avaliacaoDto = AvaliacaoDto(
        id = 1,
        nomeServico = "Corte",
        dataAgendamento = "2025-01-01",
        agendamentoId = 1,
        nomeFuncionario = "João",
        nomeUsuario = "Maria",
        notaServico = 5,
        descricaoServico = "Muito bom",
        dataHorario = dataHora
    )

    @BeforeEach
    fun setup() {
        avaliacaoRepository = mock(AvaliacaoRepository::class.java)
        usuarioRepository = mock(UsuarioRepository::class.java)
        agendamentoRepository = mock(AgendamentoRepository::class.java)

        service = AvaliacaoService(avaliacaoRepository, usuarioRepository, agendamentoRepository)

    }

    @Test
    fun `listar deve retornar lista com DTOs`() {
        `when`(avaliacaoRepository.findAll()).thenReturn(listOf(avaliacao))

        val resultado = service.listar()

        assertEquals(1, resultado.size)
        assertEquals("Muito bom", resultado[0].descricaoServico)
        verify(avaliacaoRepository).findAll()
    }

    @Test
    fun `buscarPorId deve retornar DTO correto`() {
        `when`(avaliacaoRepository.findById(1)).thenReturn(Optional.of(avaliacao))

        val resultado = service.buscarPorId(1)

        assertNotNull(resultado)
        assertEquals(5, resultado.notaServico)
        verify(avaliacaoRepository).findById(1)
    }

    @Test
    fun `cadastrar deve salvar nova avaliação e retornar entidade`() {
        val cadastroDto = CadastroAvaliacaoDto(
            agendamento = agendamento,
            usuario = usuario,
            notaServico = 5,
            descricaoServico = "Muito bom",
            dataHorario = dataHora
        )


        `when`(usuarioRepository.existsById(usuario.id)).thenReturn(true)
        `when`(agendamento.id?.let { agendamentoRepository.existsById(it) }).thenReturn(true)
        `when`(avaliacaoRepository.save(Mockito.any(Avaliacao::class.java))).thenReturn(avaliacao)

        val resultado = service.cadastrar(cadastroDto)

        assertNotNull(resultado)
        assertEquals("Muito bom", resultado.descricaoServico)
    }

    @Test
    fun `atualizar deve modificar avaliação existente e retornar DTO`() {
        val atualizarDto = AtualizarAvaliacaoDto(
            notaServico = 4,
            descricaoServico = "Bom"
        )

        val atualizado = avaliacao.copy(
            notaServico = atualizarDto.notaServico,
            descricaoServico = atualizarDto.descricaoServico
        )

        `when`(avaliacaoRepository.findById(1)).thenReturn(Optional.of(avaliacao))
        `when`(avaliacaoRepository.save(any())).thenReturn(atualizado)

        val resultado = service.atualizar(1, atualizarDto)

        assertNotNull(resultado)
        assertEquals("Bom", resultado.descricaoServico) // Mockado para sempre retornar "Muito bom"
        verify(avaliacaoRepository).save(any())
    }

    @Test
    fun `deletar deve remover avaliação com sucesso`() {
        `when`(avaliacaoRepository.findById(1)).thenReturn(Optional.of(avaliacao))

        service.deletar(1)

        verify(avaliacaoRepository).delete(avaliacao)
    }
}
