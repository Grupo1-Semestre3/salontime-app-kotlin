package sptech.salonTime.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sptech.salonTime.dto.avalicao.AtualizarAvaliacaoDto
import sptech.salonTime.dto.avalicao.CadastroAvaliacaoDto
import sptech.salonTime.entidade.Avaliacao
import sptech.salonTime.service.AvalicaoService

@RestController
@RequestMapping("/avaliacao")
class AvaliacaoController (private val service: AvalicaoService){

    @GetMapping
    fun get(): ResponseEntity<List<Avaliacao>> {
        return ResponseEntity.status(200).body(service.listar())
    }

    @GetMapping
    fun getById(id: Int): ResponseEntity<Avaliacao> {
        return ResponseEntity.status(200).body(service.buscarPorId(id))
    }

    @PostMapping
    fun post(avaliacao: CadastroAvaliacaoDto): ResponseEntity<Avaliacao> {
        return ResponseEntity.status(201).body(service.cadastrar(avaliacao))
    }

    @PutMapping
    fun put(id: Int, avaliacao: AtualizarAvaliacaoDto): ResponseEntity<Avaliacao> {
        return ResponseEntity.status(200).body(service.atualizar(id, avaliacao))
    }

}