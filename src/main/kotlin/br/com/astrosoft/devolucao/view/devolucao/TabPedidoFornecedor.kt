package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.FornecedorProduto
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.model.reports.RelatorioFornecedorPedido
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorProdutoColumns.fornecedorCliente
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorProdutoColumns.fornecedorDataNF
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorProdutoColumns.fornecedorFornecedor
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorProdutoColumns.fornecedorInvno
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorProdutoColumns.fornecedorNome
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorProdutoColumns.fornecedorNota
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabNota
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabPedidoFornecedor
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabPedidoFornecedorViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.TabPanelGrid
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.HorizontalLayout

class TabPedidoFornecedor(private val viewModel: TabPedidoFornecedorViewModel) :
        TabPanelGrid<FornecedorProduto>(FornecedorProduto::class), ITabPedidoFornecedor {
  override fun HorizontalLayout.toolBarConfig() {
    //Vazio
  }

  override fun Grid<FornecedorProduto>.gridPanel() {
    setSelectionMode(Grid.SelectionMode.MULTI)

    fornecedorFornecedor()
    fornecedorCliente()
    fornecedorNome()
    //fornecedorInvno()
    //fornecedorNota()
    //fornecedorDataNF()
  }

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.pedidoFornecedor == true
  }

  override val label: String
    get() = "Fornecedor"

  override fun updateComponent() {
    viewModel.updateView()
  }
}
