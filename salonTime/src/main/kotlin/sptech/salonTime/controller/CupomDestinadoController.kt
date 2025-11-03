package sptech.salonTime.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sptech.salonTime.dto.CupomDestinadoDto
import sptech.salonTime.entidade.CupomDestinado
import sptech.salonTime.service.CupomDestinadoService


@RestController
@RequestMapping("/cupom-destinado")
class CupomDestinadoController(val service: CupomDestinadoService) {

    @GetMapping
    fun listar(): ResponseEntity<List<CupomDestinadoDto>> {
        return ResponseEntity.status(200).body(service.listar())
    }

    @GetMapping("/lista/{idUsuario}")
    fun listarPorIdUsuario(@PathVariable idUsuario: Int): ResponseEntity<List<CupomDestinadoDto>> {
        return ResponseEntity.status(200).body(service.listarPorIdUsuario(idUsuario))
    }

    @PostMapping
    fun inserir(@RequestBody cupomDestinado: CupomDestinado): ResponseEntity<CupomDestinadoDto>{
        return ResponseEntity.status(201).body(service.salvar(cupomDestinado))
    }

    @PutMapping("/{id}")
    fun editar(@PathVariable id:Int, @RequestBody cupomDestinado: CupomDestinado): ResponseEntity<CupomDestinadoDto> {
        return ResponseEntity.status(200).body(service.editar(id, cupomDestinado))
    }

    @PatchMapping("/{id}/{usado}")
    fun atualizarUsado(@PathVariable id: Int, @PathVariable usado: Boolean): ResponseEntity<CupomDestinadoDto> {
        return ResponseEntity.status(200).body(service.atualizarUsado(id, usado))
    }

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Int): ResponseEntity<Void>{
        service.deletar(id)
        return ResponseEntity.status(204).build()
    }






}



