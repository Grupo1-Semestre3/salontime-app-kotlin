package sptech.salonTime.dto

data class DescCancelamentoDto(
    val id: Int,
    val descricao: String,
    val agendamentoId: Int,
    val nomeServico: String,
    val dataServico: String,
    val nomeCliente: String,
    val emailCliente: String,
    val nomeFuncionario: String,
    val emailFuncionario: String
)
