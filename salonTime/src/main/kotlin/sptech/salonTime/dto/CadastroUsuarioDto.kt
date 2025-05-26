package sptech.salonTime.dto

data class CadastroUsuarioDto(
    val nome: String,
    val email: String,
    val senha: String,
    val telefone: String? = null
)
