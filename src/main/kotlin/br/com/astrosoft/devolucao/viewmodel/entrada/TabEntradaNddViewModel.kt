package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.devolucao.model.beans.ETipoNota
import br.com.astrosoft.devolucao.model.beans.ETipoNota.RECEBIDO
import br.com.astrosoft.devolucao.model.beans.ETipoNota.TODOS

class TabEntradaNddViewModel(viewModel: EntradaViewModel) :
        TabAbstractEntradaNddViewModel<ITabEntradaNddViewModel>(viewModel) {
  override val subView
    get() = viewModel.view.tabEntradaNddViewModel

  override val tipoTab: ETipoNota
    get() = TODOS
}

interface ITabEntradaNddViewModel : ITabAbstractEntradaNddViewModel