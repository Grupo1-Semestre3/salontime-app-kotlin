package sptech.salonTime.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import sptech.salonTime.dto.DashboardAgendamentosMensaisDto
import sptech.salonTime.service.DashboardService

@RestController
@RequestMapping("/dashboard")
class DashboardController(
    val dashboardService: DashboardService
){

    @GetMapping("kpi/{ano}/{mes}")
    fun kpi(@PathVariable ano: Int, @PathVariable mes: Int): List<DashboardAgendamentosMensaisDto> {
        return dashboardService.pegarAgendamentosMensais(ano, mes)
    }

//    @GetMapping("atendimento-grafico/{mes}")
//    fun atendimento(@PathVariable ano: Int, @PathVariable mes: Int): List<DashboardAgendamentosMensaisDto> {
//        return dashboardService.atendimentoGrafico(mes)
//    }
//
//    @GetMapping("atendimento-servico/{mes}")
//    fun atendimentoServico(@PathVariable ano: Int, @PathVariable mes: Int): List<DashboardAgendamentosMensaisDto> {
//        return dashboardService.atendimentoServico(mes)
//    }
}