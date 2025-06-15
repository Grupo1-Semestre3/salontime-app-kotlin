package sptech.salonTime.controller

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import sptech.salonTime.dto.ServicoDto
import sptech.salonTime.entidade.Servico
import sptech.salonTime.service.ServicoService
import java.time.LocalTime
import kotlin.test.assertEquals

class ServicoControllerTest {

 private lateinit var service: ServicoService
 private lateinit var controller: ServicoController

 private val servico = Servico(
  id = 1,
  nome = "Corte de cabelo",
  preco = 50.0,
  tempo = LocalTime.of(0, 30),
  status = "ATIVO",
  simultaneo = false,
  descricao = "Serviço básico",
  foto = null
 )
 private val servicoDto = ServicoDto(
  id = 1,
  nome = "Corte de cabelo",
  preco = 50.0,
  tempo = LocalTime.of(0, 30),
  status = "ATIVO",
  simultaneo = false,
  descricao = "Serviço básico",
    mediaAvaliacao = 4.5
 )

 @BeforeEach
 fun setup() {
  service = mock(ServicoService::class.java)
  controller = ServicoController(service)
 }

 @Test
 @DisplayName("Consulta todos os serviços ativos")
 fun listarAtivos() {
  `when`(service.listarAtivos()).thenReturn(listOf(servicoDto))

  val response = controller.listarAtivos()

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(1, response.body?.size)
  verify(service).listarAtivos()
 }

 @Test
 @DisplayName("Consulta todos os serviços desativados")
    fun listarDesativados() {
    `when`(service.listarDesativados()).thenReturn(listOf(servicoDto))

    val response = controller.listarDesativados()

    assertEquals(HttpStatus.OK, response.statusCode)
    assertEquals(1, response.body?.size)
    verify(service).listarDesativados()
    }

 @Test
 @DisplayName("Consulta serviço por ID")
 fun listarPorId() {
  `when`(service.listarPorId(1)).thenReturn(servicoDto)

  val response = controller.listarPorId(1)

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(servicoDto, response.body)
  verify(service).listarPorId(1)
 }

 @Test
 @DisplayName("Desativa um serviço")
 fun desativar() {
  `when`(service.desativar(1)).thenReturn(true)

  val response = controller.desativar(1)

  assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
  verify(service).desativar(1)
 }

 @Test
 @DisplayName("Ativa um serviço")
 fun ativar() {
  `when`(service.ativar(1)).thenReturn(true)

  val response = controller.ativar(1)

  assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
  verify(service).ativar(1)
 }

 @Test
 @DisplayName("Cria um novo serviço")
 fun criar() {
  `when`(service.criar(servico)).thenReturn(servico)

  val response = controller.criar(servico)

  assertEquals(HttpStatus.CREATED, response.statusCode)
  assertEquals(servico, response.body)
  verify(service).criar(servico)
 }

 @Test
 @DisplayName("Atualiza um serviço")
 fun atualizar() {
  `when`(service.listarPorId(1)).thenReturn(servicoDto)
  `when`(service.atualizar(1, servico)).thenReturn(servico)

  val response = controller.atualizar(1, servico)

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(servico, response.body)
  verify(service).atualizar(1, servico)
 }

 @Test
 @DisplayName("Ativa simultaneamente um serviço")
 fun ativarSimultaneo() {
  `when`(service.ativarSimultaneo(1)).thenReturn(true)

  val response = controller.ativarSimultaneo(1)

  assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
  verify(service).ativarSimultaneo(1)
 }

 @Test
 @DisplayName("Desativa simultaneamente um serviço")
 fun desativarSimultaneo() {
  `when`(service.desativarSimultaneo(1)).thenReturn(true)

  val response = controller.desativarSimultaneo(1)

  assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
  verify(service).desativarSimultaneo(1)
 }

 @Test
 @DisplayName("Mudar foto de um serviço")
    fun mudarFoto() {
  val id = 1
  val foto: ByteArray = "nova_foto.jpg".toByteArray()
  `when`(service.atualizarFoto(id, foto)).thenReturn(foto)

  val response = controller.patchFoto(id, foto)

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(foto, response.body)
  verify(service).atualizarFoto(id, foto)
 }

 @Test
 @DisplayName("Exibir a foto de um serviço")
    fun exibirFoto() {
    val id = 1
    val foto: ByteArray = "foto.jpg".toByteArray()
    `when`(service.getFoto(id)).thenReturn(foto)

    val response = controller.getFoto(id)

    assertEquals(HttpStatus.OK, response.statusCode)
    assertEquals(foto, response.body)
    verify(service).getFoto(id)
    }
}
