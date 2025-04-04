package sptech.salonTime.entidade

import jakarta.persistence.*

@Entity
@Table(name = "tipo_usuario")
data class TipoUsuario(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    val descricao: String? = null
){
    constructor() : this(0, null)
}
