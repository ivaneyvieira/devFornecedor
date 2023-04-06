package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.chaveDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.dataAgendaDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.dataSituacaoDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.docSituacao
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.niSituacao
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaDataNfAjuste
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaDataNota
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaDataPedido
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaFatura
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaLoja
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaNfAjuste
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaNota
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaObservacao
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaPedido
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaSituacao
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaValor
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaValorPago
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.situacaoDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.tituloSituacao
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.usuarioSituacao
import br.com.astrosoft.devolucao.viewmodel.devolucao.*
import br.com.astrosoft.devolucao.viewmodel.devolucao.Serie.*
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.*
import com.github.mvysny.kaributools.getColumnBy
import com.vaadin.flow.component.Focusable
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.data.provider.SortDirection

@CssImport("./styles/gridTotal.css")
class DlgNota<T : IDevolucaoAbstractView>(viewModel: TabDevolucaoViewModelAbstract<T>) : DlgNotaAbstract<T>(viewModel) {
  override fun createGridNotas(
    listNotas: List<NotaSaida>,
    serie: Serie,
    situacao: ESituacaoPendencia?
  ): Grid<NotaSaida> {
    val gridDetail = Grid(NotaSaida::class.java, false)
    return gridDetail.apply {
      //addThemeVariants()
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)
      setItems(listNotas)
      if (serie in listOf(Serie01, NFD, PED, AJC, AJT, AJP, AJD, A66)) {
        this.withEditor(NotaSaida::class, openEditor = {
          val colunas = this.columns
          val componente = colunas.firstOrNull {
            it.editorComponent != null && (it.editorComponent is Focusable<*>)
          }
          val focusable = componente?.editorComponent as? Focusable<*>
          focusable?.focus()
        }, closeEditor = { binder ->
          viewModel.salvaDesconto(binder.bean)
          this.dataProvider.refreshItem(binder.bean)
        })
      }

      addColumnButton(VaadinIcon.FILE_PICTURE, "Arquivos", "Arq", ::configIconArq) { nota ->
        viewModel.editFile(nota)
      }
      addColumnButton(VaadinIcon.EDIT, "Editor", "Edt", ::configIconEdt) { nota ->
        viewModel.editRmk(nota)
      }
      if (viewModel !is TabNotaPendenteViewModel) {
        addColumnButton(VaadinIcon.ENVELOPE_O, "Editor", "Email", ::configMostraEmail) { nota ->
          viewModel.mostrarEmailNota(nota)
        }
      }

      notaLoja()
      if (serie !in listOf(Serie01, FIN, NFD)) {
        notaDataPedido()
        notaPedido()
      }
      notaDataNota()
      if (serie !in listOf(PED)) {
        if (serie !in listOf(AJP, AJT, AJC, AJD, A66)) {
          notaNota()
        }
        notaDataNfAjuste().dateFieldEditor()
        notaNfAjuste().textFieldEditor()
      }
      if (serie in listOf(AJP, AJT, AJC, AJD, A66)) {
        chaveDesconto("Observação").textFieldEditor().apply {
          this.setClassNameGenerator {
            it.situacaoPendencia?.cssCor
          }
        }
        notaObservacao()
        notaValorPago().decimalFieldEditor()
      } else {
        if (serie !in listOf(PED)) {
          notaFatura()
        }
      }
      usuarioSituacao(situacao)
      dataSituacaoDesconto(situacao).apply {
        this.isVisible = false
      }
      situacaoDesconto(situacao)
      if (viewModel is TabNotaPendenteViewModel) {
        if (situacao == ESituacaoPendencia.CREDITO_CONTA) {
          niSituacao(situacao).textFieldEditor()
          docSituacao(situacao).textFieldEditor()
          notaSituacao(situacao).textFieldEditor()
          tituloSituacao(situacao).textFieldEditor()
        } else {
          if (situacao != ESituacaoPendencia.BASE) {
            docSituacao(situacao).textFieldEditor()
            notaSituacao(situacao).textFieldEditor()
            tituloSituacao(situacao).textFieldEditor()
            niSituacao(situacao).textFieldEditor()
          }
        }
      }
      if (serie in listOf(Serie01, NFD, FIN, PED)) {
        if (serie in listOf(PED)) {
          usuarioSituacao(situacao)
          situacaoDesconto(situacao)
        }
        dataAgendaDesconto(situacao).dateFieldEditor()
        chaveDesconto().textFieldEditor().apply {
          this.setClassNameGenerator {
            it.situacaoPendencia?.cssCor
          }
        }
      }
      notaValor().apply {
        val totalPedido = listNotas.sumOf { it.valorNota }.format()
        setFooter(Html("<b><font size=4>${totalPedido}</font></b>"))
      }
      if (serie in listOf(PED, AJT, AJC, AJD, AJP, A66)) {
        sort(listOf(GridSortOrder(getColumnBy(NotaSaida::dataPedido), SortDirection.ASCENDING)))
      } else {
        sort(listOf(GridSortOrder(getColumnBy(NotaSaida::dataNota), SortDirection.ASCENDING)))
      }
    }
  }
}