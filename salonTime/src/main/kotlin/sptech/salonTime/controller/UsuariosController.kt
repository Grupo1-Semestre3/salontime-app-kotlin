package sptech.salonTime.controller

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sptech.salonTime.dto.*
import sptech.salonTime.entidade.Usuario
import sptech.salonTime.service.UsuarioService
import java.util.*


@RestController
@RequestMapping("/usuarios")
class UsuariosController(
    val service: UsuarioService
) {
    @GetMapping
    fun listar(): ResponseEntity<List<Usuario>> {
        return ResponseEntity.status(200).body(service.listar())
    }

    @GetMapping("/{id}")
    fun listarPorid(@PathVariable id: Int): ResponseEntity<Usuario> {
        val usuario = service.listarPorId(id)
        return if (usuario != null) {
            ResponseEntity.status(200).body(usuario)
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @PostMapping("/cadastro")
    fun criar(@RequestBody @Valid novoUsuario: CadastroUsuarioDto): ResponseEntity<UsuarioPublicoDto> {
        return ResponseEntity.status(201).body(service.salvarUsuario(novoUsuario))
    }
    @PostMapping
    fun criarFuncionario(@RequestBody @Valid novoUsuario: CadastroUsuarioDto): ResponseEntity<UsuarioPublicoDto> {
        return ResponseEntity.status(201).body(service.salvarFuncionario(novoUsuario))
    }

    @DeleteMapping("/{id}")
    fun excluir(@PathVariable id: Int): ResponseEntity<String> {
        val usuario = service.listarPorId(id)
        return if (!usuario.equals(null)) {
            service.excluir(id)
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Int, @RequestBody @Valid usuarioAtualizado: Usuario): ResponseEntity<Usuario> {
    val usuario = service.listarPorId(id)
        return if (!usuario.equals(null)) {
            service.atualizar(id, usuarioAtualizado)
            ResponseEntity.status(201).body(usuarioAtualizado)
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @PatchMapping("/login/{id}")
    fun login(@PathVariable id: Int): ResponseEntity<Usuario> {
        val usuario = service.listarPorId(id)
        return if (!usuario.equals(null)) {
            service.login(id)
            ResponseEntity.status(201).body(usuario)
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @PatchMapping("/logoff/{id}")
    fun logoff(@PathVariable id: Int): ResponseEntity<Usuario> {
        val usuario = service.listarPorId(id)
        return if (!usuario.equals(null)) {
            service.logoff(id)
            ResponseEntity.status(201).body(usuario)
        } else {
            ResponseEntity.status(404).build()
        }
    }
//
    @PatchMapping("/mudarSenha/{id}")
    fun mudarSenha(@PathVariable id: Int, @RequestBody novaSenha: SenhaDto): ResponseEntity<Usuario> {
        val usuario = service.listarPorId(id)
        return if (!usuario.equals(null)) {
            service.mudarSenha(id, novaSenha)
            ResponseEntity.status(201).body(usuario)
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @PatchMapping("/mudarEmail/{id}")
    fun mudarEmail(@PathVariable id: Int, @RequestBody novoEmail: EmailDto): ResponseEntity<Usuario> {
        val usuario = service.listarPorId(id)
        return if (!usuario.equals(null)) {
            service.mudarEmail(id, novoEmail)
            ResponseEntity.status(201).body(usuario)
        } else {
            ResponseEntity.status(404).build()
        }
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

    @GetMapping("/verificar-email/{email}")
    fun verificarEmail(@PathVariable email: String): ResponseEntity<Boolean> {
       return ResponseEntity.status(200).body(service.verificarEmail(email))
    }

}