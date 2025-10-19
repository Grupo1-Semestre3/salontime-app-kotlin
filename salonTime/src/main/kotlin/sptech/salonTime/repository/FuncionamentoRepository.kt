package sptech.salonTime.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import sptech.salonTime.entidade.DiaSemana
import sptech.salonTime.entidade.Funcionamento
import java.time.DayOfWeek

@Repository
interface FuncionamentoRepository: JpaRepository<Funcionamento, Int> {
    @Query("""
        SELECT f FROM Funcionamento f
        WHERE f.funcionario.id = :funcionarioId
          AND f.diaSemana = :diaSemana
    """)
    fun findByFuncionarioAndDiaSemana(
        @Param("funcionarioId") funcionarioId: Int,
        @Param("diaSemana") diaSemana: DiaSemana
    ): Funcionamento

    fun findAllByFuncionarioId(funcionarioId: Int): List<Funcionamento>
}