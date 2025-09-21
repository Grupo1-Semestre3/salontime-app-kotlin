package sptech.salonTime.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import sptech.salonTime.dto.DashboardAgendamentosMensaisDto
import sptech.salonTime.dto.DashboardAtendimentoGraficoDto
import sptech.salonTime.dto.DashboardAtendimentoServicoDto
import sptech.salonTime.dto.DashboardKpiUsuariosDto
import sptech.salonTime.service.DashboardService

@RestController
@RequestMapping("/dashboard")
class DashboardController(
    val dashboardService: DashboardService
){

    @GetMapping("kpi/{ano}/{mes}")
    fun kpi(@PathVariable ano: Int, @PathVariable mes: Int): ResponseEntity<List<DashboardAgendamentosMensaisDto>> {
        return ResponseEntity.status(200).body(dashboardService.pegarAgendamentosMensais(ano, mes))
    }

    @GetMapping("kpi-usuarios/{ano}/{mes}")
    fun kpiUsuarios(@PathVariable ano: Int, @PathVariable mes: Int): ResponseEntity<DashboardKpiUsuariosDto> {
        return ResponseEntity.status(200).body(dashboardService.pegarKpiUsuarios(ano, mes))
    }

        @GetMapping("atendimento-grafico/{ano}/{mes}")
    fun atendimento(@PathVariable ano: Int, @PathVariable mes: Int): ResponseEntity<List<DashboardAtendimentoGraficoDto>> {
        return ResponseEntity.status(200).body(dashboardService.atendimentoGrafico(ano, mes))
    }

    @GetMapping("atendimento-servico/{ano}/{mes}")
    fun atendimentoServico(@PathVariable ano: Int, @PathVariable mes: Int): ResponseEntity<List<DashboardAtendimentoServicoDto>> {
        return ResponseEntity.status(200).body(dashboardService.atendimentoServico(ano, mes))
    }
}