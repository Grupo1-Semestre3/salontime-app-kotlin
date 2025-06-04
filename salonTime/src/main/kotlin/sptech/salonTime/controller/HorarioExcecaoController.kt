package sptech.salonTime.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sptech.salonTime.entidade.HorarioExcecao
import sptech.salonTime.service.HorarioExcecaoService

@RestController
@RequestMapping("/horario-execao")
class HorarioExcecaoController (val service: HorarioExcecaoService){
    @GetMapping
    fun listar() : ResponseEntity<List<HorarioExcecao>> {
        return ResponseEntity.status(200).body(service.listar())
    }

    @PostMapping
    fun inserir(horarioExcecao: HorarioExcecao): ResponseEntity<HorarioExcecao> {
        return ResponseEntity.status(201).body(service.salvar(horarioExcecao))
    }

    @PatchMapping("/{id}")
    fun editar(id: Int, horarioExcecao: HorarioExcecao): ResponseEntity<HorarioExcecao> {
        return ResponseEntity.status(200).body(service.editar(id, horarioExcecao))
    }

    @PatchMapping("/{id}/aberto")
    fun editarAberto(id: Int, aberto: Boolean): ResponseEntity<HorarioExcecao> {
        return ResponseEntity.status(200).body(service.editarAberto(id, aberto))
    }

}