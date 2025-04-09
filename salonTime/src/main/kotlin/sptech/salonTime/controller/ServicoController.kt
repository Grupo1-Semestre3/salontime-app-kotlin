package sptech.salonTime.controller

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sptech.salonTime.entidade.Servico
import sptech.salonTime.repository.ServicoRepository

@RestController
@RequestMapping("/servicos")

class ServicoController(val repositorio: ServicoRepository) {

    @GetMapping
    fun get(): ResponseEntity<List<Servico>> {
        val servicos = repositorio.findAll()
        return if (servicos.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(servicos)
        }
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id:Int):
            ResponseEntity<Servico> {
        val servico = repositorio.findById(id)
        return ResponseEntity.of(servico)
    }

    @DeleteMapping("/{id}")
    fun delete (@PathVariable id:Int):
            ResponseEntity<Void> {
        if (repositorio.existsById(id)){
            repositorio.deleteById(id)
            return ResponseEntity.status(204).build()
        }
        return ResponseEntity.status(404).build()
    }

    @PostMapping
    fun post (@RequestBody @Valid novoServico: Servico):
            ResponseEntity<Servico> {
        val servico = repositorio.save(novoServico)
        return ResponseEntity.status(201).body(servico)
    }

    @PutMapping("/{id}")
    fun put (@PathVariable id:Int, @RequestBody servicoAtualizado: Servico):

            ResponseEntity<Servico>{

        if (!repositorio.existsById(id)) {
            return ResponseEntity.status(404).build()
        }
        servicoAtualizado.id = id
        val servico = repositorio.save(servicoAtualizado)
        return ResponseEntity.status(200).body(servico)

    }

//    @PatchMapping("/{id}")
//    fun desativar(@PathVariable id: Int): ResponseEntity<Servico> {
//        val atualizados = repositorio.atualizarStatus(id)
//
//        if (atualizados == 0) {
//            return ResponseEntity.status(404).build()
//        }
//
//        val servicoAtualizado = repositorio.findById(id).get()
//        return ResponseEntity.status(200).body(servicoAtualizado)
//    }

    @PatchMapping("/{id}")
    fun atualizarSimultaneo(@PathVariable id: Int): ResponseEntity<Servico> {
        val atualizados = repositorio.atualizarSimultaneo(id)

        if (atualizados == 0) {
            return ResponseEntity.status(404).build()
        }

        val servicoAtualizado = repositorio.findById(id).get()
        return ResponseEntity.status(200).body(servicoAtualizado)
    }

}





