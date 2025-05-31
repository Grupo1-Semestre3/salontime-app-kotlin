package sptech.salonTime.entidade
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import java.time.LocalTime



@Entity
@Table(name = "funcionamento")
data class Funcionamento(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana")
    val diaSemana: DiaSemana? = null,

    @NotNull
    val inicio: LocalTime? = null,

    @NotNull
    val fim: LocalTime? = null,

    @NotNull
    val aberto: Boolean? = null,

    @NotNull
    val capacidade: Int? = null,

    @ManyToOne
    val funcionario: Usuario? = null
){
    constructor() : this(null, null, null, null, null, null)

}

