package sptech.salonTime.repository

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import sptech.salonTime.entidade.Agendamento
import java.time.LocalDate
import java.time.LocalTime


interface AgendamentoRepository: JpaRepository<Agendamento, Int> {
    @Query(value = "SELECT * FROM agendamento WHERE (data > CURDATE()) OR (data = CURDATE() AND inicio > CURTIME()) ORDER BY data ASC, inicio ASC", nativeQuery = true)
    fun buscarProximosAgendamentos(): List<Agendamento>

    @Query(
        value = """
        SELECT COUNT(*) > 0 
        FROM agendamento 
        WHERE data = :data 
          AND ((:inicio BETWEEN inicio AND fim) OR (:fim BETWEEN inicio AND fim) OR (inicio BETWEEN :inicio AND :fim))
    """,
        nativeQuery = true
    )
    fun existeConflitoDeAgendamento(data: LocalDate?, inicio: LocalTime?, fim: LocalTime?): Long

    @Query("""
    SELECT a FROM Agendamento a
    JOIN FETCH a.usuario u
    JOIN FETCH a.funcionario f
    JOIN FETCH a.pagamento p
    JOIN FETCH a.servico s
    JOIN FETCH a.statusAgendamento sa
""")
    fun listarTudo(): List<Agendamento>


}