package sptech.salonTime.entidade
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "log_agendamento")
data class LogAgendamento(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    val agendamentoId:Int?,

    val agendamentoFkServico:Int?,

    val agendamentoFkUsuario:Int?,

    val agendamentoFkStatus:Int?,

    val agendamentoFkPagamento: Int?,

    val inicio:LocalDateTime?,

    val fim:LocalDateTime?,

    val preco:Double?,

    val dataLog: LocalDateTime = LocalDateTime.now(),
)