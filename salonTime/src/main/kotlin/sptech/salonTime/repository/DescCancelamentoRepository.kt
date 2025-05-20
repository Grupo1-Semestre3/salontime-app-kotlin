package sptech.salonTime.repository

import org.springframework.data.jpa.repository.JpaRepository
import sptech.salonTime.entidade.DescCancelamento

interface DescCancelamentoRepository : JpaRepository<DescCancelamento, Int> {
}