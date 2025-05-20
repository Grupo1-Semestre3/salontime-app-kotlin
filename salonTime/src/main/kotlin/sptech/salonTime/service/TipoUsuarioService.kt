package sptech.salonTime.service

import org.springframework.stereotype.Service
import sptech.salonTime.dto.TPUDescricaoDto
import sptech.salonTime.entidade.TipoUsuario
import sptech.salonTime.entidade.Usuario
import sptech.salonTime.repository.TipoUsuarioRepository

@Service
class TipoUsuarioService (val repository: TipoUsuarioRepository) {

    fun listar(): List<TipoUsuario> {
        return repository.findAll() ?: emptyList()
    }

    fun salvar(tipoUsuario: TipoUsuario): TipoUsuario {
        return repository.save(tipoUsuario)
    }

    fun listarPorId(id: Int): TipoUsuario? {
        return repository.findById(id).orElse(null)
    }

    fun excluir(id: Int) {
        if (repository.existsById(id)) {
            repository.deleteById(id)
        }
    }

    fun atualizar(id: Int, descricao: TPUDescricaoDto): TipoUsuario {
        val tipoUsuario = repository.findById(id).orElse(null)
        return if (tipoUsuario != null) {
            tipoUsuario.id = id
            tipoUsuario.descricao = descricao.descricao
            repository.save(tipoUsuario)
        } else {
            throw Exception("Tipo de usuário não encontrado")
        }
    }

}