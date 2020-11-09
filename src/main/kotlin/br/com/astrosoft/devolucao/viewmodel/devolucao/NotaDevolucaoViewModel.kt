package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.devolucao.model.beans.NotaSaida
import java.time.LocalDate

class NotaDevolucaoViewModel(val viewModel: DevFornecedorViewModel) {
  private val subView
    get() = viewModel.view.tabNotaDevolucao
  
  fun imprimirNotaDevolucao(nota: NotaSaida) = viewModel.exec {
    subView.imprimeSelecionados(listOf(nota))
  }
  
  fun updateGridNotaDevolucao() = viewModel.exec {
    val dataInicial = subView.dataInicial()
    val dataFinal = subView.dataFinal()
    if(dataInicial == null || dataFinal == null)
      subView.updateGrid(emptyList())
    else
      subView.updateGrid(listNotaDevolucao(dataInicial, dataFinal))
  }
  
  private fun listNotaDevolucao(dataInicial: LocalDate, dataFinal: LocalDate): List<NotaSaida> {
    return NotaSaida.findNotaDevolucao(dataInicial, dataFinal)
  }
  
  fun editRmk(nota: NotaSaida) {
    subView.editRmk(nota) {nota ->
      nota.save()
    }
  }
}

interface INotaDevolucao {
  fun updateGrid(list: List<NotaSaida>)
  fun itensSelecionados(): List<NotaSaida>
  fun imprimeSelecionados(itens: List<NotaSaida>)
  fun editRmk(nota: NotaSaida, save: (NotaSaida) -> Unit)
  fun dataInicial(): LocalDate?
  fun dataFinal(): LocalDate?
}