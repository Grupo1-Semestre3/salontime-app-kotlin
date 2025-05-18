package sptech.salonTime.repository

import org.springframework.data.jpa.repository.JpaRepository
import sptech.salonTime.entidade.Pagamento

interface PagamentoRepository: JpaRepository<Pagamento, Int> {
}