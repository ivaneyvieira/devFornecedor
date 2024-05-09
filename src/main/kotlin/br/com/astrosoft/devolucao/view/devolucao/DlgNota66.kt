package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaDataNfAjuste
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaDataNota
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaDataPedido
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaLoja
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaNota
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaObservacao
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaPedido
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaValor
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.situacaoDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.usuarioSituacao
import br.com.astrosoft.devolucao.viewmodel.devolucao.*
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.addColumnButton
import br.com.astrosoft.framework.view.dateFieldEditor
import com.github.mvysny.kaributools.getColumnBy
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.data.provider.SortDirection

@CssImport("./styles/gridTotal.css")
class DlgNota66<T : IDevolucaoAbstractView>(viewModel: TabDevolucaoViewModelAbstract<T>) :
  DlgNotaAbstract<T>(viewModel) {
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
      notaDataPedido()
      notaPedido()
      notaDataNota()
      notaNota()
      notaDataNfAjuste().dateFieldEditor()
      //notaNfAjuste().textFieldEditor()
      //notaFatura()
      usuarioSituacao(situacao)
      situacaoDesconto(situacao)
      //docSituacao(situacao).textFieldEditor()
      //notaSituacao(situacao).textFieldEditor()
      //tituloSituacao(situacao).textFieldEditor()

      notaObservacao()

      // niSituacao(situacao).textFieldEditor()
      notaValor().apply {
        val totalPedido = listNotas.sumOf { it.valorNota }.format()
        setFooter(Html("<b><font size=4>${totalPedido}</font></b>"))
      }
      sort(listOf(GridSortOrder(getColumnBy(NotaSaida::dataNota), SortDirection.ASCENDING)))
    }
  }
}