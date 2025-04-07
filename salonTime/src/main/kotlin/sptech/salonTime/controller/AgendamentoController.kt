package sptech.salonTime.controller

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
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
        val agendamento = repository.save(novoAgendamento)
        return ResponseEntity.status(201).body(agendamento)
    }

    @PatchMapping("/{atributo}/{id}")
    fun patch(@PathVariable id: Int, @PathVariable atributo: String, @RequestBody novoValor: String): ResponseEntity<Agendamento> {
        val agendamento = repository.findById(id).orElse(null)

        if (agendamento != null) {
            try {
                val agendamentoAtualizado = when (atributo) {
                    "data" -> agendamento.copy(data = LocalDate.parse(novoValor))
                    "inicio" -> agendamento.copy(inicio = LocalTime.parse(novoValor))
                    "fim" -> agendamento.copy(fim = LocalTime.parse(novoValor))
                    "preco" -> agendamento.copy(preco = novoValor.toDouble())
                    "status" -> {
                        val statusAgendamento = statusAgendamentoRepository.findById(novoValor.toInt()).orElse(null)
                        if (statusAgendamento != null) {
                            agendamento.copy(status = statusAgendamento)
                        } else {
                            return ResponseEntity.status(400).build()
                        }
                    }
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




}