package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.devolucao.model.beans.EmailGmail
import br.com.astrosoft.devolucao.model.beans.Fornecedor
import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.framework.viewmodel.IViewModelUpdate

class Devolucao66ViewModel(view: IDevolucao66View) : DevolucaoAbstractViewModel<IDevolucao66View>(view) {
  val tabNotaDevolucaoViewModel = TabNotaSerie66ViewModel(this)
  val tabNota66PagoViewModel = TabNotaSerie66PagoViewModel(this)
  val tabEntradaViewModel = TabEntradaViewModel(this)
  val tabEmailRecebidoViewModel = TabEmailRecebidoViewModel(this)

  override fun listTab() = listOf(
    view.tabNotaSerie66,
    view.tabNotaSerie66Pago,
    view.tabEntrada,
    view.tabEmailRecebido,
  )
}

interface IDevolucao66View : IDevolucaoAbstractView {
  val tabNotaSerie66: ITabNotaSerie66
  val tabNotaSerie66Pago: ITabNotaSerie66Pago
  val tabEntrada: ITabEntrada
  val tabEmailRecebido: ITabEmailRecebido
}

interface IEmailView : IViewModelUpdate {
  fun listEmail(fornecedor: Fornecedor?): List<String>
  fun enviaEmail(gmail: EmailGmail, notas: List<NotaSaida>)
}