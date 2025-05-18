package sptech.salonTime.service

import org.springframework.stereotype.Service
import sptech.salonTime.entidade.InfoSalao
import sptech.salonTime.exception.AtributoInvalidoAoAtualizarException
import sptech.salonTime.repository.InfoSalaoRepository

@Service
class InfoSalaoService(private val repository: InfoSalaoRepository) {

    fun listar(): MutableList<InfoSalao> {
        return repository.findAll()
    }

    fun editar(atributo: String, novoValor: String): InfoSalao {
        val infoSalaoExistente = repository.findById(1)
            .orElseThrow { IllegalArgumentException("InfoSalao não encontrado") }

        try {

            val infoSalaoAtualizado = when (atributo) {
                "email" -> infoSalaoExistente.copy(email = novoValor)
                "telefone" -> infoSalaoExistente.copy(telefone = novoValor)
                "logradouro" -> infoSalaoExistente.copy(logradouro = novoValor)
                "numero" -> infoSalaoExistente.copy(numero = novoValor)
                "cidade" -> infoSalaoExistente.copy(cidade = novoValor)
                "estado" -> infoSalaoExistente.copy(estado = novoValor)
                "complemento" -> infoSalaoExistente.copy(complemento = novoValor)
                else -> throw AtributoInvalidoAoAtualizarException("Atributo inválido: $atributo")
            }.let { repository.save(it) }

            return infoSalaoAtualizado

        }catch (e : Exception){
            throw IllegalArgumentException("Erro ao atualizar o atributo: $atributo. Verifique o valor fornecido.")
        }



    }
}