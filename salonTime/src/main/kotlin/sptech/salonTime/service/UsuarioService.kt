package sptech.salonTime.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import sptech.salonTime.dto.EmailDto
import sptech.salonTime.dto.SenhaDto
import sptech.salonTime.entidade.Usuario
import sptech.salonTime.exception.UsuarioNaoEncontradoException
import sptech.salonTime.repository.UsuarioRepository

@Service
class UsuarioService(val repository: UsuarioRepository) {

    fun listar(): List<Usuario> {
        return repository.findAll() ?: emptyList()
    }

    fun salvar(usuario: Usuario): Usuario {
        return repository.save(usuario) ?: throw Exception("Erro ao salvar o usuário")
    }

    fun listarPorId(id: Int): Usuario {
        return repository.findById(id).orElse(null)
    }

    fun excluir(id: Int) {
        if (repository.existsById(id)) {
            repository.deleteById(id)
        }
    }

    fun atualizar(id: Int, usuarioAtualizado: Usuario): Usuario {
        val usuario = repository.findById(id).orElse(null)
        return if (usuario != null) {
            usuarioAtualizado.id = id
            repository.save(usuarioAtualizado)
        } else {
            throw Exception("Usuário não encontrado")
        }
    }

    fun login(id: Int): Usuario {
        val usuario = repository.findById(id).orElse(null)
        return if (usuario != null) {
            usuario.id = id
            usuario.login = true
            repository.save(usuario)
        } else {
            throw Exception("Usuário não encontrado")
        }
    }

    fun logoff(id: Int): Usuario {
        val usuario = repository.findById(id).orElse(null)
        return if (usuario != null) {
            usuario.id = id
            usuario.login = false
            repository.save(usuario)
        } else {
            throw Exception("Usuário não encontrado")
        }
    }

    fun mudarSenha(id: Int, novaSenha: SenhaDto): Usuario {
        var usuario = repository.findById(id).orElse(null)
        return if (usuario != null) {
            usuario.id = id
            usuario.senha = novaSenha.senha
            repository.save(usuario)
        } else {
            throw Exception("Usuário não encontrado")
        }
    }

    fun mudarEmail(id: Int, novoEmail: EmailDto): Usuario {
        var usuario = repository.findById(id).orElse(null)
        return if (usuario != null) {
            usuario.id = id
            usuario.email = novoEmail.email
            repository.save(usuario)
        } else {
            throw Exception("Usuário não encontrado")
        }
    }

    fun atualizarFoto(id: Int, foto: ByteArray): ByteArray {
        val usuario = repository.findById(id).orElseThrow { UsuarioNaoEncontradoException("Usuário não encontrado") }

        usuario.id = id
        usuario.foto = foto
        repository.save(usuario)

        return usuario.foto!!
    }

    fun getFoto(id: Int): ByteArray? {
        val usuario = repository.findById(id).orElseThrow { UsuarioNaoEncontradoException("Usuário não encontrado") }

        return usuario.foto
    }
}