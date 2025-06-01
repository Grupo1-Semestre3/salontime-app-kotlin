package sptech.salonTime.service;

import org.springframework.stereotype.Service
import sptech.salonTime.entidade.CupomDestinado
import sptech.salonTime.exception.CupomNaoEncontradoException
import sptech.salonTime.exception.UsuarioNaoEncontradoException
import sptech.salonTime.repository.CupomDestinadoRepository
import sptech.salonTime.repository.CupomRepository
import sptech.salonTime.repository.UsuarioRepository

@Service
class CupomDestinadoService (
    val repository: CupomDestinadoRepository,
    val cupomRepository: CupomRepository,
    val usuarioRepository: UsuarioRepository
){
    fun salvar(cupomDestinado: CupomDestinado): CupomDestinado {

        val cupom = cupomDestinado.cupom?.id?.let { cupomRepository.findById(it).orElseThrow { CupomNaoEncontradoException("Cupom não encontrado") } }
        val usuario = cupomDestinado.usuario?.id?.let { usuarioRepository.findById(it).orElseThrow { UsuarioNaoEncontradoException("Usuário não encontrado") } }

        cupomDestinado.cupom = cupom
        cupomDestinado.usuario = usuario
        cupomDestinado.usado = false

        return repository.save(cupomDestinado)
    }

    fun editar(id: Int, cupomDestinado: CupomDestinado): CupomDestinado {
        val CupomDestinadoEncontrado = repository.findById(id).orElseThrow { RuntimeException("Cupom Destinado not found") }

        CupomDestinadoEncontrado.cupom = cupomDestinado.cupom
        CupomDestinadoEncontrado.usuario = cupomDestinado.usuario
        CupomDestinadoEncontrado.usado = cupomDestinado.usado

        return repository.save(CupomDestinadoEncontrado)
    }

    fun atualizarUsado (id:Int, usado:Boolean): CupomDestinado {
        val CupomDestinadoEncontrado = repository.findById(id).orElseThrow { RuntimeException("Cupom Destinado not found") }

        CupomDestinadoEncontrado.usado = usado

        return repository.save(CupomDestinadoEncontrado)
    }

    fun deletar(id:Int){
        val CupomDestinadoEncontrado = repository.findById(id).orElseThrow { RuntimeException("Cupom Destinado not found") }
        repository.delete(CupomDestinadoEncontrado)
    }

    fun listar(): List<CupomDestinado> = repository.findAll()



}
