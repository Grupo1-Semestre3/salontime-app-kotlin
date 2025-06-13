package sptech.salonTime.dto

import java.time.LocalTime

data class ListagemServicoDto(
    val id: Int,
    val nome: String,
    val preco: Double,
    val tempo: LocalTime,
    val status: String,
    val simultaneo: Boolean,
    val descricao: String?,
    val mediaAvaliacao: Double?
)
