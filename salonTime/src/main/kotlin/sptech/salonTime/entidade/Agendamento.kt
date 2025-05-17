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
    val id: Int = 0,

    @ManyToOne
    @NotNull
    val servico: Servico?,

    @ManyToOne
    @NotNull
    val usuario: Usuario?,

    @ManyToOne
    @NotNull
    val statusAgendamento: StatusAgendamento?,

    @ManyToOne
    @NotNull
    val pagamento: Pagamento?,

    @NotNull
    @FutureOrPresent
    val data: LocalDate?,

    @NotNull
    val inicio: LocalTime?,

    @NotNull
    val fim: LocalTime?,

    @NotNull
    @DecimalMin("0.0", inclusive = false)
    val preco: Double?
){
    constructor():this(0, null, null, null, null, null, null, null, 0.0)
}
