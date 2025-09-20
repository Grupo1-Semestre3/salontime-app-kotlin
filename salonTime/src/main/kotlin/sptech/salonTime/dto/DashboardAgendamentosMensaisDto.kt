package sptech.salonTime.dto

interface DashboardAgendamentosMensaisDto{
    fun getAno(): Int?
    fun getMes(): Int?
    fun getTotalAtendimentos(): Long?
    fun getTotalCancelados(): Long?
    fun getFaturamentoTotal(): Double?
    fun getTotalAtendimentosTaxa(): Long?
    fun getTotalCanceladosTaxa(): Long?
    fun getFaturamentoTotalTaxa(): Double?
}
