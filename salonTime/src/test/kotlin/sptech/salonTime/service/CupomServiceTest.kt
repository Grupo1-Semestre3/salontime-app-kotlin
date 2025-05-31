package sptech.salonTime.service

import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import sptech.salonTime.entidade.Cupom
import sptech.salonTime.repository.CupomRepository
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CupomServiceTest {

 private val repository = mock(CupomRepository::class.java)
 private val service = CupomService(repository)

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

 @Test
 fun `criar deve salvar e retornar cupom`() {
  `when`(repository.save(cupom)).thenReturn(cupom)

  val result = service.criar(cupom)

  assertEquals(cupom, result)
  verify(repository).save(cupom)
 }

 @Test
 fun `listarAtivos deve retornar cupons ativos`() {
  val cupons = listOf(cupom)
  `when`(repository.listarAtivos()).thenReturn(cupons)

  val result = service.listarAtivos()

  assertEquals(cupons, result)
  verify(repository).listarAtivos()
 }

 @Test
 fun `atualizar deve atualizar cupom existente`() {
  val cupomAtualizado = cupom.copy(nome = "Desconto Atualizado")

  `when`(repository.findById(1)).thenReturn(java.util.Optional.of(cupom))
  `when`(repository.save(any(Cupom::class.java))).thenReturn(cupomAtualizado)

  val result = service.atualizar(1, cupomAtualizado)

  assertEquals(cupomAtualizado, result)
  verify(repository).findById(1)
  verify(repository).save(cupomAtualizado)
 }

 @Test
 fun `atualizar deve retornar null se cupom não existir`() {
  `when`(repository.findById(1)).thenReturn(java.util.Optional.empty())

  val result = service.atualizar(1, cupom)

  assertNull(result)
  verify(repository).findById(1)
  verify(repository, never()).save(any(Cupom::class.java))
 }

 @Test
 fun `deletar deve remover cupom existente`() {
  `when`(repository.existsById(1)).thenReturn(true)

  val result = service.deletar(1)

  assertEquals(true, result)
  verify(repository).existsById(1)
  verify(repository).deleteById(1)
 }

 @Test
 fun `deletar deve retornar false se cupom não existir`() {
  `when`(repository.existsById(1)).thenReturn(false)

  val result = service.deletar(1)

  assertEquals(false, result)
  verify(repository).existsById(1)
  verify(repository, never()).deleteById(anyInt())
 }
}
