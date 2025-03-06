package sptech.salonTime

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDate

/*
Para que yum atributo não seja exposto no JSON gerado
Podemos:
1 - marcar ele como private (desvantagem: nem no código teremos acesso público a ele)
2 - anotar o atributo com @JsonIgnore
 */
data class Clientes (
    var nome: String? = null,
    var cpf: String? = null,
    var email: String? = null,
    var senha: String? = null
) {

}
