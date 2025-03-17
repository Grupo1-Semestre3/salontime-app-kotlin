package sptech.salonTime

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/usuarios")
class UsuariosController {

    var usuarios = mutableListOf<Usuarios>()


//    @GetMapping
//    fun login(@RequestParam(required = false) email:String?,
//              @RequestParam(required = false) senha:String?): ResponseEntity<List<Usuarios>> {
//        if (email == null && senha == null) {
//            if (usuarios.isEmpty()) {
//                return ResponseEntity.status(204).build()
//            }
//            return ResponseEntity.status(200).body(usuarios)
//        }
//
//        val filtraEmail = email != null
//        val filtraSenha = senha != null
//
//        val listaFiltrada = mutableListOf<Usuarios>()
//
//        if (filtraEmail && filtraSenha) {
//            listaFiltrada.addAll(usuarios.filter {
//                it.email == email || it.senha == senha
//            })
//        }
//        else if (filtraEmail) {
//           listaFiltrada.addAll(usuarios.filter{ it.email == email })
//        } else {
//            listaFiltrada.addAll(usuarios.filter { it.senha!! == senha!! })
//        }
//
//        if (listaFiltrada.isEmpty()) {
//            return ResponseEntity.status(204).build()
//        }
//
//        return ResponseEntity.status(200).body(listaFiltrada)
//
//    }


    @GetMapping
    fun listar(): ResponseEntity<List<Usuarios>>{
        return ResponseEntity.status(200).body(usuarios)
    }

    @PostMapping
    fun criar(@RequestBody novoUsuario: Usuarios): ResponseEntity<Usuarios> {
        usuarios.add(novoUsuario)
        return ResponseEntity.status(201).body(novoUsuario)
    }

    @DeleteMapping("/{id}")
    fun excluir(@PathVariable id: Int): ResponseEntity<String> {
        val usuario = usuarios.find { it.id == id }

        return if (usuario != null) {
            usuarios.remove(usuario)
            ResponseEntity.status(200).body("Usuário excluído com sucesso")
        } else {
            ResponseEntity.status(404).body("Usuário não encontrado")
        }
    }

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Int, @RequestBody usuarioAtualizado: Usuarios): ResponseEntity<Usuarios> {
        val usuarioIndex = usuarios.indexOfFirst { it.id == id } // Busca pelo ID real

        return if (usuarioIndex != -1) {
            usuarios[usuarioIndex] = usuarioAtualizado
            ResponseEntity.ok(usuarios[usuarioIndex])
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @PatchMapping("/mudarSenha/{id}")
    fun mudarSenha(@RequestBody novaSenha: Usuarios, @PathVariable id: Int): ResponseEntity<Usuarios> {
        val usuario = usuarios.find { it.id == id } // Busca o usuário pelo ID real

        return if (usuario != null) {
            usuario.senha = novaSenha.senha
            ResponseEntity.ok(usuario)
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @PatchMapping("/mudarEmail/{id}/{novoEmail}")
    fun mudarEmail(@PathVariable id: Int, @PathVariable novoEmail: String): ResponseEntity<Usuarios> {
        val usuario = usuarios.find { it.id == id } // Busca pelo ID dentro do objeto

        return if (usuario != null) {
            usuario.email = novoEmail
            ResponseEntity.ok(usuario)
        } else {
            ResponseEntity.status(404).build() // Se não encontrar, retorna 404
        }
    }


}