package sptech.salonTime.controller

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import sptech.salonTime.dto.CupomDestinadoDto
import sptech.salonTime.dto.UsuarioPublicoDto
import sptech.salonTime.entidade.Cupom
import sptech.salonTime.entidade.CupomDestinado
import sptech.salonTime.service.CupomDestinadoService

class CupomDestinadoControllerTest {

 private val service = mock(CupomDestinadoService::class.java)
 private val controller = CupomDestinadoController(service)
 private val cupomDestinadoDto = CupomDestinadoDto(1, mock(Cupom::class.java), mock(UsuarioPublicoDto::class.java), true)

 @Test
 @DisplayName("Deve listar todos os cupons destinados com sucesso")
 fun listar() {
  `when`(service.listar()).thenReturn(listOf(cupomDestinadoDto))

  val response = controller.listar()

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(1, response.body?.size)
  verify(service).listar()
 }

 @Test
 @DisplayName("Deve inserir um novo cupom destinado e retornar status 201")
 fun inserir() {
  val cupomDestinado = CupomDestinado()
  `when`(service.salvar(cupomDestinado)).thenReturn(cupomDestinadoDto)

  val response = controller.inserir(cupomDestinado)

  assertEquals(HttpStatus.CREATED, response.statusCode)
  assertNotNull(response.body)
  verify(service).salvar(cupomDestinado)
 }

 @Test
 @DisplayName("Deve editar um cupom destinado existente e retornar os dados atualizados")
 fun editar() {
  val cupomDestinado = CupomDestinado()
  `when`(service.editar(1, cupomDestinado)).thenReturn(cupomDestinadoDto)

  val response = controller.editar(1, cupomDestinado)

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(cupomDestinadoDto, response.body)
  verify(service).editar(1, cupomDestinado)
 }

 @Test
 @DisplayName("Deve atualizar o status de uso do cupom destinado e retornar os dados atualizados")
 fun atualizarUsado() {
  `when`(service.atualizarUsado(1, true)).thenReturn(cupomDestinadoDto)

  val response = controller.atualizarUsado(1, true)

  assertEquals(HttpStatus.OK, response.statusCode)
  assertEquals(cupomDestinadoDto, response.body)
  verify(service).atualizarUsado(1, true)
 }

 @Test
 @DisplayName("Deve deletar um cupom destinado pelo ID e retornar status 204")
 fun deletar() {
  doNothing().`when`(service).deletar(1)

  val response = controller.deletar(1)

  assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
  verify(service).deletar(1)
 }
}
