package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.chaveDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.dataNotaEditavel
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaDataNota
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaLoja
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaNfAjuste
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaObservacao
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaPedidoEditavel
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaValor
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaValorPago
import br.com.astrosoft.devolucao.viewmodel.devolucao.*
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.*
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
class DlgNotaGarantia<T : IDevolucaoAbstractView>(viewModel: TabDevolucaoViewModelAbstract<T>) :
        DlgNotaAbstract<T>(viewModel) {
  override fun createGridNotas(listNotas: List<NotaSaida>,
                               serie: Serie,
                               situacao: ESituacaoPendencia?): Grid<NotaSaida> {
    val gridDetail = Grid(NotaSaida::class.java, false)
    return gridDetail.apply {
      addThemeVariants()
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)
      updateNota(listNotas)
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
      if (viewModel !is TabNotaPendenteViewModel) {
        addColumnButton(VaadinIcon.ENVELOPE_O, "Editor", "Email", ::configMostraEmail) { nota ->
          viewModel.mostrarEmailNota(nota)
        }
      }

      notaLoja() //notaDataPedido()
      dataNotaEditavel(null).dateFieldEditor().apply {
        this.setHeader("Data")
      }
      notaPedidoEditavel().textFieldEditor()
      notaDataNota()
      notaNfAjuste()
      chaveDesconto("Observação").textFieldEditor().apply {
        this.setClassNameGenerator {
          it.situacaoPendencia?.cssCor
        }
      } //notaNiBonificacao().textFieldEditor()
      //notaNiValor().textFieldEditor()
      notaObservacao()
      notaValorPago().decimalFieldEditor()
      notaValor().apply {
        val totalPedido = listNotas.sumOf { it.valorNota }.format()
        setFooter(Html("<b><font size=4>${totalPedido}</font></b>"))
      }
      sort(listOf(GridSortOrder(getColumnBy(NotaSaida::dataNotaEditavel), SortDirection.ASCENDING)))
    }
  }

  fun updateNota(itens: List<NotaSaida>) {
    itens.forEach {
      if (it.dataNotaEditavel == null) it.dataNotaEditavel = it.dataPedido
    }
  }
}