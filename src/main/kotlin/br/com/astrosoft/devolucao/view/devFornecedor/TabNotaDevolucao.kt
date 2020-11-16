package br.com.astrosoft.devolucao.view.devFornecedor

import br.com.astrosoft.devolucao.viewmodel.devolucao.INotaDevolucao
import br.com.astrosoft.devolucao.viewmodel.devolucao.NotaSerie66ViewModel

class TabNotaDevolucao(viewModel: NotaSerie66ViewModel): TabFornecedorAbstract(viewModel), INotaDevolucao {
  override val label: String
    get() = "Notas série 66"
}