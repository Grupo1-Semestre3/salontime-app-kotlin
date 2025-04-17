package sptech.salonTime.entidade

import jakarta.persistence.*

@Entity
@Table(name = "pagamento")
data class Pagamento (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    var forma: String? = null



){
        constructor():this(0, null)
}