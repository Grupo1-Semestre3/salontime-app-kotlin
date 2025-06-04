package sptech.salonTime.entidade
import jakarta.persistence.*
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.NotNull
import java.time.*

@Entity
@Table(name = "horario_excecao")
data class HorarioExcecao(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,
    @NotNull
    @FutureOrPresent
    var dataInicio: LocalDate,
    @NotNull
    @FutureOrPresent
    var dataFim: LocalDate,
    @NotNull
    @FutureOrPresent
    var inicio: LocalTime,
    @NotNull
    @FutureOrPresent
    var fim: LocalTime,
    @NotNull
    var aberto: Boolean,
    @NotNull
    var capacidade: Int,
    @ManyToOne
    var funcionario: Usuario? = null
)
