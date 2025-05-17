package sptech.salonTime.dto.avalicao

import sptech.salonTime.entidade.Agendamento
import sptech.salonTime.entidade.Usuario
import java.time.LocalDateTime

data class CadastroAvaliacaoDto(val agendamento: Agendamento, val usuario: Usuario, val notaServico: Int, val descricaoServico: String, val dataHorario: LocalDateTime) {
}
