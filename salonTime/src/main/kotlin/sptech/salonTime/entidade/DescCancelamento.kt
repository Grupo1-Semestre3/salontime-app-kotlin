package sptech.salonTime.entidade

import jakarta.persistence.*
import jakarta.validation.constraints.*

@Entity
@Table(name = "desc_cancelamento")
data class DescCancelamento(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,
    @Size(max = 250)
    var descricao: String? = null,
    @NotNull
    var agendamento: Int?
)
