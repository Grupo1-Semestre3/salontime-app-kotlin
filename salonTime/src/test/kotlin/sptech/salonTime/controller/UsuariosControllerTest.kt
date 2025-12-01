package sptech.salonTime.controller

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import sptech.salonTime.dto.*
import sptech.salonTime.entidade.TipoUsuario
import sptech.salonTime.entidade.Usuario
import sptech.salonTime.service.UsuarioService
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertEquals
import java.sql.Date
import java.time.LocalDate

class UsuariosControllerTest {

    private lateinit var service: UsuarioService
    private lateinit var controller: UsuariosController

    private val tipoUsuario = TipoUsuario(id = 1, descricao = "Administrador")
    private lateinit var usuario: Usuario

    @BeforeEach
    fun setup() {
        service = mock(UsuarioService::class.java)
        controller = UsuariosController(service)
        usuario = Usuario(
            id = 1,
            tipoUsuario = tipoUsuario,
            nome = "João da Silva",
            telefone = "11999999999",
            cpf = "12345678901",
            email = "joao@email.com",
            senha = "123456",
            dataNascimento = Date.valueOf(LocalDate.now()),
            dataCriacao = LocalDateTime.now(),
            foto = null,
            login = false
        )
    }

    @Test
    @DisplayName("Consulta todos os usuários")
    fun listar() {
        `when`(service.listar()).thenReturn(listOf(usuario))

        val response = controller.listar()

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(1, response.body?.size)
        verify(service).listar()
    }

    @Test
    @DisplayName("Consulta usuário por ID")
    fun listarPorid() {
        `when`(service.listarPorId(1)).thenReturn(usuario)

        val response = controller.listarPorid(1)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(usuario, response.body)
        verify(service).listarPorId(1)
    }

    @Test
    @DisplayName("Cria um novo usuário")
    fun criar() {

        val userDto = CadastroUsuarioDto(
            nome = "João da Silva",
            telefone = "11999999999",
            email = "",
            senha = "123456"
        )

        val respsonse = UsuarioPublicoDto(
            id = 1,
            tipoUsuario = tipoUsuario,
            nome = "João da Silva",
            email = "",
            login = false
        )

        `when`(service.salvarUsuario(userDto)).thenReturn(respsonse)

        val response = controller.criar(userDto)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(respsonse, response.body)
        verify(service).salvarUsuario(userDto)
    }

    @Test
    @DisplayName("Exclui um usuário")
    fun excluir() {
        `when`(service.listarPorId(1)).thenReturn(usuario)
        doNothing().`when`(service).excluir(1)

        val response = controller.excluir(1)

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        verify(service).excluir(1)
    }

    @Test
    @DisplayName("Atualiza um usuário")
    fun atualizar() {

        val usuarioAtualizado = Usuario(
            id = 1,
            tipoUsuario = tipoUsuario,
            nome = "João da Silva",
            telefone = "11999999999",
            cpf = "12345678901",
            email = ""
        )

        `when`(service.listarPorId(1)).thenReturn(usuario)
        `when`(service.atualizar(1, usuarioAtualizado)).thenReturn(usuario)

        val response = controller.atualizar(1, usuario)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(usuario, response.body)
        verify(service).atualizar(1, usuario)
    }

    @Test
    @DisplayName("Faz login do usuário")
    fun login() {

        val loginDto = LoginDto(
           email = "m@gmail.com", senha = "123456"
        )

        `when`(service.listarPorId(1)).thenReturn(usuario)
        `when`(service.login(loginDto)).thenReturn(usuario)

        val response = controller.login(loginDto)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(usuario, response.body)
        verify(service).login(loginDto)
    }

    @Test
    @DisplayName("Faz logoff do usuário")
    fun logoff() {
        `when`(service.listarPorId(1)).thenReturn(usuario)
        `when`(service.logoff(1)).thenReturn(usuario)

        val response = controller.logoff(1)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(usuario, response.body)
        verify(service).logoff(1)
    }

    @Test
    @DisplayName("Altera a senha do usuário")
    fun mudarSenha() {
        val novaSenha = SenhaDto("senha123", "novaSenha123")
        `when`(service.listarPorId(1)).thenReturn(usuario)
        `when`(service.mudarSenha(1, novaSenha)).thenReturn(usuario)

        val response = controller.mudarSenha(1, novaSenha)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(usuario, response.body)
        verify(service).mudarSenha(1, novaSenha)
    }

    @Test
    @DisplayName("Altera o email do usuário")
    fun mudarEmail() {
        val novoEmail = EmailDto("novo@email.com")
        `when`(service.listarPorId(1)).thenReturn(usuario)
        `when`(service.mudarEmail(1, novoEmail)).thenReturn(usuario)

        val response = controller.mudarEmail(1, novoEmail)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(usuario, response.body)
        verify(service).mudarEmail(1, novoEmail)
    }

    @Test
    @DisplayName("Get foto do usuário")
    fun getFoto() {
        val id = 1
        val foto: ByteArray = "foto.jpg".toByteArray()
        `when`(service.getFoto(1)).thenReturn(foto)

        val response = controller.getFoto(1)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(foto, response.body)
        verify(service).getFoto(1)
    }

    @Test
    @DisplayName("Atualiza foto do usuário")
    fun patchFoto() {
        val id = 1
        val foto: ByteArray = "nova_foto.jpg".toByteArray()
        `when`(service.atualizarFoto(id, foto)).thenReturn(foto)

        val response = controller.patchFoto(id, foto)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(foto, response.body)
        verify(service).atualizarFoto(id, foto)
    }

    @Test
    @DisplayName("Verifica email do usuário")
    fun verificarEmail() {
        val email = "joao@email.com"
        `when`(service.verificarEmail(email)).thenReturn(usuario)
        val response = controller.verificarEmail(email)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(usuario, response.body)
        verify(service).verificarEmail(email)
    }
}
