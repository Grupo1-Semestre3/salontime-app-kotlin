package sptech.salonTime.service

import org.springframework.stereotype.Service
import sptech.salonTime.entidade.DescCancelamento
import sptech.salonTime.repository.DescCancelamentoRepository

@Service
class DescCancelamentoService (val repository: DescCancelamentoRepository) {

    fun listar(): List<DescCancelamento> {
        return repository.findAll() ?: emptyList()
    }

    fun listarPorId(id: Int): DescCancelamento? {
        return repository.findById(id).orElse(null)
    }

    fun criar(descCancelamento: DescCancelamento): DescCancelamento {
        return repository.save(descCancelamento)
    }

}