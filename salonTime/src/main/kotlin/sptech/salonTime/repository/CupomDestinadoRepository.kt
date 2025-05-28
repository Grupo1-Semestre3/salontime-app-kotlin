package sptech.salonTime.repository

import org.springframework.data.jpa.repository.JpaRepository
import sptech.salonTime.service.CupomDestinadoService

interface CupomDestinadoRepository: JpaRepository<CupomDestinadoService,  Int> {
}