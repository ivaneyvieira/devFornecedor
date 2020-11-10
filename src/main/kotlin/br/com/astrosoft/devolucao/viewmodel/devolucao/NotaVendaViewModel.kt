package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.devolucao.model.beans.NotaSaida
import java.time.LocalDate

class NotaVendaViewModel(val viewModel: DevFornecedorViewModel) {
  private val subView
    get() = viewModel.view.tabNotaVenda
  
  fun imprimirNotaVenda(nota: NotaSaida) = viewModel.exec {
    subView.imprimeSelecionados(listOf(nota))
  }
  
  fun updateGridNotaVenda() = viewModel.exec {
    val dataInicial = subView.dataInicial()
    val dataFinal = subView.dataFinal()
    val fornecedor = subView.fornecedor()
    val nota = subView.nota()
    
    subView.updateGrid(listNotaVenda(dataInicial, dataFinal, fornecedor, nota))
  }
  
  private fun listNotaVenda(dataInicial: LocalDate?,
                            dataFinal: LocalDate?,
                            fornecedor: String,
                            nota: String): List<NotaSaida> {
    return NotaSaida.findNotaVenda(dataInicial, dataFinal, fornecedor, nota)
  }
  
  fun editRmk(nota: NotaSaida) {
    subView.editRmk(nota) {notaSaida ->
      notaSaida.save()
    }
  }
}

interface INotaVenda {
  fun updateGrid(list: List<NotaSaida>)
  fun itensSelecionados(): List<NotaSaida>
  fun imprimeSelecionados(itens: List<NotaSaida>)
  fun editRmk(nota: NotaSaida, save: (NotaSaida) -> Unit)
  fun dataInicial(): LocalDate?
  fun dataFinal(): LocalDate?
  fun fornecedor(): String
  fun nota(): String
}