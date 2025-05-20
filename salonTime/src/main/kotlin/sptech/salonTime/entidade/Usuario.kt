package sptech.salonTime.entidade

/*
Para que yum atributo não seja exposto no JSON gerado
Podemos:
1 - marcar ele como private (desvantagem: nem no código teremos acesso público a ele)
2 - anotar o atributo com @JsonIgnore
 */


import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.br.CPF
import java.sql.Date

@Entity
@Table(name = "usuario")
data class Usuario(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @ManyToOne
<<<<<<< HEAD
    @NotNull
=======
>>>>>>> 0de2042596b636e0e303d661ba82983a1a81cb60
    val tipoUsuario: TipoUsuario?,

    @NotNull
    @Size(min = 3, max = 50)
    val nome: String? = null,

    @NotNull
    @Size(min = 11, max = 11)
    val telefone: String? = null,

    @NotNull
    @CPF
    val cpf: String? = null,

    @NotNull
    @Email
    var email: String? = null,

    @Size(min = 6, max = 40)
    @NotNull
    var senha: String? = null,

    @NotNull
    var dataNascimento: Date? = null,

    @Size(max = 20_971_520, message = "A foto não pode exceder 20MB")
    var foto: ByteArray? = null,

    @NotNull
    var login: Boolean = false
){
    constructor() : this(0, null, null, null, null, null, null, null, null, false)
}