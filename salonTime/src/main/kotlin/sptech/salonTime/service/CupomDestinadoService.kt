package sptech.salonTime.service;

import org.springframework.stereotype.Service
import sptech.salonTime.entidade.CupomDestinado
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

        val cupom = cupomDestinado.cupom?.id?.let { cupomRepository.findById(it).orElse(null) }
        val usuario = cupomDestinado.usuario?.id?.let { usuarioRepository.findById(it).orElse(null) }

        cupomDestinado.cupom = cupom
        cupomDestinado.usuario = usuario
        cupomDestinado.usado = false

        return repository.save(cupomDestinado)
    }

    fun editar(id: Int, cupomDestinado: CupomDestinado): CupomDestinado {
        val CupomDestinadoEncontrado = repository.findById(id).orElseThrow { RuntimeException("Cupom Destinado not found") }

        cupomDestinado.cupom = CupomDestinadoEncontrado.cupom
        cupomDestinado.usuario = CupomDestinadoEncontrado.usuario
        cupomDestinado.usado = CupomDestinadoEncontrado.usado

        return repository.save(cupomDestinado)
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
