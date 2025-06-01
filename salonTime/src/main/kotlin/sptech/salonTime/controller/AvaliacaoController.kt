package sptech.salonTime.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sptech.salonTime.dto.AvaliacaoDto
import sptech.salonTime.dto.avalicao.AtualizarAvaliacaoDto
import sptech.salonTime.dto.avalicao.CadastroAvaliacaoDto
import sptech.salonTime.entidade.Avaliacao
import sptech.salonTime.service.AvaliacaoService

@RestController
@RequestMapping("/avaliacao")
class AvaliacaoController (private val service: AvaliacaoService){

    @GetMapping
    fun get(): ResponseEntity<List<AvaliacaoDto>> {
        return ResponseEntity.status(200).body(service.listar())
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Int): ResponseEntity<AvaliacaoDto> {
        return ResponseEntity.status(200).body(service.buscarPorId(id))
    }

    @PostMapping
    fun post(@RequestBody avaliacao: CadastroAvaliacaoDto): ResponseEntity<Avaliacao> {
        return ResponseEntity.status(201).body(service.cadastrar(avaliacao))
    }

    @PutMapping("/{id}")
    fun put(@PathVariable id: Int,@RequestBody avaliacao: AtualizarAvaliacaoDto): ResponseEntity<AvaliacaoDto> {
        return ResponseEntity.status(200).body(service.atualizar(id, avaliacao))
    }

}