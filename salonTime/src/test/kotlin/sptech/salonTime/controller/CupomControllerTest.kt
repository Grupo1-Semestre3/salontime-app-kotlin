package sptech.salonTime.controller

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import sptech.salonTime.entidade.Cupom
import sptech.salonTime.service.CupomService
import java.time.LocalDate
import kotlin.test.assertEquals

class CupomControllerTest {

 private lateinit var service: CupomService
 private lateinit var controller: CupomController

 private val cupom = Cupom(
  id = 1,
  nome = "Desconto de Aniversário",
  descricao = "10% de desconto no mês de aniversário",
  codigo = "ANIV10",
  ativo = true,
  inicio = LocalDate.now(),
  fim = LocalDate.now().plusMonths(1),
  tipoDestinatario = "Todos"
 )

 @BeforeEach
 fun setup() {
  service = mock(CupomService::class.java)
  controller = CupomController(service)
 }

 @Test
 fun criar() {
  `when`(service.criar(cupom)).thenReturn(cupom)

  val response = controller.criar(cupom)

  assertEquals(HttpStatus.CREATED, response.statusCode)
  assertEquals(cupom, response.body)
  verify(service).criar(cupom)
 }

 @Test
 fun listarAtivos() {
  `when`(service.listarAtivos()).thenReturn(listOf(cupom))

  val response = controller.listarAtivos()

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(1, response.body?.size)
  verify(service).listarAtivos()
 }

 @Test
 fun atualizar() {
  `when`(service.atualizar(1, cupom)).thenReturn(cupom)

  val response = controller.atualizar(1, cupom)

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(cupom, response.body)
  verify(service).atualizar(1, cupom)
 }

 @Test
 fun deletar() {
  `when`(service.deletar(1)).thenReturn(true)

  val response = controller.deletar(1)

  assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
  verify(service).deletar(1)
 }
}
