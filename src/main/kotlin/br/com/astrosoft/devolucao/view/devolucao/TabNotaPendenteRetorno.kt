package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.Fornecedor
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.dataAgendaDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.dataSituacaoDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.docSituacao
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorCliente
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorCodigo
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorNome
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorObservacao
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorPrimeiraData
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorUltimaData
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorValorTotal
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.niSituacao
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.notaSituacao
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.observacaoChaveDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.situacaoDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.tituloSituacao
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.usuarioSituacao
import br.com.astrosoft.devolucao.viewmodel.devolucao.*
import br.com.astrosoft.devolucao.viewmodel.devolucao.ESituacaoPendencia.*
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.addColumnButton
import br.com.astrosoft.framework.view.center
import br.com.astrosoft.framework.view.left
import br.com.astrosoft.framework.view.right
import com.github.mvysny.kaributools.getColumnBy
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.data.provider.SortDirection

class TabNotaPendenteRetorno(viewModel: TabNotaPendenteViewModel, private val situacao: () -> ESituacaoPendencia) :
        TabDevolucaoAbstract<IDevolucaoPendenteView>(viewModel), ITabNotaPendente {
  override val label: String
    get() = situacao().title
  override val serie: Serie
    get() = Serie.RET

  override val situacaoPendencia: ESituacaoPendencia
    get() = situacao()

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci ?: return false
    return when (situacao()) {
      BASE              -> username.forPendenteBASE
      NOTA              -> username.forPendenteNOTA
      EMAIL             -> username.forPendenteEMAIL
      TRANSITO          -> username.forPendenteTRANSITO
      FABRICA           -> username.forPendenteFABRICA
      CREDITO_AGUARDAR  -> username.forPendenteCREDITO_AGUARDAR
      CREDITO_CONCEDIDO -> username.forPendenteCREDITO_CONCEDIDO
      CREDITO_APLICADO  -> username.forPendenteCREDITO_APLICADO
      CREDITO_CONTA     -> username.forPendenteCREDITO_CONTA
      BONIFICADA        -> username.forPendenteBONIFICADA
      REPOSICAO         -> username.forPendenteREPOSICAO
      RETORNO           -> username.forPendenteRETORNO
      AGUARDA_COLETA    -> username.forPendenteAGUARDA_COLETA
      ASSINA_CTE        -> username.forPendenteASSINA_CTE
    }
  }

  init {
    val situacao = situacao()
    situacaoCol.configCol(situacao.situacaoCol)
    notaCol.configCol(situacao.notaCol)
    niCol.configCol(situacao.niCol)
    docCol.configCol(situacao.docCol)
    tituloCol.configCol(situacao.numeroCol)
    dataCol.configCol(situacao.dataCol)
    userCol.configCol(situacao.userCol)
    dataSitCol.configCol(situacao.dataSitCol)
    if (situacao == CREDITO_CONTA) {
      val columns = gridPanel.columns.toMutableList()
      val pNi = columns.indexOf(niCol)
      val pSit = columns.indexOf(docCol)
      if (pNi >= 0 && pSit >= 0) {
        columns.removeAt(pNi)
        columns.add(pSit, niCol)
      }
      gridPanel.setColumnOrder(columns)
    }
    else if (situacao == BASE) {
      niCol.isVisible = false
      tituloCol.isVisible = false
      docCol.isVisible = false
      situacaoCol.setHeader("Situação")
    }
  }

  override fun Grid<Fornecedor>.gridPanel() {
    setSelectionMode(Grid.SelectionMode.MULTI)
    addColumnButton(VaadinIcon.FILE_TABLE, "Notas", "Notas") { fornecedor ->
      dlgNota = DlgNotaRetorno(viewModel)
      dlgNota?.showDialogNota(fornecedor, serie, situacaoPendencia) {
        viewModel.updateView()
      }
    }
    addColumnButton(VaadinIcon.MONEY, "Parcelas do fornecedor", "Parcelas") { fornecedor ->
      DlgParcelas(viewModel).showDialogParcela(fornecedor, serie)
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

    userCol = usuarioSituacao()
    dataSitCol = dataSituacaoDesconto().apply {
      this.isVisible = false
    }
    situacaoCol = situacaoDesconto()
    notaCol = notaSituacao()
    docCol = docSituacao()
    tituloCol = tituloSituacao()
    niCol = niSituacao()
    dataCol = dataAgendaDesconto()
    observacaoChaveDesconto()


    val totalCol = fornecedorValorTotal()
    this.dataProvider.addDataProviderListener {
      val totalPedido = listBeans().sumOf { it.valorTotal }.format()
      totalCol.setFooter(Html("<b><font size=4>${totalPedido}</font></b>"))
    }

    sort(listOf(GridSortOrder(getColumnBy(Fornecedor::fornecedor), SortDirection.ASCENDING)))
  }
}

