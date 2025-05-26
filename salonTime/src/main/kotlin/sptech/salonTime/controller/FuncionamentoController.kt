package sptech.salonTime.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sptech.salonTime.entidade.Funcionamento
import sptech.salonTime.service.FuncionamentoService

@RestController
@RequestMapping("/funcionamento")
class FuncionamentoController (private val service: FuncionamentoService) {

    @GetMapping
    fun listarFuncionamento(): ResponseEntity<List<Funcionamento>> {
        return ResponseEntity.status(200).body(service.listar())
    }

    @PatchMapping("/{id}/{atributo}/{novoValor}")
    fun editarFuncionamento(@PathVariable id: Int, @PathVariable atributo: String, @PathVariable novoValor: String): ResponseEntity<Funcionamento> {
        return ResponseEntity.status(200).body(service.editar(id, atributo, novoValor))
    }

    @PutMapping("/{id}")
    fun editarFuncionamento(@PathVariable id: Int, @RequestBody dados: Funcionamento): ResponseEntity<Funcionamento> {
        return ResponseEntity.status(200).body(service.editarTudo(id, dados))
    }

}