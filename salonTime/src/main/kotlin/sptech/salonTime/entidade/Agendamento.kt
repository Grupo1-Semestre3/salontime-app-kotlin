package sptech.salonTime.entidade
import jakarta.persistence.*
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.NotNull
import java.time.*

@Entity
@Table(name = "agendamento")
data class Agendamento(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @ManyToOne
    @NotNull
    var servico: Servico? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    var usuario: Usuario? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    var funcionario: Usuario? = null,

    @ManyToOne
    @NotNull
    var statusAgendamento: StatusAgendamento? = null,

    @ManyToOne
    @NotNull
    var pagamento: Pagamento? = null,

    @NotNull
    @FutureOrPresent
    val data: LocalDate? = null,

    @NotNull
    val inicio: LocalTime? = null,

    @NotNull
    val fim: LocalTime? = null,

    @NotNull
    @DecimalMin("0.0", inclusive = false)
    val preco: Double? = null
){
    constructor() : this(
        null,
        Servico(),
        Usuario(),
        Usuario(),
        StatusAgendamento(),
        Pagamento(),
        LocalDate.now(),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0),
        1.0
    )
}
