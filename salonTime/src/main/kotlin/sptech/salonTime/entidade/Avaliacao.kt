package sptech.salonTime.entidade
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "avaliacao")
data class Avaliacao(
    @Id
    @Column(name = "fk_agendamento")
    val id: Int = 0,

    @OneToOne
    @JoinColumn(name = "fk_agendamento", insertable = false, updatable = false)
    val agendamento: Agendamento,

    @Column(name = "nota_servico")
    val notaServico: Int,

    @Column(name = "descricao_servico")
    val descricaoServico: String? = null,

    @Column(name = "data_horario")
    val dataHorario: LocalDateTime
)

