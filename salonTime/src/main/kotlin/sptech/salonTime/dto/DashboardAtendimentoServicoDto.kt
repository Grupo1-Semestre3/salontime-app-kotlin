package sptech.salonTime.dto

interface DashboardAtendimentoServicoDto {
    fun getServicoId(): Int
    fun getNomeServico(): String
    fun getQtdAtual(): Int
    fun getQtdAnterior(): Int?
}