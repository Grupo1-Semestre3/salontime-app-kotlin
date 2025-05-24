package sptech.salonTime.mapper

import sptech.salonTime.dto.AgendamentoDto
import sptech.salonTime.dto.UsuarioPublicoDto
import sptech.salonTime.entidade.Agendamento
import sptech.salonTime.service.TipoUsuarioService

class AgendamentoMapper {
    companion object {
        fun toDto(agendamento: Agendamento): AgendamentoDto {
            return AgendamentoDto(
                id = agendamento.id,
                servico = agendamento.servico,
                usuario = UsuarioPublicoDto(
                    id = agendamento.usuario?.id,
                    tipoUsuario = agendamento.usuario?.tipoUsuario,
                    nome = agendamento.usuario?.nome,
                    email = agendamento.usuario?.email,
                    login = agendamento.usuario?.login
                ),
                funcionario = UsuarioPublicoDto(
                    id = agendamento.funcionario?.id,
                    tipoUsuario = agendamento.funcionario?.tipoUsuario,
                    nome = agendamento.funcionario?.nome,
                    email = agendamento.funcionario?.email,
                    login = agendamento.funcionario?.login
                ),
                statusAgendamento = agendamento.statusAgendamento,
                pagamento = agendamento.pagamento,
                data = agendamento.data.toString(),
                inicio = agendamento.inicio.toString(),
                fim = agendamento.fim.toString(),
                preco = agendamento.preco
            )
        }
    }
}
