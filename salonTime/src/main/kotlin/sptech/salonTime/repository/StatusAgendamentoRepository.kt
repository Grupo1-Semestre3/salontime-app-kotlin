package sptech.salonTime.repository

import org.springframework.data.jpa.repository.JpaRepository
import sptech.salonTime.entidade.StatusAgendamento


interface StatusAgendamentoRepository: JpaRepository<StatusAgendamento, Int> {
}