package br.com.astrosoft.devolucao.view.demanda

import br.com.astrosoft.devolucao.model.beans.FornecedorProduto
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorDemandaColumns.fornecedorDemandaCliente
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorDemandaColumns.fornecedorDemandaFornecedor
import br.com.astrosoft.devolucao.view.demanda.columns.FornecedorDemandaColumns.fornecedorDemandaNome
import br.com.astrosoft.devolucao.viewmodel.demanda.ITabFornecedorDemanda
import br.com.astrosoft.devolucao.viewmodel.demanda.TabFornecedorDemandaViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.TabPanelGrid
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode

class TabFornecedorDemanda(private val viewModel: TabFornecedorDemandaViewModel) :
        TabPanelGrid<FornecedorProduto>(FornecedorProduto::class), ITabFornecedorDemanda {

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

    fornecedorDemandaFornecedor()
    fornecedorDemandaCliente()
    fornecedorDemandaNome()
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
