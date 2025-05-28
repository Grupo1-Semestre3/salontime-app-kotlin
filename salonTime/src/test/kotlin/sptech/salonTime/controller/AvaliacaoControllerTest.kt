package sptech.salonTime.controller

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import sptech.salonTime.dto.avalicao.AtualizarAvaliacaoDto
import sptech.salonTime.dto.avalicao.CadastroAvaliacaoDto
import sptech.salonTime.entidade.Agendamento
import sptech.salonTime.entidade.Avaliacao
import sptech.salonTime.entidade.Usuario
import sptech.salonTime.service.AvaliacaoService
import java.time.LocalDateTime
import kotlin.test.assertEquals

class AvaliacaoControllerTest {

 private lateinit var service: AvaliacaoService
 private lateinit var controller: AvaliacaoController

 private val avaliacao = Avaliacao(
  id = 1,
  agendamento = null,
  usuario = null,
  notaServico = 5,
  descricaoServico = "Excelente",
  dataHorario = LocalDateTime.now().plusDays(1)
 )

 @BeforeEach
 fun setup() {
  service = mock(AvaliacaoService::class.java)
  controller = AvaliacaoController(service)
 }

 @Test
 fun get() {
  `when`(service.listar()).thenReturn(listOf(avaliacao))

  val response = controller.get()

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(1, response.body?.size)
  verify(service).listar()
 }

 @Test
 fun getById() {
  `when`(service.buscarPorId(1)).thenReturn(avaliacao)

  val response = controller.getById(1)

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(avaliacao, response.body)
  verify(service).buscarPorId(1)
 }

 @Test
 fun post() {
  val dto = CadastroAvaliacaoDto(
   agendamento = Agendamento(),
   usuario = Usuario(),
   notaServico = 4,
   descricaoServico = "Muito bom",
   dataHorario = LocalDateTime.now()
  )
  `when`(service.cadastrar(dto)).thenReturn(avaliacao)

  val response = controller.post(dto)

  assertEquals(HttpStatus.CREATED, response.statusCode)
  assertEquals(avaliacao, response.body)
  verify(service).cadastrar(dto)
 }

 @Test
 fun put() {
  val dto = AtualizarAvaliacaoDto(
   notaServico = 3,
   descricaoServico = "Bom",
  )
  `when`(service.atualizar(1, dto)).thenReturn(avaliacao)

  val response = controller.put(1, dto)

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(avaliacao, response.body)
  verify(service).atualizar(1, dto)
 }
}
