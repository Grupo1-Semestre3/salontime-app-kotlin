package sptech.salonTime.entidade

import jakarta.persistence.*

@Entity
class FuncionarioCompetencia(

    @EmbeddedId
    val id: FuncionarioCompetenciaId? = null,

    @ManyToOne
    @MapsId("funcionarioId")
    @JoinColumn(name = "funcionario_id")
    val funcionario: Usuario,

    @ManyToOne
    @MapsId("servicoId")
    @JoinColumn(name = "servico_id")
    val servico: Servico
) {
    constructor() : this(null, Usuario(), Servico())
}
