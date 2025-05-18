package sptech.salonTime.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sptech.salonTime.entidade.InfoSalao
import sptech.salonTime.service.InfoSalaoService

@RestController
@RequestMapping("/info-salao")
class InfoSalaoController (private val service: InfoSalaoService) {

    @GetMapping
    fun listarInfoSalao(): ResponseEntity<List<InfoSalao>> {
        return ResponseEntity.status(200).body(service.listar())
    }

    @PatchMapping("/{id}/{atributo}/{novoValor}")
    fun editarInfoSalao(@PathVariable id: Int, @PathVariable atributo: String, @PathVariable novoValor: String): ResponseEntity<InfoSalao> {
        return ResponseEntity.status(200).body(service.editar(id, atributo, novoValor))
    }



}