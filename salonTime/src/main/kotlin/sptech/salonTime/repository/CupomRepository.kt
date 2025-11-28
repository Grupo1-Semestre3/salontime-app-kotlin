package sptech.salonTime.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import sptech.salonTime.dto.PointsDto
import sptech.salonTime.entidade.Cupom

interface CupomRepository : JpaRepository<Cupom, Int> {
    @Query("SELECT c FROM Cupom c WHERE c.ativo = true AND c.fim > CURRENT_TIME")
    fun listarAtivos(): List<Cupom>
    fun findByCodigo(cupom: String?): Cupom?
    fun findByTipoDestinatario(tipo: String): List<Cupom>

    @Query(
        value = """
SELECT
    CASE
        WHEN a.total_agendamentos % cc.intervalo_atendimento = 0
             AND a.total_agendamentos > 0
        THEN cc.intervalo_atendimento
        ELSE a.total_agendamentos % cc.intervalo_atendimento
    END AS pointsParcial,
    cc.intervalo_atendimento AS pointsTotal,
    cc.porcentagem_desconto AS porcentagemCupom
FROM (
    SELECT 
        h.agendamento_usuario_id,
        COUNT(*) AS total_agendamentos
    FROM historico_agendamento h
    WHERE h.agendamento_status_agendamento_id = 5
      AND h.agendamento_usuario_id = :idUsuario
    GROUP BY h.agendamento_usuario_id
) a
CROSS JOIN cupom_configuracao cc;
        """,
        nativeQuery = true
    )
    fun calcularPoints(idUsuario: Int): PointsDto?
}
