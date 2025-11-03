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
    var cupom: Cupom? = null,

    @ManyToOne
    @NotNull
    var statusAgendamento: StatusAgendamento? = null,

    @ManyToOne
    @NotNull
    var pagamento: Pagamento? = null,

    @NotNull
    @FutureOrPresent
    var data: LocalDate? = null,

    @NotNull
        var inicio: LocalTime? = null,

    @NotNull
    var fim: LocalTime? = null,

    @NotNull
    @DecimalMin("0.0", inclusive = false)
    var preco: Double? = null
){
    constructor() : this(
        null,
        Servico(),
        Usuario(),
        Usuario(),
        Cupom(),
        StatusAgendamento(),
        Pagamento(),
        LocalDate.now(),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0),
        1.0
    )
}
