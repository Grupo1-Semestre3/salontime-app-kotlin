package sptech.salonTime.dto

data class PointsDto(
    val pointsParcial: Long,
    val pointsTotal: Long,
    //Percent para exibir no front o valor do desconto que o cliente ira ganhar
    val porcentagemCupom: Int
)
