import kotlin.test.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import sptech.salonTime.controller.DescCancelamentoController
import sptech.salonTime.dto.DescCancelamentoDto
import sptech.salonTime.entidade.Agendamento
import sptech.salonTime.entidade.DescCancelamento
import sptech.salonTime.service.DescCancelamentoService

class DescCancelamentoControllerTest {

 private lateinit var service: DescCancelamentoService
 private lateinit var controller: DescCancelamentoController

 private val cancelamentoDto = DescCancelamentoDto(
  id = 1,
  descricao = "Cliente não compareceu",
  agendamentoId = 10,
  nomeServico = "Corte de cabelo",
  dataServico = "2025-06-01T10:00:00",
  idCliente = 5,
  nomeCliente = "João",
  emailCliente = "joao@email.com",
  nomeFuncionario = "Carlos",
  emailFuncionario = "carlos@email.com"
 )

 @BeforeEach
 fun setup() {
  service = mock(DescCancelamentoService::class.java)
  controller = DescCancelamentoController(service)
 }

 @Test
 @DisplayName("Consulta todos os cancelamentos")
 fun listar() {
  `when`(service.listar()).thenReturn(listOf(cancelamentoDto))

  val response = controller.listar()

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(1, response.body?.size)
  assertEquals(cancelamentoDto, response.body?.get(0))
  verify(service).listar()
 }

 @Test
 @DisplayName("Consulta cancelamento por ID")
 fun listarPorId() {
  `when`(service.listarPorId(1)).thenReturn(cancelamentoDto)

  val response = controller.listarPorId(1)

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(cancelamentoDto, response.body)
  verify(service).listarPorId(1)
 }

 @Test
 @DisplayName("Cria um novo cancelamento")
 fun criar() {
  val novoCancelamento = DescCancelamento(
   descricao = "Cliente desistiu",
   agendamento = mock(Agendamento::class.java)
  )

  val criado = DescCancelamentoDto(
   id = 1,
   descricao = "Cliente desistiu",
   agendamentoId = novoCancelamento.agendamento?.id ?: 0,
   nomeServico = "Serviço não informado",
   dataServico = "2025-06-01T10:00:00",
    idCliente = 5,
   nomeCliente = "Cliente não informado",
   emailCliente = "email@cliente.com",
   nomeFuncionario = "Funcionário não informado",
   emailFuncionario = "email@funcionario.com"
  )

  `when`(service.criar(novoCancelamento)).thenReturn(criado)

  val response = controller.criar(novoCancelamento)

  assertEquals(HttpStatus.CREATED, response.statusCode)
  assertEquals(criado, response.body)
  verify(service).criar(novoCancelamento)
 }
}
