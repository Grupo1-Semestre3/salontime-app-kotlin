package sptech.salonTime.entidade
import jakarta.persistence.*

@Entity
@Table(name = "status_agendamento")
data class StatusAgendamento(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    val status: String? = null
){

}
