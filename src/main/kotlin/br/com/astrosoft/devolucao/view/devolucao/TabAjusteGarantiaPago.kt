package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.ESituacaoPendencia
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucao01View
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabAjusteGarantiaPago
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabAjusteGarantiaPagoViewModel
import br.com.astrosoft.framework.model.IUser

class TabAjusteGarantiaPago(viewModel: TabAjusteGarantiaPagoViewModel) : TabDevolucaoAbstract<IDevolucao01View>
  (viewModel), ITabAjusteGarantiaPago {
  override val label: String
    get() = "Ajuste Gar Pago"
  override val situacaoPendencia: ESituacaoPendencia?
    get() = null

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.ajusteGarantiaPago == true
  }
}