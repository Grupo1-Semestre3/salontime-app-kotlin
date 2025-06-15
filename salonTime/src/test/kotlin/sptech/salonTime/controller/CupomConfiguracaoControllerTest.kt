package sptech.salonTime.controller

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import sptech.salonTime.entidade.CupomConfiguracao
import sptech.salonTime.service.CupomConfiguracaoService
import kotlin.test.assertEquals

class CupomConfiguracaoControllerTest {

 private lateinit var service: CupomConfiguracaoService
 private lateinit var controller: CupomConfiguracaoController

 private val config = CupomConfiguracao(
  id = 1,
  intervaloAtendimento = 30,
  porcentagemDesconto = 10
 )

 @BeforeEach
 fun setUp() {
  service = mock(CupomConfiguracaoService::class.java)
  controller = CupomConfiguracaoController(service)
 }

 @Test
 fun criarCupomConfiguracao() {
  `when`(service.salvar(config)).thenReturn(config)

  val response = controller.criarCupomConfiguracao(config)

  assertEquals(HttpStatus.CREATED, response.statusCode)
  assertEquals(config, response.body)
  verify(service).salvar(config)
 }

 @Test
 fun editarCupomConfiguracao() {
  `when`(service.editar(1, config)).thenReturn(config)

  val response = controller.editarCupomConfiguracao(config)

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(config, response.body)
  verify(service).editar(1, config)
 }

 @Test
 fun editarIntervalo() {
  val novoIntervalo = 45
  val atualizado = config.copy(intervaloAtendimento = novoIntervalo)

  `when`(service.editarIntervalo(1, novoIntervalo)).thenReturn(atualizado)

  val response = controller.editarIntervalo(atualizado)

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(novoIntervalo, response.body?.intervaloAtendimento)
  verify(service).editarIntervalo(1, novoIntervalo)
 }

 @Test
 fun editarPorcentagem() {
  val novaPorcentagem = 20
  val atualizado = config.copy(porcentagemDesconto = novaPorcentagem)

  `when`(service.editarPorcentagem(1, novaPorcentagem)).thenReturn(atualizado)

  val response = controller.editarPorcentagem(atualizado)

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(novaPorcentagem, response.body?.porcentagemDesconto)
  verify(service).editarPorcentagem(1, novaPorcentagem)
 }

 @Test
 fun listarTodos() {
  `when`(service.listarTodos()).thenReturn(listOf(config))

  val response = controller.listarTodos()

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(1, response.body?.size)
  assertEquals(config, response.body?.get(0))
  verify(service).listarTodos()
 }
}
