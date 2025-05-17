package sptech.salonTime.entidade
import jakarta.persistence.*
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

@Entity
@Table(name = "avaliacao")
data class Avaliacao(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @OneToOne
    val agendamento: Agendamento?,

    @ManyToOne
    val usuario: Usuario?,

    @NotNull
    val notaServico: Int? = null,

    @NotNull
    val descricaoServico: String? = null,

    @NotNull
    @FutureOrPresent
    val dataHorario: LocalDateTime? = null
){
    constructor() : this(0, null, null, null, null, null)
}


