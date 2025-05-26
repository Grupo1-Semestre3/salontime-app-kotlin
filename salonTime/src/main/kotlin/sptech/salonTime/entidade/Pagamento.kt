package sptech.salonTime.entidade

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity
@Table(name = "pagamento")
data class Pagamento (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,
    @NotBlank
    @Size(max = 50)
    var forma: String? = null,
    @NotNull
    var taxa: Double? = null

){
    constructor():this(0, null, null)
}