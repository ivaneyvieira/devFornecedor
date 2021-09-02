package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.*
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorDescontoViewColumns.fornecedorDescontoCliente
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorDescontoViewColumns.fornecedorDescontoCodigo
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorDescontoViewColumns.fornecedorDescontoNome
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorDescontoViewColumns.fornecedorDescontoPrimeiraData
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorDescontoViewColumns.fornecedorDescontoUltimaData
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorDescontoViewColumns.fornecedorDescontoValorTotal
import br.com.astrosoft.devolucao.viewmodel.devolucao.*
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnButton
import com.github.mvysny.karibudsl.v10.getColumnBy
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.provider.SortDirection
import com.vaadin.flow.data.value.ValueChangeMode

class TabDesconto(val viewModel: TabDescontoViewModel) : TabPanelGrid<FornecedorDesconto>(FornecedorDesconto::class),
        ITabDesconto {

  override val label: String
    get() = "Desconto"
  private lateinit var edtFiltro: TextField

  override fun updateComponent() {
    viewModel.updateView()
  }

  override fun filtro(): FiltroFornecedor {
    return FiltroFornecedor(query = edtFiltro.value ?: "", loja = Loja.lojaZero)
  }

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.desconto == true
  }

  override fun HorizontalLayout.toolBarConfig() {
    edtFiltro = textField("Filtro") {
      width = "300px"
      valueChangeMode = ValueChangeMode.TIMEOUT
      addValueChangeListener {
        viewModel.updateFiltro()
      }
    }
  }

  override fun Grid<FornecedorDesconto>.gridPanel() {
    setSelectionMode(Grid.SelectionMode.MULTI)
    addColumnButton(VaadinIcon.FILE_TABLE, "Notas", "Notas") { fornecedor ->
      DlgNotaDesconto(viewModel).showDialogNota(fornecedor)
    }

    fornecedorDescontoCodigo()
    fornecedorDescontoCliente()
    fornecedorDescontoNome()

    fornecedorDescontoPrimeiraData()
    fornecedorDescontoUltimaData()

    val totalCol = fornecedorDescontoValorTotal()
    this.dataProvider.addDataProviderListener {
      val totalPedido = listBeans().sumOf { it.valorTotal }.format()
      totalCol.setFooter(Html("<b><font size=4>${totalPedido}</font></b>"))
    }

    sort(listOf(GridSortOrder(getColumnBy(FornecedorDesconto::fornecedor), SortDirection.ASCENDING)))
  }
}