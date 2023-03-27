package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.chaveDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.dataAgendaDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.dataSituacaoDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaBancoFatura
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaDataNota
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaFatura
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaLoja
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaNfAjuste
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaNota
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaObsFatura
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaSituacaoFatura
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaValor
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.situacaoDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.usuarioSituacao
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
class DlgNotaRetorno<T : IDevolucaoAbstractView>(viewModel: TabDevolucaoViewModelAbstract<T>) :
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

      notaLoja()
      notaDataNota()
      notaNota()

      notaNfAjuste()
      notaFatura()
      notaBancoFatura()
      notaSituacaoFatura()
      usuarioSituacao(situacao)
      situacaoDesconto(situacao)

      notaObsFatura()
      chaveDesconto().textFieldEditor().apply {
        this.setClassNameGenerator {
          it.situacaoPendencia?.cssCor
        }
      }
      notaValor().apply {
        val totalPedido = listNotas.sumOf { it.valorNota }.format()
        setFooter(Html("<b><font size=4>${totalPedido}</font></b>"))
      }
    }
  }
}