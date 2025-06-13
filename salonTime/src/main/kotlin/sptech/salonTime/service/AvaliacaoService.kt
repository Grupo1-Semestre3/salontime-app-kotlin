package sptech.salonTime.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import sptech.salonTime.dto.AvaliacaoDto
import sptech.salonTime.dto.avalicao.AtualizarAvaliacaoDto
import sptech.salonTime.dto.avalicao.CadastroAvaliacaoDto
import sptech.salonTime.entidade.Avaliacao
import sptech.salonTime.exception.AvaliacaoNaoExisteException
import sptech.salonTime.exception.UsuarioNaoEncontradoException
import sptech.salonTime.mapper.AvaliacaoMapper
import sptech.salonTime.repository.AgendamentoRepository
import sptech.salonTime.repository.AvaliacaoRepository
import sptech.salonTime.repository.UsuarioRepository
import java.time.LocalDateTime

@Service
class AvaliacaoService @Autowired constructor(
    private val repository: AvaliacaoRepository,
    private val usuarioRepository: UsuarioRepository,
    private val agendamentoRepository: AgendamentoRepository,
) {

    fun listar(): List<AvaliacaoDto> {
        var avaliacao = repository.findAll()

        return avaliacao.map { AvaliacaoMapper.toDto(it) }
    }

    fun buscarPorId(id: Int): AvaliacaoDto {
        var avaliacao = repository.findById(id)
            .orElseThrow { AvaliacaoNaoExisteException("Avaliação não encontrada") }

        return AvaliacaoMapper.toDto(avaliacao)

    }

    fun cadastrar(avaliacao: CadastroAvaliacaoDto): Avaliacao {

        if (!usuarioRepository.existsById(avaliacao.usuario.id)) {
            throw UsuarioNaoEncontradoException("Usuário não encontrado")
        }

        if (!agendamentoRepository.existsById(avaliacao.agendamento.id
                ?: throw IllegalArgumentException("ID do agendamento não pode ser nulo"))) {
            throw AvaliacaoNaoExisteException("Agendamento não encontrado")
        }

        val novaAvaliacao = Avaliacao(
            agendamento = avaliacao.agendamento,
            usuario = avaliacao.usuario,
            notaServico = avaliacao.notaServico,
            descricaoServico = avaliacao.descricaoServico,
            dataHorario = LocalDateTime.now()
        )

        return repository.save(novaAvaliacao)
    }

    fun atualizar(id: Int, avaliacao: AtualizarAvaliacaoDto): AvaliacaoDto {
        var avaliacaoExistente = repository.findById(id)
            .orElseThrow { AvaliacaoNaoExisteException("Avaliação não encontrada") }

        avaliacaoExistente.id = id
        avaliacaoExistente.notaServico = avaliacao.notaServico
        avaliacaoExistente.descricaoServico = avaliacao.descricaoServico

        val avaliacaoSalva = repository.save(avaliacaoExistente)

        return AvaliacaoMapper.toDto(avaliacaoSalva)
    }

    fun deletar(id: Int) {
        var avaliacao = repository.findById(id)
            .orElseThrow { AvaliacaoNaoExisteException("Avaliação não encontrada") }

        repository.delete(avaliacao)

    }



}
