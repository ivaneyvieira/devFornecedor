package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.devolucao.viewmodel.devolucao.Serie.Serie01
import br.com.astrosoft.devolucao.viewmodel.devolucao.SimNao.*

class TabNotaSerie01ColetaViewModel(viewModel: Devolucao01ViewModel) :
        TabDevolucaoViewModelAbstract<IDevolucao01View>(viewModel) {
  override val subView
    get() = viewModel.view.tabNotaSerie01Coleta
}

interface ITabNotaSerie01Coleta : ITabNota {
  override val serie: Serie
    get() = Serie01
  override val pago66: SimNao
    get() = NONE
  override val pago01: SimNao
    get() = NAO
  override val coleta01: SimNao
    get() = SIM
  override val remessaConserto: SimNao
    get() = NONE
}