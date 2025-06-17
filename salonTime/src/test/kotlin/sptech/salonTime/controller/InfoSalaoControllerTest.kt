package sptech.salonTime.controller

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import sptech.salonTime.entidade.InfoSalao
import sptech.salonTime.service.InfoSalaoService
import kotlin.test.assertEquals

class InfoSalaoControllerTest {

 private lateinit var service: InfoSalaoService
 private lateinit var controller: InfoSalaoController

 private val infoMock = InfoSalao(
  id = 1,
  email = "teste@salao.com",
  telefone = "11999999999",
  logradouro = "Rua A",
  numero = "123",
  cidade = "São Paulo",
  estado = "SP",
  complemento = "Sala 1"
 )

 @BeforeEach
 fun setup() {
  service = mock(InfoSalaoService::class.java)
  controller = InfoSalaoController(service)
 }

 @Test
 @DisplayName("Deve listar informações do salão e retornar status 200 com a lista")
 fun listarInfoSalao() {
  `when`(service.listar()).thenReturn(mutableListOf(infoMock))

  val response = controller.listarInfoSalao()

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(1, response.body?.size)
  assertEquals(infoMock, response.body?.get(0))
  verify(service).listar()
 }

 @Test
 @DisplayName("Deve editar campo específico das informações do salão e retornar dados atualizados")
 fun editarInfoSalao() {
  val novoValor = "novo@email.com"
  `when`(service.editar("email", novoValor)).thenReturn(infoMock.copy(email = novoValor))

  val response = controller.editarInfoSalao("email", novoValor)

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(novoValor, response.body?.email)
  verify(service).editar("email", novoValor)
 }
}
