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
    var dataInicio: LocalDate? = null,
    @NotNull
    @FutureOrPresent
    var dataFim: LocalDate? = null,
    @NotNull
    @FutureOrPresent
    var inicio: LocalTime? = null,
    @NotNull
    @FutureOrPresent
    var fim: LocalTime? = null,
    var aberto: Boolean? = null,

    var capacidade: Int? = null,
    @ManyToOne
    var funcionario: Usuario? = null
){
    constructor() : this(
        0,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )
}
