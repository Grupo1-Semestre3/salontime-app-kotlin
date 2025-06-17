package sptech.salonTime.controller

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
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
 fun initMocks() {
  service = mock(CupomConfiguracaoService::class.java)
  controller = CupomConfiguracaoController(service)
 }

 @Test
 @DisplayName("Deve criar uma nova configuração de cupom")
 fun deveCriarConfiguracao() {
  `when`(service.salvar(config)).thenReturn(config)

  val response = controller.criarCupomConfiguracao(config)

  assertEquals(HttpStatus.CREATED, response.statusCode)
  assertEquals(config, response.body)
  verify(service).salvar(config)
 }

 @Test
 @DisplayName("Deve editar uma configuração existente por completo")
 fun deveEditarConfiguracaoCompleta() {
  `when`(service.editar(1, config)).thenReturn(config)

  val response = controller.editarCupomConfiguracao(config)

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(config, response.body)
  verify(service).editar(1, config)
 }

 @Test
 @DisplayName("Deve atualizar apenas o intervalo de atendimento")
 fun deveEditarSomenteIntervalo() {
  val novoIntervalo = 45
  val atualizado = config.copy(intervaloAtendimento = novoIntervalo)

  `when`(service.editarIntervalo(1, novoIntervalo)).thenReturn(atualizado)

  val response = controller.editarIntervalo(atualizado)

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(novoIntervalo, response.body?.intervaloAtendimento)
  verify(service).editarIntervalo(1, novoIntervalo)
 }

 @Test
 @DisplayName("Deve atualizar apenas a porcentagem de desconto")
 fun deveEditarSomentePorcentagem() {
  val novaPorcentagem = 20
  val atualizado = config.copy(porcentagemDesconto = novaPorcentagem)

  `when`(service.editarPorcentagem(1, novaPorcentagem)).thenReturn(atualizado)

  val response = controller.editarPorcentagem(atualizado)

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(novaPorcentagem, response.body?.porcentagemDesconto)
  verify(service).editarPorcentagem(1, novaPorcentagem)
 }

 @Test
 @DisplayName("Deve listar todas as configurações salvas")
 fun deveListarTodasConfiguracoes() {
  `when`(service.listarTodos()).thenReturn(listOf(config))

  val response = controller.listarTodos()

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(1, response.body?.size)
  assertEquals(config, response.body?.get(0))
  verify(service).listarTodos()
 }
}
