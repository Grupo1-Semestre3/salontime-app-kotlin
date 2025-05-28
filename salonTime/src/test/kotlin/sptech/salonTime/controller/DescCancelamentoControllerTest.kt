package sptech.salonTime.controller

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import sptech.salonTime.entidade.DescCancelamento
import sptech.salonTime.service.DescCancelamentoService
import sptech.salonTime.service.DescCancelamentoServiceTest

class DescCancelamentoControllerTest {
 private lateinit var service: DescCancelamentoService
 private lateinit var controller: DescCancelamentoController

 private val cancelamento = DescCancelamento(
  id = 1,
  descricao = "Cliente não compareceu",
  agendamento = 10
 )

 @BeforeEach
 fun setup() {
  service = mock(DescCancelamentoService::class.java)
  controller = DescCancelamentoController(service)
 }

 @Test
 @DisplayName("Consulta todos os cancelamentos")
 fun listar() {
  `when`(service.listar()).thenReturn(listOf(cancelamento))

  val response = controller.listar()

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(1, response.body?.size)
  verify(service).listar()
 }

 @Test
 @DisplayName("Consulta cancelamento por ID")
 fun listarPorId() {
  `when`(service.listarPorId(1)).thenReturn(cancelamento)

  val response = controller.listarPorId(1)

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(cancelamento, response.body)
  verify(service).listarPorId(1)
 }

 @Test
 @DisplayName("Cria um novo cancelamento")
 fun criar() {
  val novoCancelamento = DescCancelamento(
   descricao = "Cliente desistiu",
   agendamento = 20
  )

  `when`(service.criar(novoCancelamento)).thenReturn(cancelamento)

  val response = controller.criar(novoCancelamento)

  assertEquals(HttpStatus.CREATED, response.statusCode)
  assertEquals(cancelamento, response.body)
  verify(service).criar(novoCancelamento)
 }

 }