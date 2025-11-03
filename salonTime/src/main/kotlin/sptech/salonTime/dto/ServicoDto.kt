package sptech.salonTime.dto

import sptech.salonTime.entidade.Servico
import java.time.LocalTime

data class ServicoDto(
    val id: Int?,
    val nome: String?,
    val preco: Double?,
    val tempo: LocalTime?,
    val status: String?,
    val simultaneo: Boolean?,
    val descricao: String?,
    val mediaAvaliacao: Double?
)
