package sptech.salonTime.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import sptech.salonTime.dto.DashboardAgendamentosMensaisDto
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

}
