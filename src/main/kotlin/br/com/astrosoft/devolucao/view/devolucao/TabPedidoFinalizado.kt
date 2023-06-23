package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.ESituacaoPedido.*
import br.com.astrosoft.devolucao.viewmodel.devolucao.ESituacaoPendencia
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucaoPedidoView
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabPedidoFinalizado
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabPedidoFinalizadoViewModel
import br.com.astrosoft.framework.model.IUser

class TabPedidoFinalizado(viewModel: TabPedidoFinalizadoViewModel) : TabPedidoAbstract<IDevolucaoPedidoView>(viewModel),
    ITabPedidoFinalizado {
  override val label: String
    get() = "Finalizado"
  override val situacaoPendencia: ESituacaoPendencia?
    get() = null


  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.pedidoFinalizado == true
  }

  override val situacaoPedido
    get() = listOf(
        NFD_AUTOZ, BAIXA, PAGO, RETORNO,
        PERCA, DESCARTE, ASSISTENCIA_RETORNO
    )
}
