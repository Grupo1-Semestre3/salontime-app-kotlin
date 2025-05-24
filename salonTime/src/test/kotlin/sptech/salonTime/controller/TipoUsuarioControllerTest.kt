package sptech.salonTime.controller

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import sptech.salonTime.dto.TPUDescricaoDto
import sptech.salonTime.entidade.TipoUsuario
import sptech.salonTime.service.TipoUsuarioService
import kotlin.test.assertEquals

class TipoUsuarioControllerTest {

 private lateinit var service: TipoUsuarioService
 private lateinit var controller: TipoUsuarioController

 private val tipoUsuario = TipoUsuario(
  id = 1,
  descricao = "Administrador Geral"
 )

 @BeforeEach
 fun setup() {
  service = mock(TipoUsuarioService::class.java)
  controller = TipoUsuarioController(service)
 }

 @Test
 @DisplayName("Consulta todos os tipos de usuário")
 fun listar() {
  `when`(service.listar()).thenReturn(listOf(tipoUsuario))

  val response = controller.listar()

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(1, response.body?.size)
  verify(service).listar()
 }

 @Test
 @DisplayName("Cria um novo tipo de usuário")
 fun criar() {
  `when`(service.salvar(tipoUsuario)).thenReturn(tipoUsuario)

  val response = controller.criar(tipoUsuario)

  assertEquals(HttpStatus.CREATED, response.statusCode)
  assertEquals(tipoUsuario, response.body)
  verify(service).salvar(tipoUsuario)
 }

 @Test
 @DisplayName("Consulta tipo de usuário por ID")
 fun listarPorId() {
  `when`(service.listarPorId(1)).thenReturn(tipoUsuario)

  val response = controller.listarPorId(1)

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(tipoUsuario, response.body)
  verify(service).listarPorId(1)
 }

 @Test
 @DisplayName("Exclui um tipo de usuário")
 fun excluir() {
  `when`(service.listarPorId(1)).thenReturn(tipoUsuario)
  doNothing().`when`(service).excluir(1)

  val response = controller.excluir(1)

  assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
  verify(service).excluir(1)
 }

 @Test
 @DisplayName("Altera descrição de um tipo de usuário")
 fun mudarDescricao() {
  val tpuDescricaoDto = TPUDescricaoDto("Administrador Financeiro")
  val tipoAtualizado = tipoUsuario.copy(descricao = tpuDescricaoDto.descricao)


  `when`(service.listarPorId(1)).thenReturn(tipoUsuario)
  `when`(service.atualizar(1, tpuDescricaoDto)).thenReturn(tipoAtualizado)

  val response = controller.mudarDescricao(1, tpuDescricaoDto)

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(tpuDescricaoDto.descricao, response.body?.descricao)
  verify(service).atualizar(1, tpuDescricaoDto)
 }
}
