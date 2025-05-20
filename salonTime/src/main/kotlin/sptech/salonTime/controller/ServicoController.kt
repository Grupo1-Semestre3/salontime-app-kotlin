package sptech.salonTime.controller

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sptech.salonTime.entidade.Servico
import sptech.salonTime.repository.ServicoRepository
import sptech.salonTime.service.ServicoService

@RestController
@RequestMapping("/servicos")

class ServicoController(val service: ServicoService) {

    @GetMapping
    fun listarAtivos(): ResponseEntity<List<Servico>> {
        val servicos = service.listarAtivos()
        return if (servicos.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(servicos)
        }
    }

    @GetMapping("/{id}")
    fun listarPorId(@PathVariable id:Int): ResponseEntity<Servico> {
        val servico = service.listarPorId(id)
        return if (servico != null) {
            ResponseEntity.status(200).body(servico)
        } else {
            ResponseEntity.status(404).build()
        }
    }

    //Seria o deletar
    @PatchMapping("status-desativar/{id}")
    fun desativar(@PathVariable id:Int): ResponseEntity<Void> {
        if (service.desativar(id)){
            return ResponseEntity.status(204).build()
        }
        return ResponseEntity.status(404).build()
    }

    @PatchMapping("status-ativar/{id}")
    fun ativar(@PathVariable id:Int): ResponseEntity<Void> {
        if (service.ativar(id)){
            return ResponseEntity.status(204).build()
        }
        return ResponseEntity.status(404).build()
    }

    @PostMapping
    fun criar(@RequestBody @Valid novoServico: Servico): ResponseEntity<Servico> {
        val servico = service.criar(novoServico)
        return ResponseEntity.status(201).body(servico)
    }

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id:Int, @RequestBody servicoAtualizado: Servico):
            ResponseEntity<Servico>{
        val servico = service.listarPorId(id)
        return if (servico != null) {
            service.atualizar(id, servicoAtualizado)
            ResponseEntity.status(200).body(servicoAtualizado)
        } else {
            ResponseEntity.status(404).build()
        }
    }


    @PatchMapping("ativar-simultaneo/{id}")
    fun ativarSimultaneo(@PathVariable id: Int): ResponseEntity<Servico> {
        if (service.ativarSimultaneo(id)){
            return ResponseEntity.status(204).build()
        }
        return ResponseEntity.status(404).build()
    }

    @PatchMapping("desativar-simultaneo/{id}")
    fun desativarSimultaneo(@PathVariable id: Int): ResponseEntity<Servico> {
        if (service.desativarSimultaneo(id)){
            return ResponseEntity.status(204).build()
        }
        return ResponseEntity.status(404).build()
    }

}





