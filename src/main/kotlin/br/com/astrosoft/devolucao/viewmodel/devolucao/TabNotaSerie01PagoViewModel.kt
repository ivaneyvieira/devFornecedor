package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.devolucao.viewmodel.devolucao.Serie.*
import br.com.astrosoft.devolucao.viewmodel.devolucao.SimNao.*

class TabNotaSerie01PagoViewModel(viewModel: DevolucaoViewModel) : TabDevolucaoViewModelAbstract(
  viewModel
                                                                                            ) {
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