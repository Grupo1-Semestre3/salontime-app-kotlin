package sptech.salonTime.controller

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sptech.salonTime.dto.ListagemServicoDto
import sptech.salonTime.dto.ServicoDto
import sptech.salonTime.entidade.Servico
import sptech.salonTime.repository.ServicoRepository
import sptech.salonTime.service.ServicoService

@RestController
@RequestMapping("/servicos")

class ServicoController(val service: ServicoService) {

    @GetMapping
    fun listarAtivos(): ResponseEntity<List<ServicoDto>> {
        val servicos = service.listarAtivos()
        return ResponseEntity.status(200).body(servicos)
    }

    @GetMapping("/listar-desativados")
    fun listarDesativados(): ResponseEntity<List<ServicoDto>> {
        return ResponseEntity.status(200).body(service.listarDesativados())
    }

    @GetMapping("/{id}")
    fun listarPorId(@PathVariable id:Int): ResponseEntity<ServicoDto> {
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
    fun atualizar(@PathVariable id:Int, @RequestBody servicoAtualizado: Servico): ResponseEntity<Servico>{
        return ResponseEntity.status(200).body(service.atualizar(id, servicoAtualizado))
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

    @PatchMapping("/foto/{id}")
    fun patchFoto(@PathVariable id:Int, @RequestBody foto: ByteArray): ResponseEntity<ByteArray> {
        return ResponseEntity.status(200).body(service.atualizarFoto(id, foto));
    }

    @GetMapping(
        value = ["/foto/{id}"],
        produces = ["image/png", "image/jpeg", "image/jpg"]
    )

    fun getFoto(
        @PathVariable id:Int
    ): ResponseEntity<ByteArray> {
        return ResponseEntity.status(200).body(service.getFoto(id))
    }

}





