package sptech.salonTime.service

import org.springframework.stereotype.Service
import sptech.salonTime.dto.DashboardAgendamentosMensaisDto
import sptech.salonTime.dto.DashboardAtendimentoGraficoDto
import sptech.salonTime.dto.DashboardAtendimentoServicoDto
import sptech.salonTime.dto.DashboardKpiUsuariosDto
import sptech.salonTime.repository.DashboardRepository

@Service
class DashboardService(
    val repository: DashboardRepository
) {
    fun pegarAgendamentosMensais(ano: Int, mes: Int): List<DashboardAgendamentosMensaisDto> {
        return repository.buscarRelatorioMensal(ano, mes)
    }

    fun pegarKpiUsuarios(ano: Int, mes: Int): DashboardKpiUsuariosDto? {
        return repository.buscarKpiUsuarios(ano, mes)
    }
    fun atendimentoGrafico(ano: Int, mes: Int): List<DashboardAtendimentoGraficoDto>{
        return repository.buscarAtendimentoGrafico(ano, mes)
    }

    fun atendimentoServico(ano: Int, mes: Int): List<DashboardAtendimentoServicoDto>{
        return repository.buscarAtendimentoServico(ano, mes)
    }
}