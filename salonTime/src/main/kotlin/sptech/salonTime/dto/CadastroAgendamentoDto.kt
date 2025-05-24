package sptech.salonTime.dto

import java.time.LocalDate
import java.time.LocalTime

data class CadastroAgendamentoDto(
    val usuario: Int,
    val servico: Int,
    val funcionario: Int,
    val statusAgendamento: Int,
    val pagamento: Int,
    val data: LocalDate,
    val inicio: LocalTime,
    val fim: LocalTime,
    val preco: Double
)