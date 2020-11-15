package br.com.astrosoft.devolucao.view.devFornecedor

import br.com.astrosoft.devolucao.viewmodel.devolucao.INotaDevolucao
import br.com.astrosoft.devolucao.viewmodel.devolucao.NotaDevolucaoViewModel

class TabNotaDevolucao(val viewModel: NotaDevolucaoViewModel): TabFornecedorAbstract(), INotaDevolucao {
  override val label: String
    get() = "Notas série 66"
  
  override fun updateComponent() {
    viewModel.updateGridNotaDevolucao()
  }
}