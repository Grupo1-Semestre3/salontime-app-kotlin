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
import sptech.salonTime.dto.AgendamentoDto
import sptech.salonTime.dto.CadastroAgendamentoDto
import sptech.salonTime.entidade.Agendamento

import sptech.salonTime.repository.AgendamentoRepository
import sptech.salonTime.repository.StatusAgendamentoRepository
import sptech.salonTime.service.AgendamentoService
import sptech.salonTime.service.UsuarioService
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@RestController
@RequestMapping("/agendamento")

class AgendamentoController(val repository: AgendamentoRepository, val statusAgendamentoRepository: StatusAgendamentoRepository, val service: AgendamentoService) {
    @GetMapping
    fun get(): ResponseEntity<List<AgendamentoDto?>> {
        return ResponseEntity.status(200).body(service.listar())
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Int): ResponseEntity<AgendamentoDto> {
        return ResponseEntity.status(200).body(service.listarPorId(id))
    }

    @PostMapping
    fun post(@RequestBody @Valid agendamento: CadastroAgendamentoDto): ResponseEntity<AgendamentoDto> {
        return ResponseEntity.status(201).body(service.cadastrar(agendamento))
    }

//O campo de fim não pode ser alterado manualmente, pois ele é um cálculo baseado no campo de início e na duração do serviço. O campo de fim é calculado automaticamente com base no horário de início e na duração do serviço associado ao agendamento. Portanto, não é necessário ou apropriado permitir que o usuário altere manualmente esse campo.
    @PatchMapping("/{atributo}/{id}/{novoValor}")
    fun patch(@PathVariable id: Int, @PathVariable atributo: String, @PathVariable novoValor:String): ResponseEntity<AgendamentoDto> {
        return ResponseEntity.status(200).body(service.atualizarAtributo(id, atributo, novoValor))
    }

    @GetMapping("/proximos-funcionario/{idFuncionario}")
    fun getProximosAgendamentos(@PathVariable idFuncionario: Int):ResponseEntity<List<AgendamentoDto>>{
        return ResponseEntity.status(200).body(service.buscarProximosAgendamentosPorFuncionario(idFuncionario))
    }

    @GetMapping("/proximo-usuario/{id}")
    fun getProximosAgendamentosPorUsuario(@PathVariable id: Int):ResponseEntity<AgendamentoDto>{
        return ResponseEntity.status(200).body(service.buscarProximosAgendamentosPorUsuario(id))
    }

    @GetMapping("/passados-funcionario/{idFuncionario}")
    fun getAgendamentosPassadosPorFuncionario(@PathVariable idFuncionario: Int):ResponseEntity<List<AgendamentoDto>>{
        return ResponseEntity.status(200).body(service.buscarAgendamentosPassadosPorFuncionario(idFuncionario))
    }

    @GetMapping("/passados-usuario/{id}")
    fun getAgendamentosPassadosPorUsuario(@PathVariable id: Int):ResponseEntity<List<AgendamentoDto>>{
        return ResponseEntity.status(200).body(service.buscarAgendamentosPassadosPorUsuario(id))
    }

    @GetMapping("/cancelados")
    fun getAgendamentosCancelados(): ResponseEntity<List<AgendamentoDto>> {
        return ResponseEntity.status(200).body(service.buscarAgendamentosCancelados())
    }

    @PatchMapping("/status/{id}/{status}")
    fun patchStatus(@PathVariable id: Int, @PathVariable status: Int): ResponseEntity<AgendamentoDto> {
        return ResponseEntity.status(200).body(service.atualizarStatus(id, status))
    }

    @PatchMapping("/valor/{id}/{valor}")
    fun patchValor(@PathVariable id: Int, @PathVariable valor: Double): ResponseEntity<AgendamentoDto> {
        return ResponseEntity.status(200).body(service.atualizarValor(id, valor))
    }

}