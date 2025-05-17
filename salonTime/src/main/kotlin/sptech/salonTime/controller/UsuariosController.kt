package sptech.salonTime.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sptech.salonTime.dto.EmailDto
import sptech.salonTime.dto.SenhaDto
import sptech.salonTime.entidade.Usuario
import sptech.salonTime.repository.UsuarioRepository
import sptech.salonTime.service.UsuarioService
import java.util.*


@RestController
@RequestMapping("/usuarios")
class UsuariosController (val service: UsuarioService) {



    @GetMapping
    fun listar(): ResponseEntity<List<Usuario>> {
        return ResponseEntity.status(200).body(service.listar())
    }
//
//    @GetMapping("/{id}")
//    fun listarPorid(@PathVariable id: Int): ResponseEntity<Usuario> {
//        val usuario = repositorio.findById(id)
//
//        return if (usuario.isPresent) {
//            ResponseEntity.of(repositorio.findById(id))
//        } else {
//            ResponseEntity.notFound().build()
//        }
//    }
//
    @PostMapping
    fun criar(@RequestBody novoUsuario: Usuario): ResponseEntity<Usuario> {

        return ResponseEntity.status(201).body(service.salvar(novoUsuario))
    }
//
//    @DeleteMapping("/{id}")
//    fun excluir(@PathVariable id: Int): ResponseEntity<String> {
//        return if (repositorio.existsById(id)) {
//            repositorio.deleteById(id)
//            ResponseEntity.status(204).build()
//        } else {
//            ResponseEntity.status(404).build()
//        }
//    }
//
//    @PutMapping("/{id}")
//    fun atualizar(@PathVariable id: Int, @RequestBody usuarioAtualizado: Usuario): ResponseEntity<Usuario> {
//        return if (repositorio.existsById(id)) {
//            usuarioAtualizado.id = id
//            val usuarioSalvo = repositorio.save(usuarioAtualizado)
//            ResponseEntity.status(201).body(usuarioSalvo)
//        } else {
//            ResponseEntity.status(404).build()
//        }
//    }
//
//    @PatchMapping("/login/{id}")
//    fun login(@PathVariable id: Int): ResponseEntity<Usuario> {
//        return if (repositorio.existsById(id)) {
//            var usuarioEncontrado = repositorio.findById(id).get()
//            usuarioEncontrado.id = id
//            usuarioEncontrado.login = true
//            val usuarioSalvo = repositorio.save(usuarioEncontrado)
//            ResponseEntity.status(201).body(usuarioSalvo)
//        } else {
//            ResponseEntity.status(404).build()
//        }
//    }
//
//    @PatchMapping("/logoff/{id}")
//    fun logoff(@PathVariable id: Int): ResponseEntity<Usuario> {
//        return if (repositorio.existsById(id)) {
//            var usuarioEncontrado = repositorio.findById(id).get()
//            usuarioEncontrado.id = id
//            usuarioEncontrado.login = false
//            val usuarioSalvo = repositorio.save(usuarioEncontrado)
//            ResponseEntity.status(201).body(usuarioSalvo)
//        } else {
//            ResponseEntity.status(404).build()
//        }
//    }
//
//    @PatchMapping("/mudarSenha/{id}")
//    fun mudarSenha(@PathVariable id: Int, @RequestBody novaSenha:SenhaDto): ResponseEntity<Usuario> {
//        return if (repositorio.existsById(id)) {
//            var usuarioEncontrado = repositorio.findById(id).get()
//            usuarioEncontrado.id = id
//            usuarioEncontrado.senha = novaSenha.senha
//            val usuarioSalvo = repositorio.save(usuarioEncontrado)
//            ResponseEntity.status(201).body(usuarioSalvo)
//        } else {
//            ResponseEntity.status(404).build()
//        }
//    }
//    @PatchMapping("/mudarEmail/{id}")
//    fun mudarEmail(@PathVariable id: Int, @RequestBody novoEmail:EmailDto): ResponseEntity<Usuario> {
//        return if (repositorio.existsById(id)) {
//            var usuarioEncontrado = repositorio.findById(id).get()
//            usuarioEncontrado.id = id
//            usuarioEncontrado.email = novoEmail.email
//            val usuarioSalvo = repositorio.save(usuarioEncontrado)
//            ResponseEntity.status(201).body(usuarioSalvo)
//        } else {
//            ResponseEntity.status(404).build()
//        }
//    }
}