package sptech.salonTime.service

import org.springframework.stereotype.Service
import sptech.salonTime.entidade.Cupom
import sptech.salonTime.exception.CupomDuplicadoException
import sptech.salonTime.exception.CupomNaoEncontradoException
import sptech.salonTime.repository.CupomRepository

@Service
class CupomService(val repository: CupomRepository) {

    fun criar(cupom: Cupom): Cupom {

        val existente = repository.findByCodigo(cupom.codigo)

        if (existente != null) {
            throw CupomDuplicadoException("Cupom com código '${cupom.codigo}' já existe.")
        }
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

    fun desativar(id: Int): Boolean {
        val cupom = repository.findById(id)
        return if (cupom.isPresent) {
            val cupomAtualizado = cupom.get().apply { ativo = false }
            repository.save(cupomAtualizado)
            true
        } else {
            false
        }
    }
}