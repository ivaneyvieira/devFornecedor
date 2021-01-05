package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.devolucao.model.beans.EmailGmail
import br.com.astrosoft.devolucao.model.beans.Fornecedor
import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel

class DevFornecedorViewModel(view: IDevFornecedorView): ViewModel<IDevFornecedorView>(view) {
  val tabNotaDevolucaoViewModel = TabNotaSerie66ViewModel(this)
  val tabNota66Pago = TabNotaSerie66PagoViewModel(this)
  val tabNotaVendaViewModel = TabNotaSerie01ViewModel(this)
  val tabPedidoViewModel = TabPedidoViewModel(this)
  val tabEmailRecebido = TabEmailRecebidoViewModel(this)
  private val listTab: List<ITabView>
    get() = listOf(view.tabPedido,
                   view.tabNotaSerie66,
                   view.tabNotaSerie66Pago,
                   view.tabNotaSerie01,
                   view.tabEmailRecebido)
  
  fun tabsAuthorized() = listTab.filter {
    it.isAuthorized()
  }
}

interface IDevFornecedorView: IView {
  val tabNotaSerie66: ITabNotaSerie66
  val tabNotaSerie66Pago: ITabNotaSerie66Pago
  val tabNotaSerie01: ITabNotaSerie01
  val tabPedido: ITabPedido
  val tabEmailRecebido: ITabEmailRecebido
}

interface IEmailViewModel {
  fun listEmail(fornecedor: Fornecedor?): List<String>
  fun enviaEmail(gmail: EmailGmail, notas: List<NotaSaida>)
}