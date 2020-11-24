package br.com.astrosoft.devolucao.view.devFornecedor

import br.com.astrosoft.devolucao.model.beans.NFFile
import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.viewmodel.devolucao.INotaVenda
import br.com.astrosoft.devolucao.viewmodel.devolucao.NotaSerie01ViewModel

class TabNotaVenda(viewModel: NotaSerie01ViewModel): TabFornecedorAbstract(viewModel), INotaVenda {
  override val label: String
    get() = "Notas série 1"
}