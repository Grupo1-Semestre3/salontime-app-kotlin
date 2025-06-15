package sptech.salonTime.controller

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import sptech.salonTime.entidade.Pagamento
import sptech.salonTime.service.PagamentoService
import kotlin.test.assertEquals

class PagamentoControllerTest {

 private lateinit var service: PagamentoService
 private lateinit var controller: PagamentoController

 private val pagamento = Pagamento(
  id = 1,
  forma = "PIX",
  taxa = 0.0
 )

 @BeforeEach
 fun setup() {
  service = mock(PagamentoService::class.java)
  controller = PagamentoController(service)
 }

 @Test
 fun `listar - deve retornar lista de pagamentos com status 200`() {
  `when`(service.listar()).thenReturn(listOf(pagamento))

  val response = controller.listar()

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(1, response.body?.size)
  assertEquals(pagamento, response.body?.get(0))
  verify(service).listar()
 }

 @Test
 fun `listarPorId - deve retornar pagamento com status 200`() {
  `when`(service.listarPorId(1)).thenReturn(pagamento)

  val response = controller.listarPorId(1)

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(pagamento, response.body)
  verify(service).listarPorId(1)
 }

 @Test
 fun `criar - deve retornar pagamento criado com status 201`() {
  `when`(service.criar(pagamento)).thenReturn(pagamento)

  val response = controller.criar(pagamento)

  assertEquals(HttpStatus.CREATED, response.statusCode)
  assertEquals(pagamento, response.body)
  verify(service).criar(pagamento)
 }

 @Test
 fun `atualizar - deve retornar pagamento atualizado com status 200`() {
  `when`(service.atualizar(1, pagamento)).thenReturn(pagamento)

  val response = controller.atualizar(1, pagamento)

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(pagamento, response.body)
  verify(service).atualizar(1, pagamento)
 }

 @Test
 fun `deletar - deve retornar status 204 se pagamento existir`() {
  `when`(service.listarPorId(1)).thenReturn(pagamento)
  doNothing().`when`(service).deletar(1)

  val response = controller.deletar(1)

  assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
  verify(service).listarPorId(1)
  verify(service).deletar(1)
 }
}
