package sptech.salonTime.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import sptech.salonTime.entidade.Usuario
import sptech.salonTime.repository.UsuarioRepository

@Service
class UsuarioService {

    @Autowired
    val repository: UsuarioRepository? = null

    fun listar(): List<Usuario> {
        return repository?.findAll() ?: emptyList()
    }

    fun salvar (usuario: Usuario): Usuario {
        return repository?.save(usuario) ?: throw Exception("Erro ao salvar o usuário")
    }

}