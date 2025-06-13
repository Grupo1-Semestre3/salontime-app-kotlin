package sptech.salonTime.service

import org.springframework.stereotype.Service
import sptech.salonTime.dto.ServicoDto
import sptech.salonTime.entidade.Servico
import sptech.salonTime.exception.ServicoNaoEcontradoException
import sptech.salonTime.repository.ServicoRepository
import kotlin.math.E

@Service
class ServicoService(val repository: ServicoRepository) {

    fun listarAtivos(): List<ServicoDto> {
        return repository.listarTodosAtivosComMedia() ?: emptyList()
    }

    fun listarDesativados(): List<ServicoDto>? {
        return repository.listarDesativadosComMedia()
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
        val servico = repository.findById(id).orElseThrow { ServicoNaoEcontradoException("Serviço não econtrado") }

        servicoAtualizado.id = id
        return  repository.save(servicoAtualizado)

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

    fun atualizarFoto(id: Int, foto: ByteArray): ByteArray {
        val servico = repository.findById(id).orElseThrow { ServicoNaoEcontradoException("Serviço não encontrado") }

        servico.foto = foto
        repository.save(servico)

        return servico.foto!!

    }

    fun getFoto(id: Int): ByteArray? {
        val servico = repository.findById(id).orElseThrow { ServicoNaoEcontradoException("Serviço não encontrado") }
        return servico.foto
    }


}