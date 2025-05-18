package sptech.salonTime.entidade

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class FuncionarioCompetenciaId(
    @Column(name = "funcionario_id")
    val funcionarioId: Long = 0,

    @Column(name = "servico_id")
    val servicoId: Long = 0
) : Serializable
