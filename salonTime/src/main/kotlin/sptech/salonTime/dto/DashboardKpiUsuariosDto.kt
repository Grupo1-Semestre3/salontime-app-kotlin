package sptech.salonTime.dto

interface DashboardKpiUsuariosDto {
    fun getAno(): Int?
    fun getMes(): Int?
    fun getTotalCadastros(): Int?
    fun getCadastrosMesAnterior(): Int?
    fun getVariacaoPercentual(): Double?
}