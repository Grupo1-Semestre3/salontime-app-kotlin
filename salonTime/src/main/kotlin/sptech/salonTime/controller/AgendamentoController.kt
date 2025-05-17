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
import sptech.salonTime.service.AgendamentoService
import sptech.salonTime.service.UsuarioService
import java.time.LocalDate
import java.time.LocalTime

@RestController
@RequestMapping("/agendamento")

class AgendamentoController(val repository: AgendamentoRepository, val statusAgendamentoRepository: StatusAgendamentoRepository, val service: AgendamentoService) {
    @GetMapping
    fun get():ResponseEntity<List<Agendamento>>{
        return ResponseEntity.status(200).body(service.listar())
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Int): ResponseEntity<Agendamento> {
        return ResponseEntity.status(200).body(service.listarPorId(id))
    }

    @PostMapping
    fun post(@RequestBody @Valid agendamento: Agendamento): ResponseEntity<Agendamento> {
        return ResponseEntity.status(201).body(service.cadastrar(agendamento))
    }

//O campo de fim não pode ser alterado manualmente, pois ele é um cálculo baseado no campo de início e na duração do serviço. O campo de fim é calculado automaticamente com base no horário de início e na duração do serviço associado ao agendamento. Portanto, não é necessário ou apropriado permitir que o usuário altere manualmente esse campo.
    @PatchMapping("/{atributo}/{id}/{novoValor}")
    fun patch(@PathVariable id: Int, @PathVariable atributo: String, @PathVariable novoValor:String): ResponseEntity<Agendamento> {
        return ResponseEntity.status(200).body(service.atualizarAtributo(id, atributo, novoValor))
    }

    @GetMapping("/proximos")
    fun getProximosAgendamentos():ResponseEntity<List<Agendamento>>{
        return ResponseEntity.status(201).body(service.buscarProximosAgendamentos())
    }

}