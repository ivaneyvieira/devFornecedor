package br.com.astrosoft.devolucao.view.devFornecedor

import br.com.astrosoft.AppConfig
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabNotaSerie66Pago
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabNotaSerie66PagoViewModel

class TabNotaSerie66Pago(viewModel: TabNotaSerie66PagoViewModel): TabFornecedorAbstract(viewModel),
                                                                  ITabNotaSerie66Pago {
  override val label: String
    get() = "Notas série 66 Pago"
  
  override fun isAuthorized(): Boolean {
    val username = AppConfig.userSaci
    return username?.nota66Pago == true
  }
}