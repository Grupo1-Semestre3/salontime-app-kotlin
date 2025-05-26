package sptech.salonTime.entidade

import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "cumpom_configuracao")
data class cumpomConfiguracao(
    val id: Int? = null,
    val intervaloAtendimento: Int? = null,
    val porcetagemDesconto: Int? = null
){}