package sptech.salonTime.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import sptech.salonTime.entidade.HorarioExcecao
import java.time.LocalDate

interface HorarioExcecaoRepository: JpaRepository<HorarioExcecao, Int> {
    @Query("""
    SELECT h FROM HorarioExcecao h
    WHERE h.dataInicio <= :data
      AND h.dataFim >= :data
      AND (:funcionarioId IS NULL OR h.funcionario.id = :funcionarioId)
""")
    fun findByData(@Param("data") data: LocalDate, @Param("funcionarioId") funcionarioId: Int?): HorarioExcecao?

    @Query("""
        SELECT h FROM HorarioExcecao h
        WHERE h.funcionario.id = :funcionarioId
          AND :data BETWEEN h.dataInicio AND h.dataFim
    """)
    fun findExcecaoPorData(
        @Param("data") data: LocalDate,
        @Param("funcionarioId") funcionarioId: Int
    ): HorarioExcecao?

}