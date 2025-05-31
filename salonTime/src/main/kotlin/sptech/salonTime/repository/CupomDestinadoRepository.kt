package sptech.salonTime.repository

import org.springframework.data.jpa.repository.JpaRepository
import sptech.salonTime.entidade.CupomDestinado

interface CupomDestinadoRepository: JpaRepository<CupomDestinado,  Int> {

}