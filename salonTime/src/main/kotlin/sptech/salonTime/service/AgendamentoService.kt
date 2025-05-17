package sptech.salonTime.service

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import sptech.salonTime.entidade.Agendamento
import sptech.salonTime.exception.AgendamentoNaoEncontradoException
import sptech.salonTime.exception.AtributoInvalidoAoAtualizarException
import sptech.salonTime.exception.ConflitoDeAgendamentoException
import sptech.salonTime.repository.AgendamentoRepository
import sptech.salonTime.repository.StatusAgendamentoRepository
import java.time.LocalDate
import java.time.LocalTime

@Service
class AgendamentoService(private val repository: AgendamentoRepository, val statusAgendamentoRepository: StatusAgendamentoRepository) {

    fun listar(): List<Agendamento> {
        val agendamentos = repository.findAll()

        return agendamentos
    }

    fun listarPorId(id: Int ): Agendamento{
        val agendamento = repository.findById(id)

        return repository.findById(id).orElseThrow {
            AgendamentoNaoEncontradoException("Agendamento com ID $id não encontrado.")
        }
    }

    fun cadastrar(agendamento: Agendamento): Agendamento {
        val existeAgendamento = repository.existeConflitoDeAgendamento(agendamento.data, agendamento.inicio, agendamento.fim)

        if (existeAgendamento > 0) {
            throw ConflitoDeAgendamentoException("Já existe um agendamento nesse horário.")
        }

        val agendamentoSalvo = repository.save(agendamento)

        return agendamentoSalvo
    }

    fun atualizarAtributo(id: Int, atributo: String, novoValor: String): Agendamento {
        val agendamento = repository.findById(id).orElseThrow{AgendamentoNaoEncontradoException("Agendamento com ID $id não encontrado.") }


            try {
                val agendamentoAtualizado = when (atributo) {
                    "data" -> agendamento.copy(data = LocalDate.parse(novoValor))
                    "inicio" -> agendamento.copy(inicio = LocalTime.parse(novoValor))
                    "fim" -> agendamento.copy(fim = LocalTime.parse(novoValor))
                    "preco" -> agendamento.copy(preco = novoValor.toDouble())
                    "status" -> agendamento.copy(statusAgendamento = novoValor.toInt().let { statusAgendamentoRepository.findById(it).orElse(null) })
                    else -> throw AtributoInvalidoAoAtualizarException("Atributo inválido: $atributo")
                }
                return repository.save(agendamentoAtualizado)
            } catch (e: Exception) {
                throw AtributoInvalidoAoAtualizarException("Erro ao atualizar o atributo: $atributo. Verifique o valor fornecido.")
            }

    }

    fun buscarProximosAgendamentos(): List<Agendamento>? {
        return repository.buscarProximosAgendamentos()
    }

}