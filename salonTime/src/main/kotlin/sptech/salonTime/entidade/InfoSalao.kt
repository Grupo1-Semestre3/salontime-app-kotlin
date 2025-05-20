package sptech.salonTime.entidade

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull

@Entity
@Table(name = "info_salao")
data class InfoSalao(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @NotNull
    val email: String? = null,
    @NotNull
    val telefone: String? = null,
    @NotNull
    val logradouro: String? = null,
    @NotNull
    val numero: String? = null,
    @NotNull
    val cidade: String? = null,
    @NotNull
    val estado: String? = null,
    @NotNull
    val complemento: String? = null
)

