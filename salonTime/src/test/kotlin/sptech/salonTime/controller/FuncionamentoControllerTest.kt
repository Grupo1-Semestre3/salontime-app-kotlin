package sptech.salonTime.controller

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import sptech.salonTime.entidade.DiaSemana
import sptech.salonTime.entidade.Funcionamento
import sptech.salonTime.service.FuncionamentoService
import java.time.LocalTime
import kotlin.test.assertEquals

class FuncionamentoControllerTest {

 private lateinit var service: FuncionamentoService
 private lateinit var controller: FuncionamentoController

 private val funcionamento = Funcionamento(
  id = 1,
  diaSemana = DiaSemana.MONDAY,
  inicio = LocalTime.of(9, 0),
  fim = LocalTime.of(18, 0),
  aberto = true,
  capacidade = 10
 )

 @BeforeEach
 fun setup() {
  service = mock(FuncionamentoService::class.java)
  controller = FuncionamentoController(service)
 }

 @Test
 fun listarFuncionamento() {
  `when`(service.listar()).thenReturn(listOf(funcionamento))

  val response = controller.listarFuncionamento()

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(1, response.body?.size)
  assertEquals(funcionamento, response.body?.get(0))
  verify(service).listar()
 }

 @Test
 fun editarFuncionamento() {
  `when`(service.editar(1, "capacidade", "20")).thenReturn(
   funcionamento.copy(capacidade = 20)
  )

  val response = controller.editarFuncionamento(1, "capacidade", "20")

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(20, response.body?.capacidade)
  verify(service).editar(1, "capacidade", "20")
 }
}
