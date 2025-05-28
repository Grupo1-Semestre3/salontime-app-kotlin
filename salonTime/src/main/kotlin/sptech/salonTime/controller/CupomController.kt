package sptech.salonTime.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sptech.salonTime.entidade.Cupom
import sptech.salonTime.service.CupomService
import java.time.LocalTime

@RestController
@RequestMapping("/cupons")
class CupomController(val service: CupomService) {

    @PostMapping
    fun criar(@RequestBody cupom: Cupom): ResponseEntity<Cupom> {
        val criado = service.criar(cupom)
        return ResponseEntity.status(201).body(criado)
    }

    @GetMapping
    fun listarAtivos(): ResponseEntity<List<Cupom>> {
        val ativos = service.listarAtivos()
        return if (ativos.isNotEmpty()) ResponseEntity.ok(ativos)
        else ResponseEntity.noContent().build()
    }

    @PatchMapping("/{id}")
    fun atualizar(@PathVariable id: Int, @RequestBody cupom: Cupom): ResponseEntity<Cupom> {
        val atualizado = service.atualizar(id, cupom)
        return if (atualizado != null) ResponseEntity.ok(atualizado)
        else ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Int): ResponseEntity<Void> {
        return if (service.deletar(id)) ResponseEntity.noContent().build()
        else ResponseEntity.notFound().build()
    }
}