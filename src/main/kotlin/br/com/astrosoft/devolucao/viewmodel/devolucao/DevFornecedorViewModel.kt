package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel

class DevFornecedorViewModel(view: IDevFornecedorView): ViewModel<IDevFornecedorView>(view) {
  val tabNotaDevolucaoViewModel = NotaSerie66ViewModel(this)
  val tabNota66Pago = NotaSerie66PagoViewModel(this)
  val tabNotaVendaViewModel = NotaSerie01ViewModel(this)
  val tabPedidoViewModel = PedidoViewModel(this)
}

interface IDevFornecedorView: IView {
  val tabNotaSerie66: INotaSerie66
  val tabNotaSerie66Pago: INotaSerie66Pago
  val tabNotaSerie01: INotaSerie01
  val tabPedido: IPedido
}