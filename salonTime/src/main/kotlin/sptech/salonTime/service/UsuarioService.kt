package sptech.salonTime.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import sptech.salonTime.dto.*
import sptech.salonTime.entidade.TipoUsuario
import sptech.salonTime.entidade.Usuario
import sptech.salonTime.exception.UsuarioNaoEncontradoException
import sptech.salonTime.mapper.UsuarioMapper
import sptech.salonTime.repository.UsuarioRepository
import java.time.LocalDateTime

@Service
class UsuarioService(val repository: UsuarioRepository) {

    fun listar(): List<Usuario> {
        return repository.findAll() ?: emptyList()
    }

    fun salvarUsuario(usuario: CadastroUsuarioDto): UsuarioPublicoDto {


        val usuarioEntity = UsuarioMapper.toEntity(usuario)
            ?: throw Exception("Erro ao mapear o usuário")

        usuarioEntity.tipoUsuario = TipoUsuario(2, "CLIENTE")// Definindo o tipo de usuário como FUNCIONARIO
        usuarioEntity.dataCriacao = LocalDateTime.now()  // Definindo a data de criação do usuário
        val usuarioSalvo = repository.save(usuarioEntity)

        return UsuarioMapper.toDto(usuarioSalvo)

    }

    fun salvarFuncionario(usuario: CadastroUsuarioDto): UsuarioPublicoDto {


        val usuarioEntity = UsuarioMapper.toEntity(usuario)
            ?: throw Exception("Erro ao mapear o usuário")

        usuarioEntity.tipoUsuario = TipoUsuario(3, "FUNCIONARIO")// Definindo o tipo de usuário como FUNCIONARIO
        usuarioEntity.dataCriacao = LocalDateTime.now()
        val usuarioSalvo = repository.save(usuarioEntity)

        return UsuarioMapper.toDto(usuarioSalvo)

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

    //Funcção de verificação de email para CHATBOT

    fun verificarEmail(email: String): Usuario {
        val usuario = repository.findByEmail(email)

       return if (usuario != null) {
            usuario
        } else {
           throw Exception("Usuário não encontrado com o email: $email")
        }

    }

}