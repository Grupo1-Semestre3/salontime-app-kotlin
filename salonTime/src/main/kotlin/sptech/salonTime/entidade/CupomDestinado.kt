package sptech.salonTime.entidade

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.validation.constraints.NotNull
import jakarta.persistence.Entity
import sptech.salonTime.entidade.Cupom
import sptech.salonTime.entidade.Usuario

@Entity
class CupomDestinado(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @ManyToOne
    var cupom: Cupom? = null,

    @ManyToOne
    var usuario: Usuario? = null,

    @NotNull
    var usado: Boolean? = null,
) {

    constructor() : this(null, Cupom(), Usuario(), null)

}