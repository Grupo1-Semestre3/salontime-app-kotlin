package sptech.salonTime.dto

import sptech.salonTime.entidade.Cupom

data class CupomDestinadoDto(
    val id: Int,
    val cupom: Cupom,
    val usuario: UsuarioPublicoDto,
    val usado: Boolean? = null
)
