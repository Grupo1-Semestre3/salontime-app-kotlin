package sptech.salonTime.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import sptech.salonTime.dto.EmailDto
import sptech.salonTime.dto.SenhaDto
import sptech.salonTime.entidade.Usuario
import sptech.salonTime.exception.UsuarioNaoEncontradoException
import sptech.salonTime.repository.UsuarioRepository
import java.util.*

class UsuarioServiceTest {

 private val repository = mock(UsuarioRepository::class.java)
 private val service = UsuarioService(repository)

 @Test
 fun `listar deve retornar todos usuários`() {
  val usuarios = listOf(Usuario(), Usuario())
  `when`(repository.findAll()).thenReturn(usuarios)

  val result = service.listar()

  assertEquals(usuarios, result)
  verify(repository).findAll()
 }

 @Test
 fun `salvar deve salvar usuário`() {
  val usuario = Usuario()
  `when`(repository.save(usuario)).thenReturn(usuario)

  val result = service.salvar(usuario)

  assertEquals(usuario, result)
  verify(repository).save(usuario)
 }

 @Test
 fun `listarPorId deve retornar o usuário`() {
  val usuario = Usuario()
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
 fun `atualizar deve fazer update e mostrar usuário`() {
  val usuario = Usuario()
  val usuarioAtualizado = Usuario()
  `when`(repository.findById(1)).thenReturn(Optional.of(usuario))
  `when`(repository.save(usuarioAtualizado)).thenReturn(usuarioAtualizado)

  val result = service.atualizar(1, usuarioAtualizado)

  assertEquals(usuarioAtualizado, result)
  verify(repository).save(usuarioAtualizado)
 }

 @Test
 fun `login should set login to true and return the user`() {
  val usuario = Usuario()
  `when`(repository.findById(1)).thenReturn(Optional.of(usuario))
  `when`(repository.save(usuario)).thenReturn(usuario)

  val result = service.login(1)

  assertTrue(result.login)
  verify(repository).save(usuario)
 }

 @Test
 fun `logoff should set login to false and return the user`() {
  val usuario = Usuario()
  `when`(repository.findById(1)).thenReturn(Optional.of(usuario))
  `when`(repository.save(usuario)).thenReturn(usuario)

  val result = service.logoff(1)

  assertFalse(result.login)
  verify(repository).save(usuario)
 }

 @Test
 fun `mudarSenha should update and return the user with new password`() {
  val usuario = Usuario()
  val novaSenha = SenhaDto("novaSenha")
  `when`(repository.findById(1)).thenReturn(Optional.of(usuario))
  `when`(repository.save(usuario)).thenReturn(usuario)

  val result = service.mudarSenha(1, novaSenha)

  assertEquals("novaSenha", result.senha)
  verify(repository).save(usuario)
 }

 @Test
 fun `mudarEmail should update and return the user with new email`() {
  val usuario = Usuario()
  val novoEmail = EmailDto("novo@email.com")
  `when`(repository.findById(1)).thenReturn(Optional.of(usuario))
  `when`(repository.save(usuario)).thenReturn(usuario)

  val result = service.mudarEmail(1, novoEmail)

  assertEquals("novo@email.com", result.email)
  verify(repository).save(usuario)
 }

 @Test
 fun `atualizarFoto should update and return the user's photo`() {
  val usuario = Usuario()
  val foto = byteArrayOf(1, 2, 3)
  `when`(repository.findById(1)).thenReturn(Optional.of(usuario))
  `when`(repository.save(usuario)).thenReturn(usuario)

  val result = service.atualizarFoto(1, foto)

  assertArrayEquals(foto, result)
  verify(repository).save(usuario)
 }

 @Test
 fun `getFoto should return the user's photo`() {
  val usuario = Usuario()
  val foto = byteArrayOf(1, 2, 3)
  usuario.foto = foto
  `when`(repository.findById(1)).thenReturn(Optional.of(usuario))

  val result = service.getFoto(1)

  assertArrayEquals(foto, result)
  verify(repository).findById(1)
 }

 @Test
 fun `should throw exception when user not found`() {
  `when`(repository.findById(1)).thenReturn(Optional.empty())

  assertThrows(UsuarioNaoEncontradoException::class.java) {
   service.getFoto(1)
  }
 }
}