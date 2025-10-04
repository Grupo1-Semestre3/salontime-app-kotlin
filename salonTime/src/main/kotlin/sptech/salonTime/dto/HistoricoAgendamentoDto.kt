package sptech.salonTime.dto

import sptech.salonTime.entidade.StatusAgendamento
import sptech.salonTime.exception.DataErradaException
import java.time.LocalDateTime

interface HistoricoAgendamentoDto {
    fun getStatusAgendamento(): String
    fun getDataHora(): LocalDateTime
}

