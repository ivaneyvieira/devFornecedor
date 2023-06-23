package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.devolucao.model.beans.ETipoNota
import br.com.astrosoft.devolucao.model.beans.ETipoNota.RECEBER

class TabEntradaNddReceberViewModel(viewModel: EntradaViewModel) :
    TabAbstractEntradaNddViewModel<ITabEntradaNddReceberViewModel>(viewModel) {
  override val subView
    get() = viewModel.view.tabEntradaNddReceberViewModel

  override val tipoTab: ETipoNota
    get() = RECEBER
}

interface ITabEntradaNddReceberViewModel : ITabAbstractEntradaNddViewModel