package sptech.salonTime.entidade

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size
import java.time.LocalTime

@Entity
@Table(name = "servico")
data class Servico(
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id // do pacote jakarta.persistence- transforma em pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Int?,
    @field:NotBlank @field:Size(min = 1, max = 50)
    var nome: String?,
    @field:PositiveOrZero var preco: Double?,
    var tempo: LocalTime?,
    var status: String? = null,
    var simultaneo: Boolean? = false,
    var descricao: String? = null,
    var foto: ByteArray? = null,
){
    constructor() : this(0, null, null, null, null, null)
}

