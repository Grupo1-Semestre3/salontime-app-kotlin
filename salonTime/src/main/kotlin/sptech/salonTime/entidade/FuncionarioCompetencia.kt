package sptech.salonTime.entidade

import jakarta.persistence.*

@Entity
class FuncionarioCompetencia(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int,

    @ManyToOne
    var funcionario: Usuario,

    @ManyToOne
    var servico: Servico,
) {
    constructor() : this(0, Usuario(), Servico())
}
