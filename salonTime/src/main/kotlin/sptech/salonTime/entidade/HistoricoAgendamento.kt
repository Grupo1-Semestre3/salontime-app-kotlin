package sptech.salonTime.entidade
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "historico_agendamento")
data class HistoricoAgendamento(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(name = "data_horario")
    val dataHorario: LocalDateTime,

    @ManyToOne
    @JoinColumn(name = "agendamento_id")
    val agendamento: Agendamento
)
