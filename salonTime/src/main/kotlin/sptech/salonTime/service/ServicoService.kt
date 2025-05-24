package sptech.salonTime.service

import org.springframework.stereotype.Service
import sptech.salonTime.dto.ServicoDto
import sptech.salonTime.entidade.Servico
import sptech.salonTime.repository.ServicoRepository
import kotlin.math.E

@Service
class ServicoService (val repository: ServicoRepository) {

    fun listarAtivos(): List<ServicoDto> {
        return  repository.listarTodosAtivosComMedia() ?: emptyList()
    }

    fun listarPorId(id: Int): ServicoDto? {
        return repository.listarPorIdComMedia(id)
    }

    fun desativar(id: Int): Boolean {
        val servico = repository.findById(id).orElse(null)
        return if (servico != null) {
            servico.status = "INATIVO"
            repository.save(servico)
            true
        } else {
            false
        }
    }

    fun ativar(id: Int): Boolean {
        val servico = repository.findById(id).orElse(null)
        return if (servico != null) {
            servico.status = "ATIVO"
            repository.save(servico)
            true
        } else {
            false
        }
    }

    fun criar(servico: Servico): Servico {
        return repository.save(servico)
    }

    fun atualizar(id: Int, servicoAtualizado: Servico): Servico? {
        val servico = repository.findById(id).orElse(null)
        return if (servico != null) {
            servicoAtualizado.id = id
            repository.save(servicoAtualizado)
        } else {
            throw Exception("Serviço não encontrado")
        }
    }

    fun ativarSimultaneo(id: Int): Boolean {
        val servico = repository.findById(id).orElse(null)
        return if (servico != null) {
            servico.simultaneo = true
            repository.save(servico)
            true
        } else {
            false
        }
    }

    fun desativarSimultaneo(id: Int): Boolean {
        val servico = repository.findById(id).orElse(null)
        return if (servico != null) {
            servico.simultaneo = false
            repository.save(servico)
            true
        } else {
            false
        }
    }
}