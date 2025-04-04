package sptech.salonTime.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sptech.salonTime.entidade.Usuario
import sptech.salonTime.repository.UsuarioRepository
import java.util.*


@RestController
@RequestMapping("/usuarios")
class UsuariosController (val repositorio: UsuarioRepository){

    @GetMapping
    fun listar(): ResponseEntity<List<Usuario>>{
        return ResponseEntity.ok(repositorio.findAll())
    }

    @GetMapping("/{id}")
    fun listarPorid(@PathVariable id: Int): ResponseEntity<Usuario> {
        val usuario = repositorio.findById(id)

        return if (usuario.isPresent) {
            ResponseEntity.of(repositorio.findById(id))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun criar(@RequestBody novoUsuario: Usuario): ResponseEntity<Usuario> {
        val usuarioSalvo = repositorio.save(novoUsuario)
        return ResponseEntity.status(201).body(usuarioSalvo)
    }

    @DeleteMapping("/{id}")
    fun excluir(@PathVariable id: Int): ResponseEntity<String> {
        return if (repositorio.existsById(id)) {
            repositorio.deleteById(id)
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Int, @RequestBody usuarioAtualizado: Usuario): ResponseEntity<Usuario> {
        return if (repositorio.existsById(id)) {
            usuarioAtualizado.id = id
            val usuarioSalvo = repositorio.save(usuarioAtualizado)
            ResponseEntity.status(201).body(usuarioSalvo)
        } else {
            ResponseEntity.status(404).build()
        }
    }

//    @PatchMapping("/mudarSenha/{id}")
//    fun mudarSenha(@RequestBody novaSenha: Usuarios, @PathVariable id: Int): ResponseEntity<Usuarios> {
//        val usuario = usuarios.find { it.id == id } // Busca o usuário pelo ID real
//
//        return if (usuario != null) {
//            usuario.senha = novaSenha.senha
//            ResponseEntity.ok(usuario)
//        } else {
//            ResponseEntity.status(404).build()
//        }
//    }
//
//    @PatchMapping("/mudarEmail/{id}")
//    fun mudarEmail(@PathVariable id: Int, @RequestBody novoEmail: String): ResponseEntity<Usuarios> {
//        val usuario = usuarios.find { it.id == id } // Busca pelo ID dentro do objeto
//
//        return if (usuario != null) {
//            usuario.email = novoEmail
//            ResponseEntity.ok(usuario)
//        } else {
//            ResponseEntity.status(404).build() // Se não encontrar, retorna 404
//        }
//    }


}