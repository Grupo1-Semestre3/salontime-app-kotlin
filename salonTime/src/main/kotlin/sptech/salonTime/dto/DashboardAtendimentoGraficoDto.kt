package sptech.salonTime.dto

interface DashboardAtendimentoGraficoDto{
    fun getDiaMesAtual(): Int?
    fun getQtdAtual(): Int?
    fun getDiaMesAnterior(): Int?
    fun getQtdAnterior(): Int?
}
