package sptech.salonTime.entidade

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "cupom_configuracao")
data class cupomConfiguracao(

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    val id: Int? = null,
    val intervaloAtendimento: Int? = null,
    val porcetagemDesconto: Int? = null
){}