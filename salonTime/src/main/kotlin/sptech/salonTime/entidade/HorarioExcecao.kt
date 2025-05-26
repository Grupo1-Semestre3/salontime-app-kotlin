package sptech.salonTime.entidade
import jakarta.persistence.*
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.NotNull
import java.time.*

@Entity
@Table(name = "horario_excecao")
data class HorarioExcecao(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @NotNull
    @FutureOrPresent
    val dataInicio: LocalDate,
    @NotNull
    @FutureOrPresent
    val dataFim: LocalDate,
    @NotNull
    @FutureOrPresent
    val inicio: LocalTime,
    @NotNull
    @FutureOrPresent
    val fim: LocalTime,
    @NotNull
    val aberto: Boolean,
    @NotNull
    val capacidade: Int
)
