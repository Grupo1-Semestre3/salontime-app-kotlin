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



}
