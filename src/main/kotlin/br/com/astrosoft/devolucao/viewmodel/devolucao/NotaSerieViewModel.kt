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
    subView.setFiltro("")
    NotaSaida.updateNotasDevolucao(subView.serie)
    return NotaSaida.findFornecedores()
  }
  
  fun editRmk(nota: NotaSaida) {
    subView.editRmk(nota) {notaSaida ->
      notaSaida.save()
    }
  }
  
  fun updateFiltro() {
    val filtro: String = subView.filtro()
    val resultList =
      NotaSaida.findFornecedores()
        .filtro(filtro)
    
    subView.updateGrid(resultList)
  }
}

fun List<Fornecedor>.filtro(txt: String): List<Fornecedor> {
  return this.filter {
    val filtroNum = txt.toIntOrNull() ?: 0
    it.custno == filtroNum || it.vendno == filtroNum || it.fornecedor.startsWith(txt, ignoreCase = true)
  }
}

interface INota {
  val serie: String
  
  fun updateGrid(itens: List<Fornecedor>)
  fun itensSelecionados(): List<Fornecedor>
  fun imprimeSelecionados(itens: List<NotaSaida>)
  fun editRmk(nota: NotaSaida, save: (NotaSaida) -> Unit)
  fun filtro(): String
  fun setFiltro(txt: String) {
  }
}