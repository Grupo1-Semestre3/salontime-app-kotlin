package sptech.salonTime.entidade
import jakarta.persistence.*
import java.time.LocalTime

enum class DiaSemana {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

@Entity
@Table(name = "funcionamento")
data class Funcionamento(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana")
    val diaSemana: DiaSemana,

    val inicio: LocalTime,
    val fim: LocalTime,
    val aberto: Boolean,
    val capacidade: Int
)
