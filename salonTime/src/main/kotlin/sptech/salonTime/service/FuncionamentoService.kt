package sptech.salonTime.service

import sptech.salonTime.entidade.DiaSemana
import sptech.salonTime.entidade.Funcionamento
import sptech.salonTime.repository.FuncionamentoRepository
import java.time.LocalTime

class FuncionamentoService(private val repository: FuncionamentoRepository) {

    fun listar(): List<Funcionamento> {
        return repository.findAll()
    }

    fun editar(id: Int, atributo: String, novoValor: String): Funcionamento {

        val funcionamentoExistente = repository.findById(id)
            .orElseThrow { IllegalArgumentException("Funcionamento não encontrado") }

        try {
            val funcionamentoAtualizado = when (atributo) {
                "diaSemana" -> funcionamentoExistente.copy(diaSemana = novoValor.toInt().let { DiaSemana.values()[it] })
                "incio" -> funcionamentoExistente.copy(inicio = LocalTime.parse(novoValor))
                "fim" -> funcionamentoExistente.copy(fim = LocalTime.parse(novoValor))
                "aberto" -> funcionamentoExistente.copy(aberto = novoValor.toBoolean())
                else -> throw IllegalArgumentException("Atributo inválido: $atributo")
            }
            return repository.save(funcionamentoAtualizado)
        }catch (e: Exception){
            throw IllegalArgumentException("Erro ao atualizar o atributo: $atributo. Verifique o valor fornecido.")
        }
    }


}