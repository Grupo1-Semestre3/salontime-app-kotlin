package sptech.salonTime.service

import org.springframework.stereotype.Service
import sptech.salonTime.dto.DashboardAgendamentosMensaisDto
import sptech.salonTime.repository.DashboardRepository

@Service
class DashboardService(
    val repository: DashboardRepository
) {
    fun pegarAgendamentosMensais(ano: Int, mes: Int): List<DashboardAgendamentosMensaisDto> {
        return repository.buscarRelatorioMensal(ano, mes)
    }
}