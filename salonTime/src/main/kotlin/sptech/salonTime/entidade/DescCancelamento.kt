package sptech.salonTime.entidade

import jakarta.persistence.*

@Entity
@Table(name = "desc_cancelamento")
data class DescCancelamento(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    val descricao: String? = null,

    val agendamentoId: Int?
)
