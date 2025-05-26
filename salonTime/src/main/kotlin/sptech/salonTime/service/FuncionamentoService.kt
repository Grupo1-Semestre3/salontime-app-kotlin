package sptech.salonTime.service

import org.springframework.stereotype.Service
import sptech.salonTime.entidade.DiaSemana
import sptech.salonTime.entidade.Funcionamento
import sptech.salonTime.repository.FuncionamentoRepository
import java.time.LocalTime

@Service
class FuncionamentoService(private val repository: FuncionamentoRepository) {

    fun listar(): List<Funcionamento> {
        return repository.findAll()
    }

    fun editar(id: Int, atributo: String, novoValor: String): Funcionamento {

        val funcionamentoExistente = repository.findById(id)
            .orElseThrow { IllegalArgumentException("Funcionamento não encontrado") }

        try {
            val funcionamentoAtualizado = when (atributo) {
                "inicio" -> funcionamentoExistente.copy(inicio = LocalTime.parse(novoValor))
                "fim" -> funcionamentoExistente.copy(fim = LocalTime.parse(novoValor))
                "aberto" -> funcionamentoExistente.copy(aberto = novoValor.toBoolean())
                "capacidade" -> funcionamentoExistente.copy(capacidade = novoValor.toInt())
                else -> throw IllegalArgumentException("Atributo inválido: $atributo")
            }
            return repository.save(funcionamentoAtualizado)
        }catch (e: Exception){
            throw IllegalArgumentException("Erro ao atualizar o atributo: $atributo. Verifique o valor fornecido.")
        }
    }

    fun editarTudo(id: Int, dados:Funcionamento): Funcionamento? {

        val funcionamentoExistente = repository.findById(id)
            .orElseThrow { IllegalArgumentException("Funcionamento não encontrado") }

        val funcionamentoAtualizado = funcionamentoExistente.copy(
            inicio = dados.inicio,
            fim = dados.fim,
            aberto = dados.aberto,
            capacidade = dados.capacidade,
            diaSemana = dados.diaSemana
        )

        return repository.save(funcionamentoAtualizado)

    }


}