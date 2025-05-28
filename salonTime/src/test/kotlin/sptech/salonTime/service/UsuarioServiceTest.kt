package sptech.salonTime.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import sptech.salonTime.dto.CadastroUsuarioDto
import sptech.salonTime.dto.EmailDto
import sptech.salonTime.dto.SenhaDto
import sptech.salonTime.entidade.TipoUsuario
import sptech.salonTime.entidade.Usuario
import sptech.salonTime.exception.UsuarioNaoEncontradoException
import sptech.salonTime.mapper.UsuarioMapper
import sptech.salonTime.repository.UsuarioRepository
import java.util.*

class UsuarioServiceTest {

 private val repository = mock(UsuarioRepository::class.java)
 private val service = UsuarioService(repository)

 @Test
 fun `listar deve retornar todos os usuários`() {
  val usuarios = listOf(Usuario(), Usuario())
  `when`(repository.findAll()).thenReturn(usuarios)

  val result = service.listar()

  assertEquals(usuarios, result)
  verify(repository).findAll()
 }

 @Test
 fun `salvarUsuario deve salvar e retornar o usuário como CLIENTE`() {
  val usuarioDto = CadastroUsuarioDto("usuario", "email@email.com", "senha", "11999999999")
  val usuarioEntity = UsuarioMapper.toEntity(usuarioDto)?.apply { tipoUsuario = TipoUsuario(2, "CLIENTE") }
  val usuarioSalvo = usuarioEntity!!.copy(id = 1)
  `when`(repository.save(any(Usuario::class.java))).thenReturn(usuarioSalvo)

  val result = service.salvarUsuario(usuarioDto)

  assertEquals(2, result.tipoUsuario?.id)
  assertEquals("CLIENTE", result.tipoUsuario?.descricao)
  verify(repository).save(any(Usuario::class.java))
 }

 @Test
 fun `salvarFuncionario deve salvar e retornar o usuário como FUNCIONARIO`() {
  val usuarioDto = CadastroUsuarioDto("funcionario", "email@email.com", "senha", "11999999999")
  val usuarioEntity = UsuarioMapper.toEntity(usuarioDto)?.apply { tipoUsuario = TipoUsuario(3, "FUNCIONARIO") }
  val usuarioSalvo = usuarioEntity!!.copy(id = 1)
  `when`(repository.save(any(Usuario::class.java))).thenReturn(usuarioSalvo)

  val result = service.salvarFuncionario(usuarioDto)

  assertEquals(3, result.tipoUsuario?.id)
  assertEquals("FUNCIONARIO", result.tipoUsuario?.descricao)
  verify(repository).save(any(Usuario::class.java))
 }

 @Test
 fun `listarPorId deve retornar o usuário`() {
  val usuario = Usuario().apply { id = 1 }
  `when`(repository.findById(1)).thenReturn(Optional.of(usuario))

  val result = service.listarPorId(1)

  assertEquals(usuario, result)
  verify(repository).findById(1)
 }

 @Test
 fun `excluir deve deletar o usuário por id`() {
  `when`(repository.existsById(1)).thenReturn(true)

  service.excluir(1)

  verify(repository).deleteById(1)
 }

 @Test
 fun `atualizar deve atualizar e retornar o usuário`() {
  val usuario = Usuario().apply { id = 1 }
  val usuarioAtualizado = Usuario().apply { id = 1 }
  `when`(repository.findById(1)).thenReturn(Optional.of(usuario))
  `when`(repository.save(usuarioAtualizado)).thenReturn(usuarioAtualizado)

  val result = service.atualizar(1, usuarioAtualizado)

  assertEquals(usuarioAtualizado, result)
  verify(repository).save(usuarioAtualizado)
 }

 @Test
 fun `login deve definir login como true e retornar o usuário`() {
  val usuario = Usuario().apply { id = 1 }
  `when`(repository.findById(1)).thenReturn(Optional.of(usuario))
  `when`(repository.save(usuario)).thenReturn(usuario)

  val result = service.login(1)

  assertTrue(result.login)
  verify(repository).save(usuario)
 }

 @Test
 fun `logoff deve definir login como false e retornar o usuário`() {
  val usuario = Usuario().apply { id = 1 }
  `when`(repository.findById(1)).thenReturn(Optional.of(usuario))
  `when`(repository.save(usuario)).thenReturn(usuario)

  val result = service.logoff(1)

  assertFalse(result.login)
  verify(repository).save(usuario)
 }

 @Test
 fun `mudarSenha deve atualizar e retornar o usuário com nova senha`() {
  val usuario = Usuario().apply { id = 1 }
  val novaSenha = SenhaDto("novaSenha")
  `when`(repository.findById(1)).thenReturn(Optional.of(usuario))
  `when`(repository.save(usuario)).thenReturn(usuario)

  val result = service.mudarSenha(1, novaSenha)

  assertEquals("novaSenha", result.senha)
  verify(repository).save(usuario)
 }

 @Test
 fun `mudarEmail deve atualizar e retornar o usuário com novo email`() {
  val usuario = Usuario().apply { id = 1 }
  val novoEmail = EmailDto("novo@email.com")
  `when`(repository.findById(1)).thenReturn(Optional.of(usuario))
  `when`(repository.save(usuario)).thenReturn(usuario)

  val result = service.mudarEmail(1, novoEmail)

  assertEquals("novo@email.com", result.email)
  verify(repository).save(usuario)
 }

 @Test
 fun `atualizarFoto deve atualizar e retornar a foto do usuário`() {
  val usuario = Usuario().apply { id = 1 }
  val foto = byteArrayOf(1, 2, 3)
  `when`(repository.findById(1)).thenReturn(Optional.of(usuario))
  `when`(repository.save(usuario)).thenReturn(usuario)

  val result = service.atualizarFoto(1, foto)

  assertArrayEquals(foto, result)
  verify(repository).save(usuario)
 }

 @Test
 fun `getFoto deve retornar a foto do usuário`() {
  val usuario = Usuario().apply { id = 1; foto = byteArrayOf(1, 2, 3) }
  `when`(repository.findById(1)).thenReturn(Optional.of(usuario))

  val result = service.getFoto(1)

  assertArrayEquals(byteArrayOf(1, 2, 3), result)
  verify(repository).findById(1)
 }

 @Test
 fun `verificarEmail deve retornar true se o email existir`() {
  val usuario = Usuario().apply { email = "email@email.com" }
  `when`(repository.findByEmail("email@email.com")).thenReturn(usuario)

  val result = service.verificarEmail("email@email.com")

  assertTrue(result)
  verify(repository).findByEmail("email@email.com")
 }

 @Test
 fun `verificarEmail deve retornar false se o email não existir`() {
  `when`(repository.findByEmail("email@email.com")).thenReturn(null)

  val result = service.verificarEmail("email@email.com")

  assertFalse(result)
  verify(repository).findByEmail("email@email.com")
 }
}