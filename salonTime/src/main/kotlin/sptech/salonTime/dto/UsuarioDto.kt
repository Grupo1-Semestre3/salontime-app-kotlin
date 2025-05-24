package sptech.salonTime.dto

data class UsuarioDto(
    val id: Int?,
    val tipoUsuario: TipoUsuarioDto?,
    val nome: String?,
    val telefone: String?,
    val email: String?,
    val foto: String?
)
