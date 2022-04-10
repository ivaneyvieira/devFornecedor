package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.Fornecedor
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorCliente
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorCodigo
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorNome
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorObservacao
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorPrimeiraData
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorValorTotal
import br.com.astrosoft.devolucao.viewmodel.devolucao.ESituacaoPendencia
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucaoInternaView
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabAjusteGarantiaPerca
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabAjusteGarantiaPercaViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.addColumnButton
import com.github.mvysny.kaributools.getColumnBy
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.data.provider.SortDirection

class TabAjusteGarantiaPerca(viewModel: TabAjusteGarantiaPercaViewModel) :
        TabDevolucaoAbstract<IDevolucaoInternaView>(viewModel), ITabAjusteGarantiaPerca {
  override val label: String
    get() = "Perca"
  override val situacaoPendencia: ESituacaoPendencia?
    get() = null

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.ajusteGarantiaPerca == true
  }

  override fun Grid<Fornecedor>.gridPanel() {
    setSelectionMode(Grid.SelectionMode.MULTI)
    addColumnButton(VaadinIcon.FILE_TABLE, "Notas", "Notas") { fornecedor ->
      dlgNota = DlgNotaGarantia(viewModel)
      dlgNota?.showDialogNota(fornecedor, serie, situacaoPendencia) {
        viewModel.updateView()
      }
    }
    addColumnButton(VaadinIcon.EDIT, "Editor", "Edt", ::configIconEdt) { fornecedor ->
      viewModel.editRmkVend(fornecedor)
    }
    fornecedorCodigo()
    fornecedorCliente()
    fornecedorNome()
    fornecedorPrimeiraData()
    fornecedorObservacao()

    val totalCol = fornecedorValorTotal()
    this.dataProvider.addDataProviderListener {
      val totalPedido = listBeans().sumOf { it.valorTotal }.format()
      totalCol.setFooter(Html("<b><font size=4>${totalPedido}</font></b>"))
    }

    sort(listOf(GridSortOrder(getColumnBy(Fornecedor::fornecedor), SortDirection.ASCENDING)))
  }
}