package sptech.salonTime.entidade
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import java.time.LocalTime



@Entity
@Table(name = "funcionamento")
data class Funcionamento(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana")
    val diaSemana: DiaSemana,

    @NotNull
    val inicio: LocalTime,

    @NotNull
    val fim: LocalTime,

    @NotNull
    val aberto: Boolean,

    @NotNull
    val capacidade: Int
)
