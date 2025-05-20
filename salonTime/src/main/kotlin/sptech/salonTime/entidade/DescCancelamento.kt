package sptech.salonTime.entidade

import jakarta.persistence.*
import jakarta.validation.constraints.*

@Entity
@Table(name = "desc_cancelamento")
data class DescCancelamento(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @Size(max = 250)
    val descricao: String? = null,
    @NotNull
    val agendamento: Int?
)
