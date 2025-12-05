package sptech.salonTime.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import sptech.salonTime.dto.PointsDto
import sptech.salonTime.entidade.Cupom
import sptech.salonTime.entidade.CupomConfiguracao
import sptech.salonTime.entidade.Usuario
import sptech.salonTime.exception.CupomDuplicadoException
import sptech.salonTime.exception.CupomNaoEncontradoException
import sptech.salonTime.exception.UsuarioNaoEncontradoException
import sptech.salonTime.repository.CupomConfiguracaoRepository
import sptech.salonTime.repository.CupomRepository
import sptech.salonTime.repository.UsuarioRepository
import java.time.LocalDate
import java.util.*

class CupomServiceTest {

 private val repository = mock(CupomRepository::class.java)
 private val usuarioRepository = mock(UsuarioRepository::class.java)
 private val cupomConfiguracaoRepository = mock(CupomConfiguracaoRepository::class.java)
 private val service = CupomService(repository, usuarioRepository, cupomConfiguracaoRepository)

 private val cupom = Cupom(
  id = 1,
  nome = "Desconto Especial",
  descricao = "10% OFF em todos os serviços",
  codigo = "DESC10",
  ativo = true,
  inicio = LocalDate.now(),
  fim = LocalDate.now().plusDays(30),
  tipoDestinatario = "Todos"
 )


 // ----------------------------------------------------------------------
 // CRIAR
 // ----------------------------------------------------------------------
 @Test
 fun `criar deve salvar e retornar cupom`() {
  `when`(repository.findByCodigo("DESC10")).thenReturn(null)
  `when`(repository.save(cupom)).thenReturn(cupom)

  val result = service.criar(cupom)

  assertEquals(cupom, result)
  verify(repository).findByCodigo("DESC10")
  verify(repository).save(cupom)
 }

 @Test
 fun `criar deve lançar exceção quando o código já existir`() {
  `when`(repository.findByCodigo("DESC10")).thenReturn(cupom)

  assertThrows<CupomDuplicadoException> {
   service.criar(cupom)
  }

  verify(repository).findByCodigo("DESC10")
  verify(repository, never()).save(any())
 }


 // ----------------------------------------------------------------------
 // LISTAR
 // ----------------------------------------------------------------------
 @Test
 fun `listarAtivos deve retornar cupons ativos`() {
  val cupons = listOf(cupom)
  `when`(repository.listarAtivos()).thenReturn(cupons)

  val result = service.listarAtivos()

  assertEquals(cupons, result)
  verify(repository).listarAtivos()
 }


 // ----------------------------------------------------------------------
 // ATUALIZAR
 // ----------------------------------------------------------------------
 @Test
 fun `atualizar deve atualizar cupom existente`() {
  val cupomAtualizado = cupom.copy(nome = "Desconto Atualizado")

  `when`(repository.findById(1)).thenReturn(Optional.of(cupom))
  `when`(repository.save(any(Cupom::class.java))).thenReturn(cupomAtualizado)

  val result = service.atualizar(1, cupomAtualizado)

  assertEquals("Desconto Atualizado", result?.nome)
  verify(repository).findById(1)
  verify(repository).save(any(Cupom::class.java))
 }

 @Test
 fun `atualizar deve retornar null se cupom não existir`() {
  `when`(repository.findById(1)).thenReturn(Optional.empty())

  val result = service.atualizar(1, cupom)

  assertNull(result)
  verify(repository).findById(1)
  verify(repository, never()).save(any())
 }


 // ----------------------------------------------------------------------
 // DELETAR
 // ----------------------------------------------------------------------
 @Test
 fun `deletar deve remover cupom existente`() {
  `when`(repository.existsById(1)).thenReturn(true)

  val result = service.deletar(1)

  assertTrue(result)
  verify(repository).existsById(1)
  verify(repository).deleteById(1)
 }

 @Test
 fun `deletar deve retornar false se cupom não existir`() {
  `when`(repository.existsById(1)).thenReturn(false)

  val result = service.deletar(1)

  assertFalse(result)
  verify(repository).existsById(1)
  verify(repository, never()).deleteById(anyInt())
 }


 // ----------------------------------------------------------------------
 // CALCULAR POINTS
 // ----------------------------------------------------------------------
 @Test
 fun `calcularPoints deve retornar PointsDto vindo do repository quando nao for null`() {
  val idUsuario = 1
  val usuario = Usuario().apply { id = 1 }

  val dtoEsperado = PointsDto(
   pointsParcial = 4,
   pointsTotal = 5,
   porcentagemCupom = 20
  )

  `when`(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario))
  `when`(repository.calcularPoints(idUsuario)).thenReturn(dtoEsperado)

  val result = service.calcularPoints(idUsuario)

  assertEquals(dtoEsperado, result)
 }

 @Test
 fun `calcularPoints deve retornar dto calculado quando repository retornar null`() {
  val idUsuario = 1
  val usuario = Usuario().apply { id = 1 }

  val config = CupomConfiguracao(
   id = 1,
   intervaloAtendimento = 5,
   porcentagemDesconto = 20
  )

  `when`(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario))
  `when`(repository.calcularPoints(idUsuario)).thenReturn(null)
  `when`(cupomConfiguracaoRepository.findById(1)).thenReturn(Optional.of(config))

  val result = service.calcularPoints(idUsuario)

  assertEquals(0, result!!.pointsParcial)
  assertEquals(5L, result.pointsTotal)
  assertEquals(20, result.porcentagemCupom)
 }

 @Test
 fun `calcularPoints deve lançar exceção quando usuario nao existir`() {
  `when`(usuarioRepository.findById(1)).thenReturn(Optional.empty())

  assertThrows<UsuarioNaoEncontradoException> {
   service.calcularPoints(1)
  }
 }

 @Test
 fun `calcularPoints deve lançar exceção quando cupom configuracao nao for encontrada`() {
  val usuario = Usuario().apply { id = 1 }

  `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario))
  `when`(repository.calcularPoints(1)).thenReturn(null)
  `when`(cupomConfiguracaoRepository.findById(1)).thenReturn(Optional.empty())

  assertThrows<CupomNaoEncontradoException> {
   service.calcularPoints(1)
  }
 }
}
