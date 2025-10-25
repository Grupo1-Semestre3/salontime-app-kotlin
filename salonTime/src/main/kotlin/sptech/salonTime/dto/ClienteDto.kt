package sptech.salonTime.dto

data class ClienteDto(
    val id: Int,
    val nome: String,
    val email: String,
    val telefone: String,
    val totalPendencias: Long
)
