package sptech.salonTime.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import sptech.salonTime.dto.CadastroUsuarioDto
import sptech.salonTime.dto.LoginDto
import sptech.salonTime.dto.SenhaDto
import sptech.salonTime.entidade.TipoUsuario
import sptech.salonTime.entidade.Usuario
import sptech.salonTime.exception.UsuarioNaoEncontradoException
import sptech.salonTime.repository.FuncionamentoRepository
import sptech.salonTime.repository.TipoUsuarioRepository
import sptech.salonTime.repository.UsuarioRepository
import java.time.LocalDateTime
import java.util.*

class UsuarioServiceTest {

    private val usuarioRepository = mock(UsuarioRepository::class.java)
    private val tipoUsuarioRepository = mock(TipoUsuarioRepository::class.java)
    private val funcionametoRepository = mock(FuncionamentoRepository::class.java)
    private val service = UsuarioService(usuarioRepository, tipoUsuarioRepository, funcionametoRepository)


    val dto = CadastroUsuarioDto(
        nome = "João da Silva",
        email = "joao@email.com",
        senha = "senha123",
        telefone = "11999999999"
    )

    val usuarioMock = Usuario(
        id = 1,
        tipoUsuario = TipoUsuario(2, "CLIENTE"),
        nome = dto.nome,
        telefone = dto.telefone,
        cpf = "12345678900",
        email = dto.email,
        senha = dto.senha,
        dataNascimento = null, // data válida
        dataCriacao = LocalDateTime.now(),
        foto = null,
        login = false,
        ativo = true
    )


    @Test
    fun `salvarUsuario deve criar usuário com tipo CLIENTE`() {
        val dto = CadastroUsuarioDto("João", "joao@email.com", "senha123", "11999999999")
        val usuario = usuarioMock

        `when`(usuarioRepository.save(any(Usuario::class.java))).thenReturn(usuario)

        val resultado = service.salvarUsuario(dto)

        assertNotNull(resultado)
        assertEquals("João da Silva", resultado.nome)
        verify(usuarioRepository).save(any())
    }

    @Test
    fun `listar deve retornar lista vazia se não houver usuários ativos`() {
        `when`(usuarioRepository.findAllByAtivoTrue()).thenReturn(emptyList())

        val lista = service.listar()

        assertTrue(lista.isEmpty())
        verify(usuarioRepository).findAllByAtivoTrue()
    }

    @Test
    fun `listarPorId deve retornar usuário quando existir`() {
        val usuario = usuarioMock

        `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario))

        val resultado = service.listarPorId(1)

        assertNotNull(resultado)
        assertEquals("João da Silva", resultado.nome)
        verify(usuarioRepository).findById(1)
    }

    @Test
    fun `excluir deve desativar o usuário`() {
        val usuario = usuarioMock

        `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario))
        `when`(usuarioRepository.save(any())).thenReturn(usuario)

        service.excluir(1)

        verify(usuarioRepository).findById(1)
    }

    @Test
    fun `mudarSenha deve alterar a senha do usuário`() {
        val usuario = usuarioMock

        val novaSenha = SenhaDto("senha123", "novaSenha123")

        `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario))
        `when`(usuarioRepository.save(any())).thenReturn(usuario.copy(senha = novaSenha.novaSenha))

        val atualizado = service.mudarSenha(1, novaSenha)

        assertEquals("novaSenha123", atualizado.senha)
        verify(usuarioRepository).save(any())
    }

    @Test
    fun `login deve ativar login quando credenciais estao corretas`() {
        val usuario = Usuario(
            id = 1,
            tipoUsuario = TipoUsuario(2, "CLIENTE"),
            nome = "Maria",
            telefone = "11999999999",
            cpf = "12345678900",
            email = "m@gmail.com",
            senha = "123",
            login = false
        )

        val loginDto = LoginDto(
            email = "m@gmail.com",
            senha = "123"
        )

        // Mock do método usado no login
        `when`(usuarioRepository.login(loginDto.email, loginDto.senha))
            .thenReturn(usuario)

        // Mockando o save: o retorno deve ser o usuário já com login = true
        `when`(usuarioRepository.save(any()))
            .thenAnswer { invocation ->
                val u = invocation.arguments[0] as Usuario
                u.copy(login = true)
            }

        val resultado = service.login(loginDto)

        // Verificações
        assertTrue(resultado.login)
        assertEquals("m@gmail.com", resultado.email)
        verify(usuarioRepository).login("m@gmail.com", "123")
        verify(usuarioRepository).save(any())
    }


}