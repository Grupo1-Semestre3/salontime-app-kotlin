package sptech.salonTime.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sptech.salonTime.entidade.CupomConfiguracao
import sptech.salonTime.service.CupomConfiguracaoService
import java.util.*

@RestController
@RequestMapping("/cupom-configuracao")

class CupomConfiguracaoController (val service: CupomConfiguracaoService) {

     @PostMapping
     fun criarCupomConfiguracao(@RequestBody cupomConfiguracao: CupomConfiguracao): ResponseEntity<CupomConfiguracao> {
         val savedCupom = service.salvar(cupomConfiguracao)
         return ResponseEntity.status(HttpStatus.CREATED).body(savedCupom)
     }

    @PutMapping
    fun editarCupomConfiguracao(@RequestBody cupomConfiguracao: CupomConfiguracao): ResponseEntity<CupomConfiguracao> {
        val updatedCupom = service.editar(cupomConfiguracao.id ?: throw IllegalArgumentException("ID não pode ser nulo"), cupomConfiguracao)
        return ResponseEntity.ok(updatedCupom)
    }

    @PatchMapping("/intervalo")
    fun editarIntervalo(@RequestBody cupomConfiguracao: CupomConfiguracao): ResponseEntity<CupomConfiguracao> {
        val updatedCupom = service.editarIntervalo(
            cupomConfiguracao.id ?: throw IllegalArgumentException("ID não pode ser nulo"),
            cupomConfiguracao.intervaloAtendimento ?: throw IllegalArgumentException("Intervalo não pode ser nulo")
        )
        return ResponseEntity.ok(updatedCupom)
    }

    @PatchMapping("/porcentagem")
    fun editarPorcentagem(@RequestBody cupomConfiguracao: CupomConfiguracao): ResponseEntity<CupomConfiguracao> {
        val updatedCupom = service.editarPorcentagem(
            cupomConfiguracao.id ?: throw IllegalArgumentException("ID não pode ser nulo"),
            cupomConfiguracao.porcentagemDesconto ?: throw IllegalArgumentException("Porcentagem não pode ser nula")
        )
        return ResponseEntity.ok(updatedCupom)
    }

    @GetMapping
    fun listarTodos(): ResponseEntity<List<CupomConfiguracao>> {
        val cupons = service.listarTodos()
        return ResponseEntity.ok(cupons)
    }
}