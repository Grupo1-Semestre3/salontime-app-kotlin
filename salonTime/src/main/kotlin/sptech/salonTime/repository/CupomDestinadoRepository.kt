package sptech.salonTime.repository

import org.springframework.data.jpa.repository.JpaRepository
import sptech.salonTime.entidade.Cupom
import sptech.salonTime.entidade.CupomDestinado
import sptech.salonTime.entidade.Usuario
import java.util.*

interface CupomDestinadoRepository: JpaRepository<CupomDestinado,  Int> {
    fun findByCupomAndUsuario(cupomEncontrado: Cupom, usuarioEncontrado: Optional<Usuario>): CupomDestinado?

}