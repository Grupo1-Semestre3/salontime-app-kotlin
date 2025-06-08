package sptech.salonTime.entidade

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.*
import org.hibernate.validator.constraints.Length
import java.time.LocalTime

@Entity
@Table(name = "servico")
data class Servico(
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id // do pacote jakarta.persistence- transforma em pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Int,
    @NotBlank
    @Size(min = 1, max = 50)
    var nome: String?,
    @PositiveOrZero
    var preco: Double?,
    @NotBlank
    var tempo: LocalTime?,
    @NotNull
    @Pattern(regexp = "ATIVO|INATIVO")
    var status: String? = null,
    @NotNull
    var simultaneo: Boolean? = false,
    @Length(max = 255)
    var descricao: String? = null,
    @Size(max = 10_485_760)
    var foto: ByteArray? = null,
){
    constructor() : this(0, null, null, null, null, null)
}

