package sptech.salonTime.service

import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import sptech.salonTime.dto.avalicao.AtualizarAvaliacaoDto
import sptech.salonTime.dto.avalicao.CadastroAvaliacaoDto
import sptech.salonTime.entidade.Avaliacao
import sptech.salonTime.exception.AvaliacaoNaoExisteException
import sptech.salonTime.repository.AvaliacaoRepository
import java.time.LocalDateTime
import kotlin.test.assertEquals

class AvaliacaoServiceTest {

    private val repository = mock(AvaliacaoRepository::class.java)
    private val service = AvaliacaoService(repository)

    @Test
    fun `listar deve retornar todas as avaliacoes`() {
        val avaliacoes = listOf(
            Avaliacao(
                id = 1,
                agendamento = null, // Mock ou stub necessário
                usuario = null, // Mock ou stub necessário
                notaServico = 5,
                descricaoServico = "Ótimo serviço",
                dataHorario = LocalDateTime.now() // Mock ou stub necessário
            ),
            Avaliacao(
                id = 2,
                agendamento = null, // Mock ou stub necessário
                usuario = null, // Mock ou stub necessário
                notaServico = 4,
                descricaoServico = "Bom serviço",
                dataHorario = LocalDateTime.now() // Mock ou stub necessário
            ))
        `when`(repository.findAll()).thenReturn(avaliacoes)

        val result = service.listar()

        assertEquals(avaliacoes, result)
        verify(repository).findAll()
    }

    @Test
    fun `buscarPorId deve retornar avaliacao existente`() {
        val avaliacao = Avaliacao(
            id = 1,
            agendamento = null, // Mock ou stub necessário
            usuario = null, // Mock ou stub necessário
            notaServico = 5,
            descricaoServico = "Ótimo serviço",
            dataHorario = LocalDateTime.now() // Mock ou stub necessário
        )
        `when`(repository.findById(1)).thenReturn(java.util.Optional.of(avaliacao))

        val result = service.buscarPorId(1)

        assertEquals(avaliacao, result)
        verify(repository).findById(1)
    }

    @Test
    fun `cadastrar deve criar nova avaliacao`() {

        // Mock ou stub necessário para agendamento e usuário
        val agendamento = mock(sptech.salonTime.entidade.Agendamento::class.java)
        val usuario = mock(sptech.salonTime.entidade.Usuario::class.java)

        val avaliacao = CadastroAvaliacaoDto(
            agendamento = agendamento, // Mock ou stub necessário
            usuario = usuario, // Mock ou stub necessário
            notaServico = 5,
            descricaoServico = "Ótimo serviço",
            dataHorario = LocalDateTime.now() // Mock ou stub necessário
        )
        `when`(repository.save(any(Avaliacao::class.java))).thenReturn(
            Avaliacao(
                id = 1, // ID gerado pelo banco de dados
                agendamento = avaliacao.agendamento,
                usuario = avaliacao.usuario,
                notaServico = avaliacao.notaServico,
                descricaoServico = avaliacao.descricaoServico,
                dataHorario = avaliacao.dataHorario
            )
        )

        val result = service.cadastrar(avaliacao)

        assertEquals(
            Avaliacao(
                id = 1, // ID gerado pelo banco de dados
                agendamento = avaliacao.agendamento,
                usuario = avaliacao.usuario,
                notaServico = avaliacao.notaServico,
                descricaoServico = avaliacao.descricaoServico,
                dataHorario = avaliacao.dataHorario
            ), result
        )
        verify(repository).save(any(Avaliacao::class.java))
    }

    @Test
    fun `atualizar deve atualizar avaliacao existente`() {
        val avaliacaoExistente = Avaliacao(
            id = 1,
            agendamento = null, // Mock ou stub necessário
            usuario = null, // Mock ou stub necessário
            notaServico = 5,
            descricaoServico = "Ótimo serviço",
            dataHorario = LocalDateTime.now() // Mock ou stub necessário
        )

        val avaliacaoAtualizadaDto = AtualizarAvaliacaoDto(
            notaServico = 4,
            descricaoServico = "Bom serviço"
        )

        `when`(repository.findById(1)).thenReturn(java.util.Optional.of(avaliacaoExistente))
        `when`(repository.save(any(Avaliacao::class.java))).thenReturn(avaliacaoExistente.copy(
            notaServico = avaliacaoAtualizadaDto.notaServico,
            descricaoServico = avaliacaoAtualizadaDto.descricaoServico
        ))

        val result = service.atualizar(1, avaliacaoAtualizadaDto)

        assertEquals(avaliacaoExistente.copy(
            notaServico = avaliacaoAtualizadaDto.notaServico,
            descricaoServico = avaliacaoAtualizadaDto.descricaoServico
        ), result)

        verify(repository).findById(1)
        verify(repository).save(any(Avaliacao::class.java))
    }

    @Test
    fun `deletar deve remover avaliacao existente`() {
        val avaliacaoExistente = Avaliacao(
            id = 1,
            agendamento = null, // Mock ou stub necessário
            usuario = null, // Mock ou stub necessário
            notaServico = 5,
            descricaoServico = "Ótimo serviço",
            dataHorario = LocalDateTime.now() // Mock ou stub necessário
        )

        `when`(repository.findById(1)).thenReturn(java.util.Optional.of(avaliacaoExistente))

        service.deletar(1)

        verify(repository).findById(1)
        verify(repository).delete(avaliacaoExistente)
    }

}