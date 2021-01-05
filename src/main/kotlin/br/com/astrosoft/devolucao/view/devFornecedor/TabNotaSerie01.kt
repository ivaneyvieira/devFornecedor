package br.com.astrosoft.devolucao.view.devFornecedor

import br.com.astrosoft.AppConfig
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabNotaSerie01
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabNotaSerie01ViewModel

class TabNotaSerie01(viewModel: TabNotaSerie01ViewModel): TabFornecedorAbstract(viewModel), ITabNotaSerie01 {
  override val label: String
    get() = "Notas série 1"
  
  override fun isAuthorized(): Boolean {
    val username = AppConfig.userSaci
    return username?.nota01 == true
  }
}