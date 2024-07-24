package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.MonitoramentoEntradaFornecedor
import br.com.astrosoft.devolucao.model.beans.MonitoramentoEntradaNota
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabMonitoramentoEntradaViewModel
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.lazyDownloadButtonXlsx
import br.com.astrosoft.framework.view.vaadin.columnGrid
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.data.value.ValueChangeMode

@CssImport("./styles/gridTotal.css")
class DlgNotaEntrada(val viewModel: TabMonitoramentoEntradaViewModel) {
  lateinit var gridNota: Grid<MonitoramentoEntradaNota>
  fun showDialogNota(
    monitoramento: MonitoramentoEntradaFornecedor,
    onClose: (Dialog) -> Unit = {}
  ) {
    val listNotas = monitoramento.notas
    val form = SubWindowForm(monitoramento.labelTitle, toolBar = {
      textField("Pesquisa") {
        this.valueChangeTimeout = 1000
        this.valueChangeMode = ValueChangeMode.LAZY
        addValueChangeListener {
          val filter = it.value
          gridNota.setItems(listNotas.filter { nota ->
            nota.nota.contains(filter, true) ||
            nota.pedido.toString().contains(filter, true) ||
            nota.dataEntrada.format().contains(filter, true) ||
            nota.observacao.contains(filter, true)
          })
        }
      }

      button("Impressão Resumida") {
        icon = VaadinIcon.PRINT.create()
        onLeftClick {
          val notas = gridNota.asMultiSelect().selectedItems.toList()
          // viewModel.imprimirNotaDevolucao(notas, resumida = true)
        }
      }
      this.lazyDownloadButtonXlsx("Planilha", "planilha") {
        val notas = gridNota.asMultiSelect().selectedItems.toList()
        //viewModel.geraPlanilha(notas, serie)
        ByteArray(0)
      }
    }, onClose = onClose) {
      gridNota = createGridNotas(listNotas)
      gridNota
    }
    form.open()
  }

  fun createGridNotas(listNotas: List<MonitoramentoEntradaNota>): Grid<MonitoramentoEntradaNota> {
    val gridDetail = Grid(MonitoramentoEntradaNota::class.java, false)
    return gridDetail.apply {
      this.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COLUMN_BORDERS)
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)
      setItems(listNotas)

      columnGrid(MonitoramentoEntradaNota::loja, "Loja")
      columnGrid(MonitoramentoEntradaNota::nota, "Nota")
      columnGrid(MonitoramentoEntradaNota::pedido, "Pedido")
      columnGrid(MonitoramentoEntradaNota::dataEntrada, "Data")
      columnGrid(MonitoramentoEntradaNota::observacao, "Observação")
      columnGrid(MonitoramentoEntradaNota::valorNota, "Valor")
    }
  }

  fun updateNota() {
    gridNota.dataProvider.refreshAll()
  }
}

