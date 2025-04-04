package sptech.salonTime.entidade

import jakarta.persistence.*

@Entity
@Table(name = "info_salao")
data class InfoSalao(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    val email: String? = null,
    val telefone: String? = null,
    val logradouro: String? = null,
    val numero: String? = null,
    val cidade: String? = null,
    val estado: String? = null,
    val complemento: String? = null
)
