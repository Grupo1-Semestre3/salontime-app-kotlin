package sptech.salonTime.entidade

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate
import java.time.LocalTime

@Entity
@Table(name = "cupom")
data class Cupom(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @field:Size(min = 1, max = 45)
    var nome: String? = null,

    @field:Size(max = 45)
    var descricao: String? = null,

    @field:Size(max = 45)
    var codigo: String? = null,

    @NotNull
    var ativo: Boolean? = true,

    @NotNull
    var inicio: LocalDate? = null,

    @NotNull
    var fim: LocalDate? = null,

    @field:Size(max = 45)
    var tipoDestinatario: String? = null,

    @NotNull
    var desconto: Int? = null
)


