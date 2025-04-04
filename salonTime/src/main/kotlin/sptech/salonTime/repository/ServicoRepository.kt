package sptech.salonTime.repository

import org.springframework.data.jpa.repository.JpaRepository
import sptech.salonTime.entidade.Servico

interface ServicoRepository: JpaRepository<Servico, Int> {
}