package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.devolucao.viewmodel.devolucao.Serie.Serie01
import br.com.astrosoft.devolucao.viewmodel.devolucao.SimNao.NONE
import br.com.astrosoft.devolucao.viewmodel.devolucao.SimNao.SIM

class TabNotaSerie01PagoViewModel(viewModel: Devolucao01ViewModel) : TabDevolucaoViewModelAbstract<IDevolucao01View>(
  viewModel) {
  override val subView
    get() = viewModel.view.tabNotaSerie01Pago
}

interface ITabNotaSerie01Pago : ITabNota {
  override val serie: Serie
    get() = Serie01
  override val pago66: SimNao
    get() = NONE
  override val pago01: SimNao
    get() = SIM
  override val coleta01: SimNao
    get() = NONE
  override val remessaConserto: SimNao
    get() = NONE
}