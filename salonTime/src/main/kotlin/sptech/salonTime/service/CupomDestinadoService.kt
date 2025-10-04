package sptech.salonTime.service;

import org.springframework.stereotype.Service
import sptech.salonTime.dto.CupomDestinadoDto
import sptech.salonTime.entidade.CupomDestinado
import sptech.salonTime.exception.CupomDestinadoNaoEncontradoException
import sptech.salonTime.exception.CupomNaoEncontradoException
import sptech.salonTime.exception.UsuarioNaoEncontradoException
import sptech.salonTime.mapper.CupomDestinadoMapper
import sptech.salonTime.repository.CupomDestinadoRepository
import sptech.salonTime.repository.CupomRepository
import sptech.salonTime.repository.UsuarioRepository

@Service
class CupomDestinadoService (
    val repository: CupomDestinadoRepository,
    val cupomRepository: CupomRepository,
    val usuarioRepository: UsuarioRepository
){
    fun salvar(cupomDestinado: CupomDestinado): CupomDestinadoDto {

        val cupom = cupomDestinado.cupom?.id?.let { cupomRepository.findById(it).orElseThrow { CupomNaoEncontradoException("Cupom não encontrado") } }
        val usuario = cupomDestinado.usuario?.id?.let { usuarioRepository.findById(it).orElseThrow { UsuarioNaoEncontradoException("Usuário não encontrado") } }

        cupomDestinado.cupom = cupom
        cupomDestinado.usuario = usuario
        cupomDestinado.usado = false

        val cupomDestinadoSalvo = repository.save(cupomDestinado)
        return CupomDestinadoMapper.toDto(cupomDestinadoSalvo)
    }

    fun editar(id: Int, cupomDestinado: CupomDestinado): CupomDestinadoDto {
        val CupomDestinadoEncontrado = repository.findById(id).orElseThrow { CupomDestinadoNaoEncontradoException("Cupom Destinado not found") }

        CupomDestinadoEncontrado.cupom = cupomDestinado.cupom
        CupomDestinadoEncontrado.usuario = cupomDestinado.usuario
        CupomDestinadoEncontrado.usado = cupomDestinado.usado

        return CupomDestinadoMapper.toDto(repository.save(CupomDestinadoEncontrado))
    }

    fun atualizarUsado (id:Int, usado:Boolean): CupomDestinadoDto {
        val CupomDestinadoEncontrado = repository.findById(id).orElseThrow { CupomDestinadoNaoEncontradoException("Cupom Destinado not found") }

        CupomDestinadoEncontrado.usado = usado

        return CupomDestinadoMapper.toDto(repository.save(CupomDestinadoEncontrado))
    }

    fun deletar(id:Int){
        val CupomDestinadoEncontrado = repository.findById(id).orElseThrow { CupomDestinadoNaoEncontradoException("Cupom Destinado not found") }
        repository.delete(CupomDestinadoEncontrado)
    }

    fun listar(): List<CupomDestinadoDto>{
        return repository.findAll().map { CupomDestinadoMapper.toDto(it) }
    }

    fun listarPorIdUsuario(idUsuario: Int): List<CupomDestinadoDto> {
        val usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow { RuntimeException("Usuário não encontrado") }

        val listaDisponiveis = mutableListOf<CupomDestinado>()

        // 1️⃣ Cupons do tipo TODOS
        val cuponsTodos = cupomRepository.findByTipoDestinatario("TODOS")
        cuponsTodos.forEach { cupom ->
            val jaExiste = repository.existsByUsuarioAndCupom(usuario, cupom)
            if (!jaExiste) {
                listaDisponiveis.add(CupomDestinado(cupom = cupom, usuario = usuario, usado = false))
            }
        }

        // 2️⃣ Cupons do tipo EXCLUSIVO e ainda não usados
        val cuponsExclusivos = cupomRepository.findByTipoDestinatario("EXCLUSIVO")
        cuponsExclusivos.forEach { cupom ->
            val cupomDestinado = repository.findByUsuario(usuario)
                .find { it.cupom?.id == cupom.id && it.usado == false }
            if (cupomDestinado != null) {
                listaDisponiveis.add(cupomDestinado)
            }
        }

        return listaDisponiveis.map { CupomDestinadoMapper.toDto(it) }
    }
}
