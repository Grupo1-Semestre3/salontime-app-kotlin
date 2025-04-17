package sptech.salonTime.entidade

/*
Para que yum atributo não seja exposto no JSON gerado
Podemos:
1 - marcar ele como private (desvantagem: nem no código teremos acesso público a ele)
2 - anotar o atributo com @JsonIgnore
 */


import jakarta.persistence.*

@Entity
@Table(name = "usuario")
data class Usuario(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @ManyToOne
    @JoinColumn(name = "fk_tipo_usuario")
    val tipoUsuario: TipoUsuario?,

    val nome: String? = null,
    val telefone: String? = null,
    val cpf: String? = null,
    val email: String? = null,
    val senha: String? = null,
    var login: Boolean = false
){
    constructor() : this(0, null, null, null, null, null, null)
}

