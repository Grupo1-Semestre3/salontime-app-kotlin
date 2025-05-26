package sptech.salonTime.service

import org.springframework.stereotype.Service
import sptech.salonTime.entidade.DescCancelamento
import sptech.salonTime.repository.DescCancelamentoRepository

@Service
class DescCancelamentoService (var repository: DescCancelamentoRepository) {

    fun listar(): List<DescCancelamento> {
        return repository.findAll() ?: emptyList()
    }

    fun listarPorId(id: Int): DescCancelamento? {
        return repository.findById(id).orElse(null)
    }

    fun criar(descCancelamento: DescCancelamento): DescCancelamento {
        return repository.save(descCancelamento)
    }

    fun atualizar(id: Int, descCancelamento: DescCancelamento): DescCancelamento? {
       var cancelamento = repository.findById(id).orElse(null)
        if (cancelamento != null) {
            cancelamento.id = id
            cancelamento.descricao = descCancelamento.descricao
            cancelamento.agendamento = descCancelamento.agendamento
            return repository.save(cancelamento)
        } else {
            return null
        }
    }

    fun atualizarDescricao(id: Int, novaDescricao: String): DescCancelamento? {
        val cancelamento = repository.findById(id).orElse(null)
        if (cancelamento != null) {
            cancelamento.descricao = novaDescricao
            return repository.save(cancelamento)
        } else {
            return null
        }
    }

    fun deletar(id: Int) {
        val cancelamento = repository.findById(id).orElse(null)
        if (cancelamento != null) {
            repository.delete(cancelamento)
        }
    }
}