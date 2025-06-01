package sptech.salonTime.dto

import sptech.salonTime.entidade.Cupom
import sptech.salonTime.entidade.Pagamento
import sptech.salonTime.entidade.Servico
import sptech.salonTime.entidade.StatusAgendamento
import sptech.salonTime.repository.InfoSalaoRepository

data class AgendamentoDto(
    val id: Int?,
    val servico: Servico?,
    val usuario: UsuarioPublicoDto?,
    val funcionario: UsuarioPublicoDto?,
    val statusAgendamento: StatusAgendamento?,
    val pagamento: Pagamento?,
    val cupom: Cupom,
    val data: String?,
    val inicio: String?,
    val fim: String?,
    val preco: Double?
)
