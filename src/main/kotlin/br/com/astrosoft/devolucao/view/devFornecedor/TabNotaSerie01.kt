package br.com.astrosoft.devolucao.view.devFornecedor

import br.com.astrosoft.AppConfig
import br.com.astrosoft.devolucao.viewmodel.devolucao.INotaSerie01
import br.com.astrosoft.devolucao.viewmodel.devolucao.NotaSerie01ViewModel

class TabNotaSerie01(viewModel: NotaSerie01ViewModel): TabFornecedorAbstract(viewModel), INotaSerie01 {
  override val label: String
    get() = "Notas série 1"
  
  override fun isAuthorized(): Boolean {
    val username = AppConfig.userSaci
    return username?.nota01 == true
  }
}