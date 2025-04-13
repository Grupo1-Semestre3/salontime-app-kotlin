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
    fun getAtivo(): ResponseEntity<List<Servico>> {
        val servicos = repositorio.findAllByStatus("ATIVO")
        return if (servicos.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(servicos)
        }
    }

    @GetMapping("/{id}")
    fun getbYId(@PathVariable id:Int):
            ResponseEntity<Servico> {
        val servico = repositorio.findById(id)
        return ResponseEntity.of(servico)
    }

    //Seria o deletar
    @PatchMapping("status-desativar/{id}")
    fun delete (@PathVariable id:Int):
            ResponseEntity<Void> {
        if (repositorio.existsById(id)){
            repositorio.mudarStatus(id)
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


    @PatchMapping("ativar-simultaneo/{id}")
    fun ativarSimultaneo(@PathVariable id: Int): ResponseEntity<Servico> {

        if (repositorio.existsById(id)){
            val atualizados = repositorio.ativarSimultaneo(id)
            val servicoAtualizado = repositorio.findById(id).get()
            return ResponseEntity.status(200).body(servicoAtualizado)
        }else{
            return ResponseEntity.status(404).build()
        }
    }

    @PatchMapping("desativar-simultaneo/{id}")
    fun desativarSimultaneo(@PathVariable id: Int): ResponseEntity<Servico> {

        if (repositorio.existsById(id)){
            val atualizados = repositorio.desativarSimultaneo(id)
            val servicoAtualizado = repositorio.findById(id).get()
            return ResponseEntity.status(200).body(servicoAtualizado)
        }else{
            return ResponseEntity.status(404).build()
        }
    }

}





