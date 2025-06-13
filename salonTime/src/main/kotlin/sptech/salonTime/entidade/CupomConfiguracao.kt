package sptech.salonTime.entidade

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "cupom_configuracao")
data class CupomConfiguracao(

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    var id: Int? = null,
    var intervaloAtendimento: Int? = null,
    var porcetagemDesconto: Int? = null
){}