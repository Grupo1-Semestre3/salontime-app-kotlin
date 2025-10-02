package sptech.salonTime.dto

import jakarta.validation.constraints.NotNull

data class SenhaDto(val senhaAtual: String, val novaSenha : String)