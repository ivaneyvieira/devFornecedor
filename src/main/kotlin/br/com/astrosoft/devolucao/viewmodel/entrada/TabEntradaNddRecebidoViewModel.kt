package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.devolucao.model.beans.ETipoNota
import br.com.astrosoft.devolucao.model.beans.ETipoNota.RECEBIDO

class TabEntradaNddRecebidoViewModel(viewModel: EntradaViewModel) :
    TabAbstractEntradaNddViewModel<ITabEntradaNddRecebidoViewModel>(viewModel) {
  override val subView
    get() = viewModel.view.tabEntradaNddRecebidoViewModel

  override val tipoTab: ETipoNota
    get() = RECEBIDO
}

interface ITabEntradaNddRecebidoViewModel : ITabAbstractEntradaNddViewModel