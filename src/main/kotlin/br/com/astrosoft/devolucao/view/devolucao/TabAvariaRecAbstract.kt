package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.Fornecedor
import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.model.reports.RelatorioPedido
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.dataAgendaDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.dataNotaEditavel
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorCliente
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorCodigo
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorNome
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorValorTotal
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.notaEditavel
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.observacaoChaveDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.situacaoDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.usuarioSituacao
import br.com.astrosoft.devolucao.viewmodel.devolucao.ESituacaoPendencia
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucaoAbstractView
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabAvariaRecEditorViewModel
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabDevolucaoViewModelAbstract
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.addColumnButton
import br.com.astrosoft.framework.view.addColumnSeq
import com.github.mvysny.kaributools.getColumnBy
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.data.provider.SortDirection

@CssImport("./styles/gridTotal.css", themeFor = "vaadin-grid")
abstract class TabAvariaRecAbstract<T : IDevolucaoAbstractView>(viewModel: TabDevolucaoViewModelAbstract<T>) :
  TabDevolucaoAbstract<T>(viewModel) {

  override val situacaoPendencia: ESituacaoPendencia?
    get() = null

  override fun Grid<Fornecedor>.gridPanel() {
    setSelectionMode(Grid.SelectionMode.MULTI)
    addColumnSeq("Item")
    addColumnButton(VaadinIcon.FILE_TABLE, "Notas", "Notas") { fornecedor ->
      dlgNota = DlgNotaAvariaRec(viewModel)
      dlgNota?.showDialogNota(fornecedor, serie, situacaoPendencia) {
        viewModel.updateView()
      }
    }
    if (viewModel is TabAvariaRecEditorViewModel) {
      addColumnButton(VaadinIcon.MONEY, "Parcelas do fornecedor", "Parcelas") { fornecedor ->
        DlgParcelas(viewModel).showDialogParcela(fornecedor, serie)
      }
    }
    addColumnButton(VaadinIcon.EDIT, "Editor", "Edt", ::configIconEdt) { fornecedor ->
      viewModel.editRmkVend(fornecedor)
    }
    addColumnButton(VaadinIcon.PHONE_LANDLINE, "Representantes", "Rep") { fornecedor ->
      DlgFornecedor().showDialogRepresentante(fornecedor)
    }
    fornecedorCodigo()
    fornecedorCliente()
    fornecedorNome()

    if (this@TabAvariaRecAbstract is TabAvariaRecEditor) {
      userCol = usuarioSituacao().marcaAzul()
      situacaoCol = situacaoDesconto().marcaAzul()
    } else {
      if (this@TabAvariaRecAbstract !is TabAvariaRecNFD &&
          this@TabAvariaRecAbstract !is TabAvariaRecTransportadora &&
          this@TabAvariaRecAbstract !is TabAvariaRecEmail
      ) {
        dataNotaEditavel().marcaAzul()
        notaEditavel().marcaAzul()
      }
    }
    dataCol = dataAgendaDesconto().marcaAzul()
    observacaoChaveDesconto().marcaAzul()

    val totalCol = fornecedorValorTotal()
    this.dataProvider.addDataProviderListener {
      val totalAvariaRec = listBeans().sumOf { it.valorTotal }.format()
      totalCol.setFooter(Html("<b><font size=4>${totalAvariaRec}</font></b>"))
    }

    sort(listOf(GridSortOrder(getColumnBy(Fornecedor::fornecedor), SortDirection.ASCENDING)))
  }

  override fun excelPedido(notas: List<NotaSaida>): ByteArray {
    val report = RelatorioPedido.processaExcel(notas)
    return report
  }
}


