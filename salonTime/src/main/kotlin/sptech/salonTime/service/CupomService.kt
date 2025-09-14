package sptech.salonTime.service

import org.springframework.stereotype.Service
import sptech.salonTime.entidade.Cupom
import sptech.salonTime.repository.CupomRepository

@Service
class CupomService(val repository: CupomRepository) {

    fun criar(cupom: Cupom): Cupom {
        return repository.save(cupom)
    }


    fun listarAtivos(): List<Cupom> {
        return repository.listarAtivos()
    }


    fun atualizar(id: Int, cupomAtualizado: Cupom): Cupom? {
        val cupom = repository.findById(id).orElse(null)
        return if (cupom != null) {
            val atualizado = cupomAtualizado.copy(id = id)
            repository.save(atualizado)
        } else {
            null
        }
    }

    fun deletar(id: Int): Boolean {
        return if (repository.existsById(id)) {
            repository.deleteById(id)
            true
        } else {
            false
        }
    }

   /* fun validarVigencia(codigo: String): Cupom? {

    }*/
}