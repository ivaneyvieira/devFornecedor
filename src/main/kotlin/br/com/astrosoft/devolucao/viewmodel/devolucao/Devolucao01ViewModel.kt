package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.devolucao.model.beans.EmailGmail
import br.com.astrosoft.devolucao.model.beans.Fornecedor
import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.IViewModelUpdate
import br.com.astrosoft.framework.viewmodel.ViewModel

class Devolucao01ViewModel(view: IDevolucao01View) : DevolucaoAbstractViewModel<IDevolucao01View>(view) {
  val tabNotaSerie01ViewModel = TabNotaSerie01ViewModel(this)
  val tabNotaSerie01PagoViewModel = TabNotaSerie01PagoViewModel(this)
  val tabNotaSerie01ColetaViewModel = TabNotaSerie01ColetaViewModel(this)
  val tabNotaRemessaConsertoViewModel = TabNotaRemessaConsertoViewModel(this)
  val tabPedidoViewModel = TabPedidoViewModel(this)
  val tabAjusteGarantiaViewModel = TabAjusteGarantiaViewModel(this)

  override fun listTab() = listOf(
    view.tabPedido,
    view.tabNotaSerie01,
    view.tabNotaSerie01Pago,
    view.tabNotaSerie01Coleta,
    view.tabNotaRemessaConserto,
    view.tabAjusteGarantia,
                                 )
}

interface IDevolucao01View : IDevolucaoAbstractView {
  val tabNotaSerie01: ITabNotaSerie01
  val tabNotaSerie01Pago: ITabNotaSerie01Pago
  val tabNotaSerie01Coleta: ITabNotaSerie01Coleta
  val tabNotaRemessaConserto: ITabNotaRemessaConserto
  val tabAjusteGarantia: ITabAjusteGarantia
  val tabPedido: ITabPedido
}

