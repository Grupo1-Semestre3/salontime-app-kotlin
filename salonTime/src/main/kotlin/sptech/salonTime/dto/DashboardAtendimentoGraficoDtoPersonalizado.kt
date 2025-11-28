package sptech.salonTime.dto

import java.util.*

interface DashboardAtendimentoGraficoDtoPersonalizado {
    fun getDataAtual(): Date?
    fun getQtdAtual(): Int?
    fun getDiaMesAnterior(): Int?
    fun getQtdAnterior(): Int?
}
