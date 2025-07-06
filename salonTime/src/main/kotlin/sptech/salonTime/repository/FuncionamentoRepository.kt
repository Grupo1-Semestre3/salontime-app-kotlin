package sptech.salonTime.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import sptech.salonTime.entidade.DiaSemana
import sptech.salonTime.entidade.Funcionamento
import java.time.DayOfWeek

@Repository
interface FuncionamentoRepository: JpaRepository<Funcionamento, Int> {
    fun findByDiaSemana(diaDaSemana: DiaSemana): Funcionamento?
}