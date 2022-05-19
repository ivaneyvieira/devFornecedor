package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.chaveDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.dataAgendaDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.dataBonificacao
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.dataNotaEditavel
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaBonificacao
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaDataNota
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaDataPedido
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaEditavel
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaLoja
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaNiBonificacao
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaPedido
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaValor
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.situacaoDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.usuarioSituacao
import br.com.astrosoft.devolucao.viewmodel.devolucao.*
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.addColumnButton
import br.com.astrosoft.framework.view.dateFieldEditor
import br.com.astrosoft.framework.view.textFieldEditor
import br.com.astrosoft.framework.view.withEditor
import com.github.mvysny.kaributools.getColumnBy
import com.vaadin.flow.component.Focusable
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.data.provider.SortDirection

@CssImport("./styles/gridTotal.css")
class DlgNotaPedido<T : IDevolucaoAbstractView>(viewModel: TabDevolucaoViewModelAbstract<T>) :
        DlgNotaAbstract<T>(viewModel) {
  override fun createGridNotas(listNotas: List<NotaSaida>,
                               serie: Serie,
                               situacao: ESituacaoPendencia?): Grid<NotaSaida> {
    val gridDetail = Grid(NotaSaida::class.java, false)
    return gridDetail.apply {
      addThemeVariants(GridVariant.LUMO_COMPACT)
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)
      setItems(listNotas)
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

      addColumnButton(VaadinIcon.FILE_PICTURE, "Arquivos", "Arq", ::configIconArq) { nota ->
        viewModel.editFile(nota)
      }
      addColumnButton(VaadinIcon.EDIT, "Editor", "Edt", ::configIconEdt) { nota ->
        viewModel.editRmk(nota)
      }
      addColumnButton(VaadinIcon.ENVELOPE_O, "Editor", "Email", ::configMostraEmail) { nota ->
        viewModel.mostrarEmailNota(nota)
      }

      notaLoja()
      notaDataPedido()
      notaPedido()
      //notaDataNota()
      if (viewModel !is TabPedidoBaseViewModel && viewModel !is TabPedidoPendenteViewModel) {
        dataNotaEditavel(situacao).dateFieldEditor().marcaAzul()
        notaEditavel(situacao).textFieldEditor().marcaAzul()
      }
      usuarioSituacao(situacao).marcaAzul()
      dataAgendaDesconto(situacao).dateFieldEditor().marcaAzul()
      situacaoDesconto(situacao).marcaAzul()
      if (viewModel !is TabPedidoBaseViewModel && viewModel !is TabPedidoPendenteViewModel) {
        dataBonificacao().dateFieldEditor().marcaAzul()
        notaBonificacao().textFieldEditor().marcaAzul()
        notaNiBonificacao().textFieldEditor().marcaAzul()
      }
      chaveDesconto().textFieldEditor().apply {
        this.setClassNameGenerator {
          it.situacaoPendencia?.cssCor
        }
        marcaAzul()
      }
      notaValor().apply {
        val totalPedido = listNotas.sumOf { it.valorNota }.format()
        setFooter(Html("<b><font size=4>${totalPedido}</font></b>"))
      }
      sort(listOf(GridSortOrder(getColumnBy(NotaSaida::dataPedido), SortDirection.ASCENDING)))
    }
  }
}

fun Grid.Column<NotaSaida>.marcaAzul(): Grid.Column<NotaSaida> {
  this.setClassNameGenerator {
    it.situacaoPendencia?.cssCor ?: "marcaRed"
  }
  return this
}