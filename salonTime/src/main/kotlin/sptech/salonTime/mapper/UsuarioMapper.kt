package sptech.salonTime.mapper

import sptech.salonTime.dto.CadastroUsuarioDto
import sptech.salonTime.dto.UsuarioPublicoDto
import sptech.salonTime.entidade.TipoUsuario
import sptech.salonTime.entidade.Usuario

class UsuarioMapper {
    companion object {

        fun toDto(usuario: Usuario): UsuarioPublicoDto {
            return UsuarioPublicoDto(
                id = usuario.id,
                tipoUsuario = usuario.tipoUsuario?.let {
                    TipoUsuario(
                        id = it.id,
                        descricao = it.descricao
                    )
                },
                nome = usuario.nome,
                email = usuario.email,
                login = usuario.login,
                telefone = usuario.telefone
            )
        }

        fun toEntity(usuario: CadastroUsuarioDto): Usuario? {
            return Usuario(
                nome = usuario.nome,
                email = usuario.email,
                senha = usuario.senha,
                telefone = usuario.telefone,
                tipoUsuario = TipoUsuario(id = 2, descricao = "CLIENTE"), // Default to Cliente
                )
        }


    }
}
