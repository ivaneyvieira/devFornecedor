package br.com.astrosoft.devolucao.view.devFornecedor

import br.com.astrosoft.devolucao.viewmodel.devolucao.INotaSerie66
import br.com.astrosoft.devolucao.viewmodel.devolucao.NotaSerie66ViewModel

class TabNotaSerie66(viewModel: NotaSerie66ViewModel): TabFornecedorAbstract(viewModel), INotaSerie66 {
  override val label: String
    get() = "Notas série 66"
}