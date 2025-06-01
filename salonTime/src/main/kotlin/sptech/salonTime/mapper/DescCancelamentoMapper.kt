package sptech.salonTime.mapper

import sptech.salonTime.dto.DescCancelamentoDto
import sptech.salonTime.entidade.DescCancelamento

class DescCancelamentoMapper {
    companion object{
        fun toDTO(entity: DescCancelamento): DescCancelamentoDto? {
            val agendamento = entity.agendamento!!

            return agendamento.id?.let {
                DescCancelamentoDto(
                    id = entity.id,
                    descricao = entity.descricao ?: "",
                    agendamentoId = it,
                    nomeServico = agendamento.servico?.nome ?: "",
                    dataServico = agendamento.data.toString(),
                    nomeCliente = agendamento.usuario?.nome ?: "",
                    emailCliente = agendamento.usuario?.email ?: "",
                    nomeFuncionario = agendamento.funcionario?.nome ?: "",
                    emailFuncionario = agendamento.funcionario?.email ?: ""
                )
            }
        }
    }
}