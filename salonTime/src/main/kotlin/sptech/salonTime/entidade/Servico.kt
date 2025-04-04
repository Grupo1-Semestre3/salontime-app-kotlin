package sptech.salonTime.entidade

import jakarta.persistence.*
import java.time.LocalTime

@Entity
@Table(name = "servico")
data class Servico(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    val nome: String? = null,
    val preco: Double? = null,
    val tempo: LocalTime? = null,
    val status: String? = null,
    val simultaneo: Boolean? = false
){
    constructor() : this(0, null, null, null, null, null)
}

