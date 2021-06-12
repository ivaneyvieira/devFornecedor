package br.com.astrosoft.devolucao.view.entrada

import br.com.astrosoft.devolucao.viewmodel.entrada.ITabEntradaNddRecebidoViewModel
import br.com.astrosoft.devolucao.viewmodel.entrada.TabEntradaNddRecebidoViewModel

class TabEntradaNddRecebido(viewModel: TabEntradaNddRecebidoViewModel) :
        TabAbstractEntradaNdd<ITabEntradaNddRecebidoViewModel>(viewModel), ITabEntradaNddRecebidoViewModel {
  override val label: String
    get() = "Recebido"
}