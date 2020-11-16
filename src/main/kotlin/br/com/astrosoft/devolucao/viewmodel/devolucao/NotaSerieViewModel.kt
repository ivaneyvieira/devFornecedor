package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.devolucao.model.beans.Fornecedor
import br.com.astrosoft.devolucao.model.beans.NotaSaida

abstract class NotaSerieViewModel(val viewModel: DevFornecedorViewModel) {
  protected abstract val subView: INota
  
  fun imprimirNotaDevolucao(nota: NotaSaida) = viewModel.exec {
    subView.imprimeSelecionados(listOf(nota))
  }
  
  fun updateGridNota() = viewModel.exec {
    subView.updateGrid(listNotaDevolucao())
  }
  
  private fun listNotaDevolucao(): List<Fornecedor> {
    NotaSaida.updateNotasDevolucao(subView.serie)
    return NotaSaida.findFornecedores()
  }
  
  fun editRmk(nota: NotaSaida) {
    subView.editRmk(nota) {notaSaida ->
      notaSaida.save()
    }
  }
}

interface INota {
  abstract val serie: String
  
  fun updateGrid(itens: List<Fornecedor>)
  fun itensSelecionados(): List<Fornecedor>
  fun imprimeSelecionados(itens: List<NotaSaida>)
  fun editRmk(nota: NotaSaida, save: (NotaSaida) -> Unit)
}