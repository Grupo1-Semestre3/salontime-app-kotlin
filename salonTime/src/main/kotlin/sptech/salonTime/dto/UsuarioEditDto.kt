package sptech.salonTime.dto

import java.sql.Date

data class UsuarioEditDto(
    val nome: String,
    val email: String,
    val telefone: String,
    val cpf: String,
    val dataNascimento: Date,
)
