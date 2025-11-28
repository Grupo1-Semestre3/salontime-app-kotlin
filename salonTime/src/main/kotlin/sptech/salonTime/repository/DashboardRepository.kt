package sptech.salonTime.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import sptech.salonTime.dto.*
import sptech.salonTime.entidade.Agendamento
import java.time.LocalDate

//Agendamento colocado propositalmente para evitar erro, mas os selects são para a dash e dash não tem entidade
@Repository
interface DashboardRepository : JpaRepository<Agendamento, Int> {





    @Query(
        value = """
WITH filtrado AS (
    SELECT
        YEAR(data) AS ano,
        MONTH(data) AS mes,
        SUM(CASE WHEN status_agendamento_id = 5 THEN 1 ELSE 0 END) AS total_atendimentos,
        SUM(CASE WHEN status_agendamento_id = 2 THEN 1 ELSE 0 END) AS total_cancelados,
        SUM(CASE WHEN status_agendamento_id = 5 THEN preco ELSE 0 END) AS faturamento_total
    FROM vw_agendamentos_base
    WHERE data BETWEEN :dataInicio AND :dataFim
    GROUP BY YEAR(data), MONTH(data)
),
totais AS (
    SELECT
        SUM(total_atendimentos) AS total_atendimentos_geral,
        SUM(total_cancelados) AS total_cancelados_geral,
        SUM(faturamento_total) AS faturamento_total_geral
    FROM filtrado
)
SELECT
    f.ano,
    f.mes,
    f.total_atendimentos AS totalAtendimentos,
    f.total_cancelados AS totalCancelados,
    f.faturamento_total AS faturamentoTotal,

    (f.total_atendimentos / NULLIF(t.total_atendimentos_geral, 0) * 100) AS totalAtendimentosTaxa,
    (f.total_cancelados / NULLIF(t.total_cancelados_geral, 0) * 100) AS totalCanceladosTaxa,
    (f.faturamento_total / NULLIF(t.faturamento_total_geral, 0) * 100) AS faturamentoTotalTaxa
FROM filtrado f
CROSS JOIN totais t;

    """,
        nativeQuery = true
    )
    fun buscarRelatorioMensalPersonalizado(
        @Param("dataInicio") dataInicio: LocalDate,
        @Param("dataFim") dataFim: LocalDate
    ): List<DashboardAgendamentosMensaisDto>




    @Query("""
   SELECT
    SUM(total_cadastros) AS totalCadastros
FROM 
    vw_cadastros_diarios_usuarios_personalizado
WHERE 
    data BETWEEN :dataInicio AND :dataFim;
""", nativeQuery = true)
    fun buscarKpiUsuariosPersonalizado(
        @Param("dataInicio") dataInicio: LocalDate,
        @Param("dataFim") dataFim: LocalDate
    ): DashboardKpiUsuariosDto?








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
    atual.total_cadastros AS totalCadastros,
    anterior.total_cadastros AS cadastrosMesAnterior,
    CASE 
        WHEN anterior.total_cadastros IS NULL OR anterior.total_cadastros = 0 THEN NULL
        ELSE ROUND(
            (atual.total_cadastros - anterior.total_cadastros) * 100.0 / anterior.total_cadastros, 
            2
        )
    END AS variacaoPercentual
FROM vw_cadastros_mensais_usuarios atual
LEFT JOIN vw_cadastros_mensais_usuarios anterior
    ON (
        (anterior.ano = atual.ano AND anterior.mes = atual.mes - 1)
        OR (anterior.ano = atual.ano - 1 AND atual.mes = 1 AND anterior.mes = 12)
    )
WHERE atual.ano = :ano AND atual.mes = :mes;
        
    """, nativeQuery = true)
    fun buscarKpiUsuarios(ano: Int, mes: Int): DashboardKpiUsuariosDto?

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
    atual.servico_id AS servicoId,
    atual.nome_servico AS nomeServico,
    atual.quantidade_atendimentos AS qtdAtual,
    COALESCE(anterior.quantidade_atendimentos, 0) AS qtdAnterior
FROM view_atendimentos_por_servico_mes AS atual
JOIN data_base 
    ON atual.ano = data_base.ano
   AND atual.mes = data_base.mes
LEFT JOIN view_atendimentos_por_servico_mes AS anterior
    ON anterior.servico_id = atual.servico_id
   AND anterior.ano = (SELECT ano FROM mes_anterior)
   AND anterior.mes = (SELECT mes FROM mes_anterior)
ORDER BY atual.nome_servico;

    """, nativeQuery = true)
    fun buscarAtendimentoServico(ano: Int, mes: Int): List<DashboardAtendimentoServicoDto>

    @Query(value = """
SELECT 
    a.dia AS data_atual,
    a.total_atendimentos AS qtd_atual,
    b.dia AS data_anterior,
    b.total_atendimentos AS qtd_anterior
FROM atendimentos_por_dia_personalizado a
LEFT JOIN atendimentos_por_dia_personalizado b
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
WHERE a.dia BETWEEN :dataInicio AND :dataFim
ORDER BY a.dia;

    """, nativeQuery = true)
    fun buscarAtendimentoGraficoPersonalizado(dataInicio: LocalDate, dataFim: LocalDate): List<DashboardAtendimentoGraficoDtoPersonalizado>?


    @Query("""
WITH periodo_atual AS (
    SELECT
        servico_id,
        nome_servico,
        SUM(quantidade_atendimentos) AS qtdAtual
    FROM view_atendimentos_por_servico_mes_personalizado
    WHERE dia BETWEEN :dataInicio AND :dataFim
    GROUP BY servico_id, nome_servico
),
periodo_anterior AS (
    SELECT
        servico_id,
        nome_servico,
        SUM(quantidade_atendimentos) AS qtdAnterior
    FROM view_atendimentos_por_servico_mes_personalizado
    WHERE dia BETWEEN 
        DATE_SUB(:dataInicio, INTERVAL 1 MONTH) AND 
        DATE_SUB(:dataFim, INTERVAL 1 MONTH)
    GROUP BY servico_id, nome_servico
)
SELECT
    atual.servico_id AS servicoId,
    atual.nome_servico AS nomeServico,
    atual.qtdAtual,
    COALESCE(anterior.qtdAnterior, 0) AS qtdAnterior
FROM periodo_atual atual
LEFT JOIN periodo_anterior anterior
      ON anterior.servico_id = atual.servico_id
ORDER BY atual.nome_servico;
""", nativeQuery = true)
    fun buscarAtendimentoServicoPersonalizado(
        dataInicio: LocalDate,
        dataFim: LocalDate
    ): List<DashboardAtendimentoServicoDto>



}
