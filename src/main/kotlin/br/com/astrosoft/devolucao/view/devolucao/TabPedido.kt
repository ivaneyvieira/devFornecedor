package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabPedido
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabPedidoViewModel
import br.com.astrosoft.framework.model.IUser

class TabPedido(viewModel: TabPedidoViewModel) : TabDevolucaoAbstract(viewModel), ITabPedido {
    override val label: String
        get() = "Pedido"

    override fun isAuthorized(user: IUser): Boolean {
        val username = user as? UserSaci
        return username?.pedido == true
    }
}