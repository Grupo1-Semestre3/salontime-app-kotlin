package sptech.salonTime.exception

class AgendamentoNaoEncontradoException(
    override val message: String = "Agendamento não encontrado"
): RuntimeException(message)