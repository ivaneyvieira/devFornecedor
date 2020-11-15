package br.com.astrosoft.devolucao.view.devFornecedor

import br.com.astrosoft.devolucao.viewmodel.devolucao.INotaVenda
import br.com.astrosoft.devolucao.viewmodel.devolucao.NotaVendaViewModel

class TabNotaVenda(val viewModel: NotaVendaViewModel): TabFornecedorAbstract(), INotaVenda {
  override val label: String
    get() = "Notas série 1"
  
  override fun updateComponent() {
    viewModel.updateGridNotaVenda()
  }
}