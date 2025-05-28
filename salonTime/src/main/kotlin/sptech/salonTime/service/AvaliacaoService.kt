package sptech.salonTime.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import sptech.salonTime.dto.avalicao.AtualizarAvaliacaoDto
import sptech.salonTime.dto.avalicao.CadastroAvaliacaoDto
import sptech.salonTime.entidade.Avaliacao
import sptech.salonTime.exception.AvaliacaoNaoExisteException
import sptech.salonTime.repository.AvaliacaoRepository
import java.time.LocalDateTime

@Service
class AvaliacaoService @Autowired constructor(
    private val repository: AvaliacaoRepository
) {

    fun listar(): List<Avaliacao> {
        return repository.findAll()
    }

    fun buscarPorId(id: Int): Avaliacao {
        return repository.findById(id)
            .orElseThrow { AvaliacaoNaoExisteException("Avaliação não encontrada") }
    }

    fun cadastrar(avaliacao: CadastroAvaliacaoDto): Avaliacao {

        val novaAvaliacao = Avaliacao(
            agendamento = avaliacao.agendamento,
            usuario = avaliacao.usuario,
            notaServico = avaliacao.notaServico,
            descricaoServico = avaliacao.descricaoServico,
            dataHorario = LocalDateTime.now()
        )

        return repository.save(novaAvaliacao)
    }

    fun atualizar(id: Int, avaliacao: AtualizarAvaliacaoDto): Avaliacao {
        val avaliacaoExistente = buscarPorId(id)

        val avaliacaoAtualizada: Avaliacao = avaliacaoExistente.copy(
            notaServico = avaliacao.notaServico,
            descricaoServico = avaliacao.descricaoServico
        )

        return repository.save(avaliacaoAtualizada)
    }

    fun deletar(id: Int) {
        val avaliacaoExistente = buscarPorId(id)
        repository.delete(avaliacaoExistente)
    }



}
