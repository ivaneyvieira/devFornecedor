package br.com.astrosoft.devolucao.view.entrada

import br.com.astrosoft.devolucao.viewmodel.entrada.ITabEntradaNddViewModel
import br.com.astrosoft.devolucao.viewmodel.entrada.TabEntradaNddViewModel

class TabEntradaNdd(viewModel: TabEntradaNddViewModel) : TabAbstractEntradaNdd<ITabEntradaNddViewModel>(viewModel),
        ITabEntradaNddViewModel {
  override val label: String
    get() = "Ndd"
}