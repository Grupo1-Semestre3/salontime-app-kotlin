package sptech.salonTime.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import sptech.salonTime.dto.HorariosOcupadosDto
import sptech.salonTime.entidade.Agendamento
import java.time.LocalDate
import java.time.LocalTime


interface AgendamentoRepository: JpaRepository<Agendamento, Int> {
    @Query(value = "SELECT * FROM agendamento WHERE ((data > CURDATE()) OR (data = CURDATE() AND inicio > CURTIME())) AND funcionario_id = :idFunc ORDER BY data ASC, inicio ASC", nativeQuery = true)
    fun buscarProximosAgendamentosPorFuncionario(idFunc: Int): List<Agendamento>

    @Query(value = "SELECT * FROM agendamento WHERE ((data > CURDATE()) OR (data = CURDATE() AND inicio > CURTIME())) AND usuario_id = :idUser ORDER BY data ASC, inicio ASC LIMIT 1", nativeQuery = true)
    fun buscarProximosAgendamentosPorUsuario(idUser: Int): Agendamento?

    @Query(
        value = """
        SELECT COUNT(*) > 0 
        FROM agendamento 
        WHERE data = :data 
          AND (:inicio < fim AND :fim > inicio)
    """,
        nativeQuery = true
    )
    fun existeConflitoDeAgendamento(
        data: LocalDate?,
        inicio: LocalTime?,
        fim: LocalTime?
    ): Long

    @Query(
        value = """
        SELECT servico_id
        FROM agendamento
        WHERE data = :data
          AND (:inicio < fim AND :fim > inicio)
        LIMIT 1
    """,
        nativeQuery = true
    )
    fun pegarAgendamentoConlfito(
        data: LocalDate?,
        inicio: LocalTime?,
        fim: LocalTime?
    ): Int


    @Query("""
    SELECT a FROM Agendamento a
    JOIN FETCH a.usuario u
    JOIN FETCH a.funcionario f
    JOIN FETCH a.pagamento p
    JOIN FETCH a.servico s
    JOIN FETCH a.statusAgendamento sa
""")
    fun listarTudo(): List<Agendamento>

    @Query(nativeQuery = true, value = """
        SELECT * FROM agendamento WHERE (data < CURDATE() AND usuario_id = :idUser) OR (data = CURDATE() AND inicio < CURTIME()) ORDER BY data ASC, inicio ASC
    """)
    fun buscarAgendamentosPassadosPorUsuario(idUser: Int): List<Agendamento>

    @Query(nativeQuery = true, value = """
        SELECT * FROM agendamento WHERE (data < CURDATE() AND funcionario_id = :idFunc) OR (data = CURDATE() AND inicio < CURTIME()) ORDER BY data ASC, inicio ASC
    """)
    fun buscarAgendamentosPassadosPorFuncionario(idFunc: Int): List<Agendamento>

    @Query(nativeQuery = true, value = """
        SELECT * FROM agendamento WHERE status_agendamento_id = 2 ORDER BY data ASC, inicio ASC
    """)
    fun buscarAgendamentosCancelados(): List<Agendamento>



    @Query(value = """
    SELECT 
        a.inicio, 
        a.fim, 
        a.funcionario_id,
        f.capacidade,
        s.simultaneo
    FROM agendamento a
    JOIN servico s ON s.id = a.servico_id
    LEFT JOIN funcionamento f 
        ON f.funcionario_id = a.funcionario_id
    WHERE a.data = :data 
      AND a.funcionario_id IN (:ids)
""", nativeQuery = true)
    fun buscarHorariosOcupados(
        @Param("data") data: LocalDate,
        @Param("ids") ids: List<Int>
    ): List<HorariosOcupadosDto>

/*
    @Query(value = """
    SELECT a.inicio, a.fim, a.funcionario_id,
    (SELECT f.capacidade FROM funcionamento f WHERE f.funcionario_id = a.funcionario_id LIMIT 1) AS capacidade
FROM agendamento a
WHERE a.data = :data AND a.funcionario_id IN (:ids)
""", nativeQuery = true)
    fun buscarHorariosOcupados(@Param("data") data: LocalDate, @Param("ids") ids: List<Int>): List<HorariosOcupadosDto>
*/
    @Query(value = """
        SELECT * FROM agendamento WHERE funcionario_id = :idFuncionario
    """, nativeQuery = true)
    fun listarCalendarioPorFuncionario(idFuncionario: Int): List<Agendamento>

    @Query("""
        SELECT a FROM Agendamento a
        WHERE a.funcionario.id = :funcionarioId
          AND a.data = :data
          AND a.statusAgendamento.status = 'AGENDADO'
    """)
    fun buscarHorariosOcupadosPorFuncionario(
        @Param("data") data: LocalDate,
        @Param("funcionarioId") funcionarioId: Int
    ): List<Agendamento>
}