package sptech.salonTime.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import sptech.salonTime.dto.DashboardAgendamentosMensaisDto
import sptech.salonTime.dto.DashboardAtendimentoGraficoDto
import sptech.salonTime.dto.DashboardAtendimentoServicoDto
import sptech.salonTime.dto.DashboardKpiUsuariosDto
import sptech.salonTime.entidade.Agendamento

//Agendamento colocado propositalmente para evitar erro, mas os selects são para a dash e dash não tem entidade
@Repository
interface DashboardRepository : JpaRepository<Agendamento, Int> {

    @Query(
        value = """
        select 
    atual.ano,
    atual.mes,
    atual.total_atendimentos,
    atual.total_cancelados,
    atual.faturamento_total,
    
    case 
        when anterior.total_atendimentos = 0 or anterior.total_atendimentos is null then null
        else round(((atual.total_atendimentos - anterior.total_atendimentos) / anterior.total_atendimentos * 100), 2)
    end as total_atendimentos_taxa,

    case 
        when anterior.total_cancelados = 0 or anterior.total_cancelados is null then null
        else round(((atual.total_cancelados - anterior.total_cancelados) / anterior.total_cancelados * 100), 2)
    end as total_cancelados_taxa,

    case 
        when anterior.faturamento_total = 0 or anterior.faturamento_total is null then null
        else round(((atual.faturamento_total - anterior.faturamento_total) / anterior.faturamento_total * 100), 2)
    end as faturamento_total_taxa

from vw_agendamentos_mensal atual
left join vw_agendamentos_mensal anterior
    on anterior.ano = atual.ano
    and anterior.mes = atual.mes - 1

where atual.ano = :ano and atual.mes = :mes;

    """,
        nativeQuery = true
    )
    fun buscarRelatorioMensal(ano: Int, mes: Int): List<DashboardAgendamentosMensaisDto>




    @Query("""
        
        SELECT 
    atual.ano,
    atual.mes,
    atual.total_cadastros,
    anterior.total_cadastros AS cadastros_mes_anterior,
    CASE 
        WHEN anterior.total_cadastros IS NULL OR anterior.total_cadastros = 0 THEN NULL
        ELSE ROUND(
            (atual.total_cadastros - anterior.total_cadastros) * 100.0 / anterior.total_cadastros, 
            2
        )
    END AS variacao_percentual
FROM vw_cadastros_mensais_usuarios atual
LEFT JOIN vw_cadastros_mensais_usuarios anterior
    ON (
        (anterior.ano = atual.ano AND anterior.mes = atual.mes - 1)
        OR (anterior.ano = atual.ano - 1 AND atual.mes = 1 AND anterior.mes = 12)
    )
WHERE atual.ano = :ano AND atual.mes = :mes;
        
    """, nativeQuery = true)
    fun buscarKpiUsuarios(ano: Int, mes: Int): DashboardKpiUsuariosDto

    @Query("""
        
        SELECT 
    DATE_FORMAT(a.dia, '%d') AS dia_mes_atual,
    a.total_atendimentos AS qtd_atual,
    DATE_FORMAT(b.dia, '%d') AS dia_mes_anterior,
    b.total_atendimentos AS qtd_anterior
FROM atendimentos_por_dia a
LEFT JOIN atendimentos_por_dia b
  ON DAY(a.dia) = DAY(b.dia)
  AND MONTH(b.dia) = 
        CASE 
            WHEN MONTH(a.dia) = 1 THEN 12
            ELSE MONTH(a.dia) - 1
        END
  AND YEAR(b.dia) =
        CASE 
            WHEN MONTH(a.dia) = 1 THEN YEAR(a.dia) - 1
            ELSE YEAR(a.dia)
        END
WHERE MONTH(a.dia) = :mes
  AND YEAR(a.dia) = :ano
ORDER BY a.dia
        
    """, nativeQuery = true)
    fun buscarAtendimentoGrafico(ano: Int, mes: Int): List<DashboardAtendimentoGraficoDto>


    @Query("""
        
        WITH data_base AS (
  SELECT :ano AS ano, :mes AS mes
),
mes_anterior AS (
  SELECT
    CASE WHEN mes = 1 THEN ano - 1 ELSE ano END AS ano,
    CASE WHEN mes = 1 THEN 12 ELSE mes - 1 END AS mes
  FROM data_base	
)
SELECT
    atual.servico_id,
    atual.nome_servico,
    atual.quantidade_atendimentos AS qtd_atual,
    COALESCE(anterior.quantidade_atendimentos, 0) AS qtd_anterior
FROM view_atendimentos_por_servico_mes AS atual
JOIN data_base ON atual.ano = data_base.ano AND atual.mes = data_base.mes
LEFT JOIN view_atendimentos_por_servico_mes AS anterior
  ON atual.servico_id = anterior.servico_id
  AND anterior.ano = (SELECT ano FROM mes_anterior)
  AND anterior.mes = (SELECT mes FROM mes_anterior)
ORDER BY atual.nome_servico

    """, nativeQuery = true)
    fun buscarAtendimentoServico(ano: Int, mes: Int): List<DashboardAtendimentoServicoDto>

}
