package sptech.salonTime.service

import org.springframework.stereotype.Service
import sptech.salonTime.dto.DescCancelamentoDto
import sptech.salonTime.dto.NovaDescricaoCancelamentoDto
import sptech.salonTime.entidade.DescCancelamento
import sptech.salonTime.mapper.AvaliacaoMapper
import sptech.salonTime.mapper.DescCancelamentoMapper
import sptech.salonTime.repository.DescCancelamentoRepository

@Service
class DescCancelamentoService (var repository: DescCancelamentoRepository) {

    fun listar(): List<DescCancelamentoDto> {
        var cancelamento = repository.findAll()

        return cancelamento.map{ DescCancelamentoMapper.toDTO(it)!! }
    }

    fun listarPorId(id: Int): DescCancelamentoDto? {
        var cancelamentoAchado = repository.findById(id).orElseThrow(DescCancelamentoNaoEncontradoException("Cancelamento com ID $id não encontrado."))

        return DescCancelamentoMapper.toDTO(cancelamentoAchado)
    }

    fun criar(descCancelamento: DescCancelamento): DescCancelamento {
        return repository.save(descCancelamento)
    }

    fun atualizar(id: Int, descCancelamento: DescCancelamento): DescCancelamentoDto? {
       var cancelamento = repository.findById(id).orElse(null)
        if (cancelamento != null) {
            cancelamento.id = id
            cancelamento.descricao = descCancelamento.descricao
            cancelamento.agendamento = descCancelamento.agendamento
            var cancelamentoSalvo = repository.save(cancelamento)
            return DescCancelamentoMapper.toDTO(cancelamentoSalvo)
        } else {
            return null
        }
    }

    fun atualizarDescricao(id: Int, novaDescricao: NovaDescricaoCancelamentoDto): DescCancelamentoDto? {
        val cancelamento = repository.findById(id).orElse(null)
        if (cancelamento != null) {
            cancelamento.descricao = novaDescricao.descricao
            var cancelamentoSalvo = repository.save(cancelamento)

            return DescCancelamentoMapper.toDTO(cancelamentoSalvo)

        } else {
            return null
        }
    }

    fun deletar(id: Int) {
        val cancelamento = repository.findById(id).orElse(null)
        if (cancelamento != null) {
            repository.delete(cancelamento)
        }
    }
}