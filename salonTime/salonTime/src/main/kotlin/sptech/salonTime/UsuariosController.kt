package sptech.salonTime

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/usuarios")
class UsuariosController {

    var usuarios = mutableListOf<Usuarios>()


    @GetMapping
    fun login(@RequestParam(required = false) email:String?,
              @RequestParam(required = false) senha:String?): ResponseEntity<List<Usuarios>> {
        if (email == null && senha == null) {
            if (usuarios.isEmpty()) {
                return ResponseEntity.status(204).build()
            }
            return ResponseEntity.status(200).body(usuarios)
        }

        val filtraEmail = email != null
        val filtraSenha = senha != null

        val listaFiltrada = mutableListOf<Usuarios>()

        if (filtraEmail && filtraSenha) {
            listaFiltrada.addAll(usuarios.filter {
                it.email == email || it.senha == senha
            })
        }
        else if (filtraEmail) {
           listaFiltrada.addAll(usuarios.filter{ it.email == email })
        } else {
            listaFiltrada.addAll(usuarios.filter { it.senha!! == senha!! })
        }

        if (listaFiltrada.isEmpty()) {
            return ResponseEntity.status(204).build()
        }

        return ResponseEntity.status(200).body(listaFiltrada)

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
    fun atualizar(@PathVariable id: Int, @RequestBody usuarioAtualizado: Usuarios): ResponseEntity<String> {
        if (id in 0.. usuarios.size-1) {
            return ResponseEntity.status(404).body("Usuário não encontrado")
        }

        usuarios[id] = usuarioAtualizado
        return ResponseEntity.ok("Usuário atualizado com sucesso!")
    }

    @PatchMapping("/{id}/{novaSenha}")
    fun mudarSenha(@PathVariable id:Int, @PathVariable novaSenha:String): ResponseEntity<Usuarios> {
        if (id in 0..usuarios.size-1) {
            usuarios[id].senha = novaSenha
            return ResponseEntity.status(200).body(usuarios[id])
        }
        return ResponseEntity.status(404).build()
    }

    @PatchMapping("/{id}/{novoEmail}")
    fun mudarEmail(@PathVariable id:Int, @PathVariable novoEmail:String): ResponseEntity<Usuarios> {
        if (id in 0..usuarios.size-1) {
            usuarios[id].email = novoEmail
            return ResponseEntity.status(200).body(usuarios[id])
        }
        return ResponseEntity.status(404).build()
    }

    @PatchMapping("/{id}/{novaCor}")
    fun mudarCorCabelo(@PathVariable id:Int, @PathVariable novaCor:String): ResponseEntity<Usuarios> {
        if (id in 0..usuarios.size-1) {
            usuarios[id].corCabelo = novaCor
            return ResponseEntity.status(200).body(usuarios[id])
        }
        return ResponseEntity.status(404).build()
    }

    @PatchMapping("/{id}/{novoComprimento}")
    fun mudarComprimentoCabelo(@PathVariable id:Int, @PathVariable novoComprimento:String): ResponseEntity<Usuarios> {
        if (id in 0..usuarios.size-1) {
            usuarios[id].tamanhoCabelo = novoComprimento
            return ResponseEntity.status(200).body(usuarios[id])
        }
        return ResponseEntity.status(404).build()
    }
}