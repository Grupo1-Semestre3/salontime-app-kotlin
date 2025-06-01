package sptech.salonTime.dto

import sptech.salonTime.entidade.Agendamento
import java.time.LocalDateTime

data class AvaliacaoDto(
    val id: Int,
    val nomeServico: String,
    val dataAgendamento: String,
    val agendamentoId: Int,
    val nomeFuncionario: String,
    val nomeUsuario: String,
    val notaServico: Int,
    val descricaoServico: String,
    val dataHorario: LocalDateTime
)
