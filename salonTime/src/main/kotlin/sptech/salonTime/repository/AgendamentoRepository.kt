package sptech.salonTime.repository

import org.springframework.data.jpa.repository.JpaRepository
import sptech.salonTime.entidade.Agendamento

interface AgendamentoRepository: JpaRepository<Agendamento, Int> {
}