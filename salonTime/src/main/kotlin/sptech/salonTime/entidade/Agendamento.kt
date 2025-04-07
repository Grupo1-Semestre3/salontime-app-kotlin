package sptech.salonTime.entidade
import jakarta.persistence.*
import java.time.*

@Entity
@Table(name = "agendamento")
data class Agendamento(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne
    @JoinColumn(name = "fk_servico")
    val servico: Servico?,

    @ManyToOne
    @JoinColumn(name = "fk_usuario")
    val usuario: Usuario?,

    @ManyToOne
    @JoinColumn(name = "fk_status")
    val status: StatusAgendamento?,

    val data: LocalDate?,
    val inicio: LocalTime?,
    val fim: LocalTime?,
    val preco: Double?
){
    constructor():this(0, null, null, null, null, null, null, 0.0)
}
