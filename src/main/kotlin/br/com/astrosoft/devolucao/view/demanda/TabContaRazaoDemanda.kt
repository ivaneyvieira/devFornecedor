package br.com.astrosoft.devolucao.view.demanda

import br.com.astrosoft.devolucao.model.beans.ContaRazao
import br.com.astrosoft.devolucao.model.beans.FiltroContaRazaoNota
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.demanda.ITabContaRazaoDemanda
import br.com.astrosoft.devolucao.viewmodel.demanda.TabContaRazaoDemandaViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnButton
import br.com.astrosoft.framework.view.addColumnSeq
import br.com.astrosoft.framework.view.vaadin.columnGrid
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode

class TabContaRazaoDemanda(private val viewModel: TabContaRazaoDemandaViewModel) :
  TabPanelGrid<ContaRazao>(ContaRazao::class), ITabContaRazaoDemanda {
  private lateinit var edtQuery: TextField

  override fun HorizontalLayout.toolBarConfig() {
    edtQuery = textField("Filtro") {
      width = "300px"
      valueChangeMode = ValueChangeMode.LAZY
      valueChangeTimeout = 2000
      addValueChangeListener {
        viewModel.updateView()
      }
    }
  }

  override fun Grid<ContaRazao>.gridPanel() {
    setSelectionMode(Grid.SelectionMode.MULTI)
    addColumnSeq("Item")

    addColumnButton(iconButton = VaadinIcon.MODAL_LIST, tooltip = "Nota", header = "Nota") { fornecedor ->
      viewModel.showNotas(fornecedor)
    }

    columnGrid(ContaRazao::numeroConta, "Número")
    columnGrid(ContaRazao::descricaoConta, "Descrição", isExpand = true)
  }

  override fun filtro(): FiltroContaRazaoNota {
    return FiltroContaRazaoNota(
      query = edtQuery.value ?: "",
    )
  }

  override fun showNotas(fornecedor: ContaRazao) {
    TODO("Not yet implemented")
  }

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.pedidoContaRazao == true
  }


  override val label: String
    get() = "Conta Razão"

  override fun updateComponent() {
    viewModel.updateView()
  }
}
