package sptech.salonTime.repository

import org.springframework.data.jpa.repository.JpaRepository
import sptech.salonTime.entidade.Funcionamento

interface FuncionamentoRepository: JpaRepository<Funcionamento, Int> {
}