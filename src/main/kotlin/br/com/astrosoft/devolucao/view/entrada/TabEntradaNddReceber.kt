package br.com.astrosoft.devolucao.view.entrada

import br.com.astrosoft.devolucao.viewmodel.entrada.ITabEntradaNddReceberViewModel
import br.com.astrosoft.devolucao.viewmodel.entrada.TabEntradaNddReceberViewModel

class TabEntradaNddReceber(viewModel: TabEntradaNddReceberViewModel) :
        TabAbstractEntradaNdd<ITabEntradaNddReceberViewModel>(viewModel), ITabEntradaNddReceberViewModel {
  override val label: String
    get() = "Receber"
}