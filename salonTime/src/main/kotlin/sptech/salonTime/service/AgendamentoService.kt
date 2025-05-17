package sptech.salonTime.service

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import sptech.salonTime.entidade.Agendamento
import sptech.salonTime.exception.AgendamentoNaoEncontradoException
import sptech.salonTime.repository.AgendamentoRepository

@Service
class AgendamentoService(private val repository: AgendamentoRepository) {

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

}