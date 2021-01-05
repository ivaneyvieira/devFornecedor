package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.devolucao.model.beans.EmailGmail
import br.com.astrosoft.devolucao.model.beans.Fornecedor
import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel

class DevFornecedorViewModel(view: IDevFornecedorView): ViewModel<IDevFornecedorView>(view) {
  val tabNotaDevolucaoViewModel = NotaSerie66ViewModel(this)
  val tabNota66Pago = NotaSerie66PagoViewModel(this)
  val tabNotaVendaViewModel = NotaSerie01ViewModel(this)
  val tabPedidoViewModel = PedidoViewModel(this)
  val tabEmailRecebido = EmailRecebidoViewModel(this)
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
  val tabNotaSerie66: INotaSerie66
  val tabNotaSerie66Pago: INotaSerie66Pago
  val tabNotaSerie01: INotaSerie01
  val tabPedido: IPedido
  val tabEmailRecebido: IEmailRecebido
}

interface IEmailViewModel {
  fun listEmail(fornecedor: Fornecedor?): List<String>
  fun enviaEmail(gmail: EmailGmail, notas: List<NotaSaida>)
}