package sptech.salonTime.entidade

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity
@Table(name = "tipo_usuario")
data class TipoUsuario(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @NotNull
    @Size(min = 15, max = 45)
    var descricao: String? = null
){
    constructor() : this(0, null)
}
