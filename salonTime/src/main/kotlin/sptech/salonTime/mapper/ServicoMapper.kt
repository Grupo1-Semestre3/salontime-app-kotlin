package sptech.salonTime.mapper

import sptech.salonTime.dto.ListagemServicoDto
import sptech.salonTime.dto.ServicoDto
import sptech.salonTime.entidade.Servico
import java.time.LocalTime

class ServicoMapper {
    companion object {
        fun toDto(servico: Servico, mediaAvaliacao: Double): ListagemServicoDto {
            return ListagemServicoDto(
                id = servico.id,
                nome = servico.nome ?: "",
                preco = servico.preco ?: 0.0,
                tempo = servico.tempo ?: LocalTime.MIN,
                status = servico.status ?: "INATIVO",
                simultaneo = servico.simultaneo ?: false,
                descricao = servico.descricao,
                mediaAvaliacao = mediaAvaliacao
            )
        }
    }
}