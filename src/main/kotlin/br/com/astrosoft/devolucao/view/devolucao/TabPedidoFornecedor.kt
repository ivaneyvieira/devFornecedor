package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.FornecedorProduto
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorProdutoColumns.fornecedorCliente
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorProdutoColumns.fornecedorFornecedor
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorProdutoColumns.fornecedorNome
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabPedidoFornecedor
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabPedidoFornecedorViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.TabPanelGrid
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode

class TabPedidoFornecedor(private val viewModel: TabPedidoFornecedorViewModel) :
        TabPanelGrid<FornecedorProduto>(FornecedorProduto::class), ITabPedidoFornecedor {

  override fun HorizontalLayout.toolBarConfig() {
    edtFiltro = textField("Filtro") {
      width = "300px"
      valueChangeMode = ValueChangeMode.LAZY
      valueChangeTimeout = 2000
      addValueChangeListener {
        viewModel.updateView()
      }
    }
  }

  override fun Grid<FornecedorProduto>.gridPanel() {
    setSelectionMode(Grid.SelectionMode.MULTI)

    fornecedorFornecedor()
    fornecedorCliente()
    fornecedorNome()
  }

  override fun filtro(): String {
    return edtFiltro.value ?: ""
  }

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.pedidoFornecedor == true
  }

  private lateinit var edtFiltro: TextField
  override val label: String
    get() = "Fornecedor"

  override fun updateComponent() {
    viewModel.updateView()
  }
}
