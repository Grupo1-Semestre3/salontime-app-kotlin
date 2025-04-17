package sptech.salonTime.controller

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sptech.salonTime.entidade.Agendamento

import sptech.salonTime.repository.AgendamentoRepository
import sptech.salonTime.repository.StatusAgendamentoRepository
import java.time.LocalDate
import java.time.LocalTime

@RestController
@RequestMapping("/agendamento")

class AgendamentoController(val repository: AgendamentoRepository, val statusAgendamentoRepository: StatusAgendamentoRepository) {
    @GetMapping
    fun get():ResponseEntity<List<Agendamento>>{
        val agendamentos = repository.findAll()

        return if (agendamentos.isEmpty()){
            ResponseEntity.status(204).build()
        }
        else{
            ResponseEntity.status(200).body(agendamentos)
        }
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Int): ResponseEntity<Agendamento> {
        val agendamento = repository.findById(id)

        return if (agendamento.isPresent()) {
            ResponseEntity.status(200).body(agendamento.get())
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @PostMapping
    fun post(@RequestBody @Valid novoAgendamento: Agendamento): ResponseEntity<Agendamento> {
        val existeAgendamento = repository.existeConflitoDeAgendamento(novoAgendamento.data, novoAgendamento.inicio, novoAgendamento.fim)
        if (existeAgendamento>0) {
            return ResponseEntity.status(400).build()
        }
        val agendamento = repository.save(novoAgendamento)
        return ResponseEntity.status(201).body(agendamento)
    }
//O campo de fim não pode ser alterado manualmente, pois ele é um cálculo baseado no campo de início e na duração do serviço. O campo de fim é calculado automaticamente com base no horário de início e na duração do serviço associado ao agendamento. Portanto, não é necessário ou apropriado permitir que o usuário altere manualmente esse campo.
    @PatchMapping("/{atributo}/{id}/{novoValor}")
    fun patch(@PathVariable id: Int, @PathVariable atributo: String, @PathVariable novoValor:String): ResponseEntity<Agendamento> {
        val agendamento = repository.findById(id).orElse(null)

        if (agendamento != null) {
            try {
                val agendamentoAtualizado = when (atributo) {
                    "data" -> agendamento.copy(data = LocalDate.parse(novoValor))
                    "inicio" -> agendamento.copy(inicio = LocalTime.parse(novoValor))
                    "fim" -> agendamento.copy(fim = LocalTime.parse(novoValor))
                    "preco" -> agendamento.copy(preco = novoValor.toDouble())
                    "status" -> agendamento.copy(fkStatus = novoValor.toInt())
                    else -> return ResponseEntity.status(400).build()
                }
                return ResponseEntity.status(200).body(repository.save(agendamentoAtualizado))
            } catch (e: Exception) {
                return ResponseEntity.status(400).build()
            }
        } else {
            return ResponseEntity.status(404).build()
        }
    }

    @GetMapping("proximos")
    fun getProximosAgendamentos():ResponseEntity<List<Agendamento>>{
        val agendamentos = repository.buscarProximosAgendamentos()
        return ResponseEntity.status(201).body(agendamentos)
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Int): ResponseEntity<Void> {
        if (repository.existsById(id)){
            repository.deleteById(id)
            return ResponseEntity.status(204).build()
        }
        return ResponseEntity.status(404).build()
    }
}