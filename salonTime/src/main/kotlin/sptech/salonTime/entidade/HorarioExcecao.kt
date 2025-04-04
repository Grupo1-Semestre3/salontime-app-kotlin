package sptech.salonTime.entidade
import jakarta.persistence.*
import java.time.*

@Entity
@Table(name = "horario_excecao")
data class HorarioExcecao(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(name = "data_inicio")
    val dataInicio: LocalDate,

    @Column(name = "data_fim")
    val dataFim: LocalDate,

    val inicio: LocalTime,
    val fim: LocalTime,
    val aberto: Boolean,
    val capacidade: Int
)
