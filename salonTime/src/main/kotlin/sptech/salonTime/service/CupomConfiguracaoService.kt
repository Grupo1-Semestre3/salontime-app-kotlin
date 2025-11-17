package sptech.salonTime.service

import org.springframework.stereotype.Service
import sptech.salonTime.entidade.CupomConfiguracao
import sptech.salonTime.exception.CupomConfiguracaoException
import sptech.salonTime.exception.UsuarioNaoEncontradoException
import sptech.salonTime.repository.CupomConfiguracaoRepository
import java.util.*


@Service
class CupomConfiguracaoService(
    private val repository: CupomConfiguracaoRepository
) {

    fun salvar(cupomConfiguracao: CupomConfiguracao): CupomConfiguracao {
        return repository.save(cupomConfiguracao)
    }

    fun editar(id: Int, cupomConfiguracao: CupomConfiguracao): CupomConfiguracao {
        if (!repository.existsById(id)) {
            throw CupomConfiguracaoException("Cupom de configuração com ID $id não encontrado.")
        }
        cupomConfiguracao.id = id
        return repository.save(cupomConfiguracao)
    }

    fun editarIntervalo(id: Int, novoIntervalo: Int): CupomConfiguracao {
        val cupom = repository.findById(id)
            .orElseThrow { CupomConfiguracaoException("Cupom de configuração com ID $id não encontrado.") }
        cupom.intervaloAtendimento = novoIntervalo
        return repository.save(cupom)
    }

    fun editarPorcentagem(id: Int, novaPorcentagem: Int): CupomConfiguracao {
        val cupom = repository.findById(id)
            .orElseThrow { CupomConfiguracaoException("Cupom de configuração com ID $id não encontrado.") }
        cupom.porcentagemDesconto = novaPorcentagem
        return repository.save(cupom)
    }

    fun listarTodos():List<CupomConfiguracao>{
        return repository.findAll()
    }

}