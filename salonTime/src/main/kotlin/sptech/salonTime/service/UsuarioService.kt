package sptech.salonTime.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import sptech.salonTime.dto.*
import sptech.salonTime.entidade.TipoUsuario
import sptech.salonTime.entidade.Usuario
import sptech.salonTime.exception.*
import sptech.salonTime.mapper.UsuarioMapper
import sptech.salonTime.repository.TipoUsuarioRepository
import sptech.salonTime.repository.UsuarioRepository
import java.time.LocalDateTime

@Service
class UsuarioService(val repository: UsuarioRepository, val tipoUsuarioRepository: TipoUsuarioRepository) {

    fun listar(): List<Usuario> {
        return repository.findAllByAtivoTrue() ?: emptyList()
    }

    fun salvarUsuario(usuario: CadastroUsuarioDto): UsuarioPublicoDto {


        val usuarioEntity = UsuarioMapper.toEntity(usuario)
            ?: throw Exception("Erro ao mapear o usuário")

        usuarioEntity.tipoUsuario = TipoUsuario(2, "CLIENTE")// Definindo o tipo de usuário como FUNCIONARIO
        usuarioEntity.dataCriacao = LocalDateTime.now()  // Definindo a data de criação do usuário

        val usuario = repository.findByEmail(usuario.email)

        if (usuario != null) {
            throw UsuarioJaCadastradoException("Email já cadastrado")
        }

        val usuarioSalvo = repository.save(usuarioEntity)

        return UsuarioMapper.toDto(usuarioSalvo)

    }

    fun listarFuncionarios(): List<UsuarioPublicoDto> {
        val funcionarios = repository.findAllByTipoUsuarioIdAndAtivoTrue(3) // 3 é o ID do tipo de usuário "FUNCIONARIO"
        return funcionarios.mapNotNull { UsuarioMapper.toDto(it) }
    }

    fun listarClientes(): List<UsuarioPublicoDto> {
        val clientes = repository.findAllByTipoUsuarioIdAndAtivoTrue(2) // 2 é o ID do tipo de usuário "CLIENTE"
        return clientes.mapNotNull { UsuarioMapper.toDto(it) }
    }


    fun salvarFuncionario(usuario: CadastroUsuarioDto): UsuarioPublicoDto {


        val usuarioEntity = UsuarioMapper.toEntity(usuario)
            ?: throw Exception("Erro ao mapear o usuário")

        usuarioEntity.tipoUsuario = TipoUsuario(3, "FUNCIONARIO")// Definindo o tipo de usuário como FUNCIONARIO
        usuarioEntity.dataCriacao = LocalDateTime.now()

        val usuario = repository.findByEmail(usuario.email)

        if (usuario != null) {
            throw UsuarioJaCadastradoException("Email já cadastrado")
        }

        val usuarioSalvo = repository.save(usuarioEntity)

        return UsuarioMapper.toDto(usuarioSalvo)

    }

    fun listarPorId(id: Int): Usuario {
        return repository.findById(id).orElseThrow{UsuarioNaoEncontradoException("Usuário não encontrado")}
    }

    fun excluir(id: Int) {
        val usuario = repository.findById(id).orElseThrow{UsuarioNaoEncontradoException("Usuário não encontrado")}
        usuario.ativo = false
        usuario.id = id
        repository.save(usuario)
    }

    fun atualizar(id: Int, usuarioAtualizado: Usuario): Usuario {
        val usuario = repository.findById(id).orElseThrow{UsuarioNaoEncontradoException("Usuário não encontrado")}
        val tipoUsuario = usuarioAtualizado.tipoUsuario?.let { tipoUsuarioRepository.findById(it.id).orElseThrow { TipoUsuarioNaoEncontradoException("Tipo de usuário não encontrado") } }

        return if (usuario != null) {
            usuarioAtualizado.id = id
            repository.save(usuarioAtualizado)
        } else {
            throw Exception("Usuário não encontrado")
        }
    }

    fun login(dado: LoginDto): Usuario {

        val resposta = repository.login(dado.email, dado.senha)

        return if (resposta != null) {
            resposta.login = true
            repository.save(resposta)
        } else {
            throw Exception("Usuário ou senha inválidos")
        }

    }

    fun logoff(id: Int): Usuario {
        val usuario = repository.findById(id).orElseThrow { UsuarioNaoEncontradoException("Usuário não encontrado") }
        return if (usuario != null) {
            usuario.id = id
            usuario.login = false
            repository.save(usuario)
        } else {
            throw Exception("Usuário não encontrado")
        }
    }

    fun mudarSenha(id: Int, senha: SenhaDto): Usuario {
        val usuario = repository.findById(id).orElseThrow { UsuarioNaoEncontradoException("Usuário não encontrado") }
        usuario.id = id

        if (senha.senhaAtual == usuario.senha) {
            usuario.senha = senha.novaSenha
        } else {
            throw UsuarioSenhaErradaException("Senha atual incorreta")
        }

        return repository.save(usuario)

    }

    fun mudarEmail(id: Int, novoEmail: EmailDto): Usuario {
        val usuario = repository.findById(id).orElseThrow { UsuarioNaoEncontradoException("Usuário não encontrado") }
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