package sptech.salonTime.entidade

import jakarta.persistence.*

@Entity
class FuncionarioCompetencia(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @ManyToOne
    var funcionario: Usuario? = null,

    @ManyToOne
    var servico: Servico? = null,
) {
    constructor() : this(null, Usuario(), Servico())
}
