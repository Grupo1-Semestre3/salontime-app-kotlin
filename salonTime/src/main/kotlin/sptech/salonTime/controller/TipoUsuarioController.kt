package sptech.salonTime.controller

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sptech.salonTime.dto.TPUDescricaoDto
import sptech.salonTime.entidade.TipoUsuario
import sptech.salonTime.service.TipoUsuarioService
import java.util.*

@RestController
@RequestMapping("/tipo-usuario")
class TipoUsuarioController(
    val service: TipoUsuarioService
) {
    @GetMapping
    fun listar(): ResponseEntity<List<TipoUsuario>> {
        return ResponseEntity.status(200).body(service.listar())
    }

    @PostMapping
    fun criar(@RequestBody @Valid tipoUsuario: TipoUsuario): ResponseEntity<TipoUsuario> {
        return ResponseEntity.status(201).body(service.salvar(tipoUsuario))
    }

    @GetMapping("/{id}")
    fun listarPorId(@PathVariable id: Int): ResponseEntity<TipoUsuario> {
        val tipoUsuario = service.listarPorId(id)
        return if (tipoUsuario != null) {
            ResponseEntity.status(200).body(tipoUsuario)
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @DeleteMapping("/{id}")
    fun excluir(@PathVariable id: Int): ResponseEntity<String> {
        val tipoUsuario = service.listarPorId(id)
        return if (tipoUsuario != null) {
            service.excluir(id)
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @PatchMapping("/mudarDescricao/{id}")
    fun mudarDescricao(
        @PathVariable id: Int,
        @RequestBody @Valid descricao: TPUDescricaoDto
    ): ResponseEntity<TipoUsuario> {
        val tipoUsuario = service.listarPorId(id)
        return if (tipoUsuario != null) {
            service.atualizar(id, descricao)
            ResponseEntity.status(201).body(tipoUsuario)
        } else {
            ResponseEntity.status(404).build()
        }
    }

}