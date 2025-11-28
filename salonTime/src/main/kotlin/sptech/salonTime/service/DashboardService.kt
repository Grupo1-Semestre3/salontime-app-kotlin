package sptech.salonTime.service

import org.springframework.cglib.core.Local
import org.springframework.stereotype.Service
import sptech.salonTime.dto.*
import sptech.salonTime.repository.DashboardRepository
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class DashboardService(
    val repository: DashboardRepository
) {
    fun pegarAgendamentosMensaisPersonalizado(dataInicio: LocalDate, dataFim: LocalDate): List<DashboardAgendamentosMensaisDto> {
        return repository.buscarRelatorioMensalPersonalizado(dataInicio, dataFim)
    }

    fun pegarKpiUsuariosPersonalizado(dataInicio: LocalDate, dataFim: LocalDate): DashboardKpiUsuariosDto? {
        return repository.buscarKpiUsuariosPersonalizado(dataInicio, dataFim)
    }

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

    fun atendimentoGraficoPersonalizado(dataInicio: LocalDate, dataFim: LocalDate): List<DashboardAtendimentoGraficoDtoPersonalizado>? {
        return repository.buscarAtendimentoGraficoPersonalizado(dataInicio, dataFim)
    }

    fun atendimentoServicoPersonalizado(dataInicio: LocalDate, dataFim: LocalDate): List<DashboardAtendimentoServicoDto>? {
        return repository.buscarAtendimentoServicoPersonalizado(dataInicio, dataFim)
    }
}