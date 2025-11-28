package sptech.salonTime.mapper

import org.springframework.stereotype.Component
import sptech.salonTime.dto.AvaliacaoDto
import sptech.salonTime.entidade.Avaliacao
import java.time.LocalDateTime

@Component
class AvaliacaoMapper {
    companion object {
        fun toDto(avaliacao: Avaliacao): AvaliacaoDto {
            return AvaliacaoDto(
                id = avaliacao.id,
                nomeServico = avaliacao.agendamento?.servico?.nome ?: "Serviço não informado",
                dataAgendamento = (avaliacao.agendamento?.data ?: "Data não informada").toString(),
                agendamentoId = avaliacao.agendamento?.id ?: 0,
                nomeFuncionario = avaliacao.agendamento?.funcionario?.nome ?: "Funcionário não informado",
                idUsuario = avaliacao.usuario?.id ?: 0,
                nomeUsuario = avaliacao.agendamento?.usuario?.nome ?: "Usuário não informado",
                notaServico = avaliacao.notaServico ?: 0,
                descricaoServico = avaliacao.descricaoServico ?: "",
                dataHorario = avaliacao.dataHorario ?: LocalDateTime.now()
            )
        }

        fun toDtoList(avaliacoes: List<Avaliacao>): List<AvaliacaoDto> {
            return avaliacoes.map { toDto(it) }
        }
    }
}
