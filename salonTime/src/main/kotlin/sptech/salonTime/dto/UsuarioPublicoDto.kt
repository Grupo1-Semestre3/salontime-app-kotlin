package sptech.salonTime.dto

import sptech.salonTime.entidade.TipoUsuario
import sptech.salonTime.service.TipoUsuarioService

data class UsuarioPublicoDto(
    val id: Int?,
    val tipoUsuario: TipoUsuario?,
    val nome: String?,
    val email: String?,
    val login: Boolean?,
    val telefone: String? = null
) {
}