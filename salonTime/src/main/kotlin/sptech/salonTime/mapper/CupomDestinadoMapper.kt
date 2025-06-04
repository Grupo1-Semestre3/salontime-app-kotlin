package sptech.salonTime.mapper

import sptech.salonTime.dto.CupomDestinadoDto
import sptech.salonTime.dto.UsuarioPublicoDto
import sptech.salonTime.entidade.Cupom
import sptech.salonTime.entidade.CupomDestinado

class CupomDestinadoMapper {
    companion object {
        fun toDto(cupomDestinado: CupomDestinado): CupomDestinadoDto {
            return CupomDestinadoDto(
                id = cupomDestinado.id ?: 0,
                cupom = cupomDestinado.cupom ?: Cupom(),
                usuario = UsuarioPublicoDto(
                    id = cupomDestinado.usuario?.id,
                    tipoUsuario = cupomDestinado.usuario?.tipoUsuario,
                    nome = cupomDestinado.usuario?.nome,
                    email = cupomDestinado.usuario?.email,
                    login = cupomDestinado.usuario?.login,
                    telefone = cupomDestinado.usuario?.telefone
                ),
                usado = cupomDestinado.usado ?: false
            )
        }
    }
}