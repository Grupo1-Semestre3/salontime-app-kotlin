package sptech.salonTime.controller

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import sptech.salonTime.dto.FuncionarioCompetenciaDto
import sptech.salonTime.entidade.FuncionarioCompetencia
import sptech.salonTime.service.FuncionarioCompetenciaService
import org.springframework.http.HttpStatus
import sptech.salonTime.dto.CadastroCompetenciaFuncionario
import sptech.salonTime.dto.TipoUsuarioDto
import sptech.salonTime.dto.UsuarioDto
import sptech.salonTime.entidade.Servico
import sptech.salonTime.entidade.Usuario

class FuncionarioCompetenciaControllerTest {

 private lateinit var service: FuncionarioCompetenciaService
 private lateinit var controller: FuncionarioCompetenciaController

 private val funcionarioCompetencia = FuncionarioCompetencia(
  id = 1,
  funcionario = mock(Usuario::class.java),
  servico = mock(Servico::class.java)
 )


 private val funcionarioCompetenciaDto = CadastroCompetenciaFuncionario(
  funcionario = 1,
  servico = 1
 )


 @BeforeEach
 fun setup() {
  service = mock(FuncionarioCompetenciaService::class.java)
  controller = FuncionarioCompetenciaController(service)
 }

 @Test
 @DisplayName("Consulta todos os registros de FuncionarioCompetencia")
 fun listar() {
  `when`(service.listar()).thenReturn(listOf(funcionarioCompetencia))

  val response = controller.listar()

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(1, response.body?.size)
  verify(service).listar()
 }

 @Test
 @DisplayName("Consulta registros de FuncionarioCompetencia por serviço")
 fun listarPorServico() {
  val usuarioDto = UsuarioDto(1, TipoUsuarioDto(1, "s"), "123456789", "Fulano", "123456789", "123456789")
  val dto = FuncionarioCompetenciaDto(1, usuarioDto)
  `when`(service.listarPorServico(1)).thenReturn(listOf(dto))

  val response = controller.listarPorServico(1)

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(1, response.body?.size)
  verify(service).listarPorServico(1)
 }

 @Test
 @DisplayName("Cria um novo registro de FuncionarioCompetencia")
 fun inserir() {
  `when`(service.salvar(funcionarioCompetenciaDto)).thenReturn(funcionarioCompetencia)

  val response = controller.inserir(funcionarioCompetenciaDto)

  assertEquals(HttpStatus.CREATED, response.statusCode)
  assertEquals(funcionarioCompetencia, response.body)
  verify(service).salvar(funcionarioCompetenciaDto)
 }

 @Test
 @DisplayName("Atualiza um registro de FuncionarioCompetencia")
 fun editar() {
  `when`(service.editar(1, funcionarioCompetenciaDto)).thenReturn(funcionarioCompetencia)

  val response = controller.editar(1, funcionarioCompetenciaDto)

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(funcionarioCompetencia, response.body)
  verify(service).editar(1, funcionarioCompetenciaDto)
 }

 @Test
 @DisplayName("Deleta um registro de FuncionarioCompetencia")
 fun deletar() {
  doNothing().`when`(service).deletar(1)

  val response = controller.deletar(1)

  assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
  verify(service).deletar(1)
 }
}
