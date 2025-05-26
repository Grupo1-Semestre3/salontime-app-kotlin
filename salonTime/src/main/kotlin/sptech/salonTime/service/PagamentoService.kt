package sptech.salonTime.service

import org.springframework.stereotype.Service
import sptech.salonTime.entidade.Pagamento
import sptech.salonTime.repository.PagamentoRepository

@Service
class PagamentoService (var repository: PagamentoRepository) {

    fun listar(): List<Pagamento> {
        return repository.findAll() ?: emptyList()
    }

    fun listarPorId(id: Int): Pagamento? {
        return repository.findById(id).orElse(null)
    }

    fun criar(pagamento: Pagamento): Pagamento {
        return repository.save(pagamento)
    }

    fun atualizar(id: Int, pagamento: Pagamento): Pagamento? {
        val pagamentoExistente = repository.findById(id).orElse(null)
        if (pagamentoExistente != null) {
            pagamento.id = id
            return repository.save(pagamento)
        }
        return null
    }

    fun deletar(id: Int) {
        val pagamento = repository.findById(id).orElse(null)
        if (pagamento != null) {
            repository.delete(pagamento)
        }
    }

}