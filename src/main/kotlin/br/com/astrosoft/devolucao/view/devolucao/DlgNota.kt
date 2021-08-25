package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.Fornecedor
import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.chaveDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaDataNota
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaDataPedido
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaFatura
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaLoja
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaNota
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaObservacao
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaPedido
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaValor
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucaoAbstractView
import br.com.astrosoft.devolucao.viewmodel.devolucao.Serie
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabDevolucaoViewModelAbstract
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.addColumnButton
import br.com.astrosoft.framework.view.textFieldEditor
import br.com.astrosoft.framework.view.withEditor
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.getColumnBy
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.vaadin.flow.component.Focusable
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.checkbox.CheckboxGroup
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.data.provider.SortDirection
import org.claspina.confirmdialog.ConfirmDialog
import org.vaadin.stefan.LazyDownloadButton
import java.io.ByteArrayInputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@CssImport("./styles/gridTotal.css")
class DlgNota<T : IDevolucaoAbstractView>(val viewModel: TabDevolucaoViewModelAbstract<T>) {
  fun showDialogNota(fornecedor: Fornecedor?, serie: Serie) {
    fornecedor ?: return
    lateinit var gridNota: Grid<NotaSaida>
    val listNotas = fornecedor.notas
    val form = SubWindowForm(fornecedor.labelTitle, toolBar = {
      val captionImpressoa =
              if (serie == Serie.Serie66 || serie == Serie.PED || serie == Serie.AJT) "Impressão Completa"
              else "Impressão"
      button(captionImpressoa) {
        icon = VaadinIcon.PRINT.create()
        onLeftClick {
          val notas = gridNota.asMultiSelect().selectedItems.toList()
          viewModel.imprimirNotaDevolucao(notas)
        }
      }
      if (serie == Serie.Serie01) {
        button("Impressão Fornecedor") {
          icon = VaadinIcon.PRINT.create()
          onLeftClick {
            val multList = CheckboxGroup<EOcorrencias>()
            multList.setItems(EOcorrencias.values().toList().sortedBy { it.num })
            multList.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL)
            multList.setItemLabelGenerator { it.descricao }
            ConfirmDialog.createQuestion()
              .withCaption("Lista de Ocorrência")
              .withMessage(multList)
              .withOkButton({
                              val notas =
                                      gridNota.asMultiSelect().selectedItems.toList()
                              viewModel.imprimirNotaFornecedor(notas,
                                                               multList.value.toList()
                                                                 .sortedBy { it.num }
                                                                 .map { it.descricao })
                            })
              .withCancelButton()
              .open()
          }
        }
      }
      if (serie == Serie.Serie66 || serie == Serie.PED || serie == Serie.AJT) {
        button("Impressão Resumida") {
          icon = VaadinIcon.PRINT.create()
          onLeftClick {
            val notas = gridNota.asMultiSelect().selectedItems.toList()
            viewModel.imprimirNotaDevolucao(notas, resumida = true)
          }
        }
      }
      this.add(buttonPlanilha(serie) {
        gridNota.asMultiSelect().selectedItems.toList()
      })
      button("Email") {
        icon = VaadinIcon.ENVELOPE_O.create()
        onLeftClick {
          val notas = gridNota.asMultiSelect().selectedItems.toList()
          viewModel.enviarEmail(notas)
        }
      }
      if (serie in listOf(Serie.Serie01, Serie.Serie66)) {
        button("Relatório") {
          icon = VaadinIcon.PRINT.create()
          onLeftClick {
            val notas = gridNota.asMultiSelect().selectedItems.toList()
            viewModel.imprimirRelatorio(notas)
          }
        }
      }
    }) {
      gridNota = createGridNotas(listNotas, serie)
      gridNota
    }
    form.open()
  }

  private fun buttonPlanilha(serie: Serie, notas: () -> List<NotaSaida>): LazyDownloadButton {
    return LazyDownloadButton("Planilha", FontAwesome.Solid.FILE_EXCEL.create(), ::filename) {
      ByteArrayInputStream(viewModel.geraPlanilha(notas(), serie))
    }
  }

  private fun filename(): String {
    val sdf = DateTimeFormatter.ofPattern("yyMMddHHmmss")
    val textTime = LocalDateTime.now().format(sdf)
    return "notas$textTime.xlsx"
  }

  private fun createGridNotas(listNotas: List<NotaSaida>, serie: Serie): Grid<NotaSaida> {
    val gridDetail = Grid(NotaSaida::class.java, false)
    return gridDetail.apply {
      addThemeVariants(GridVariant.LUMO_COMPACT)
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)
      setItems(listNotas)
      if (serie in listOf(Serie.Serie01, Serie.PED)) {
        this.withEditor(NotaSaida::class, openEditor = { _ ->
          (getColumnBy(NotaSaida::chaveDesconto).editorComponent as? Focusable<*>)?.focus()
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
      addColumnButton(VaadinIcon.ENVELOPE_O, "Editor", "Email", ::configMostraEmail) { nota ->
        viewModel.mostrarEmailNota(nota)
      }

      notaLoja()
      if (serie !in listOf(Serie.Serie01, Serie.FIN)) {
        notaDataPedido()
        notaPedido()
      }
      notaDataNota()
      notaNota()
      if (serie in listOf(Serie.AJP)) {
        notaObservacao()
      }
      else {
        notaFatura()
      }
      if (serie in listOf(Serie.Serie01, Serie.FIN, Serie.PED)) {
        chaveDesconto().textFieldEditor().apply {
          this.setClassNameGenerator { "marcaDiferenca" }
        }
      }
      notaValor().apply {
        val totalPedido = listNotas.sumOf { it.valorNota }.format()
        setFooter(Html("<b><font size=4>${totalPedido}</font></b>"))
      }
      if (serie == Serie.PED || serie == Serie.AJT) sort(listOf(GridSortOrder(getColumnBy(NotaSaida::dataPedido),
                                                                              SortDirection.ASCENDING)))
      else sort(listOf(GridSortOrder(getColumnBy(NotaSaida::dataNota), SortDirection.ASCENDING)))
    }
  }

  private fun configIconEdt(icon: Icon, nota: NotaSaida) {
    if (nota.rmk.isNotBlank()) icon.color = "DarkGreen"
    else icon.color = ""
  }

  private fun configMostraEmail(icon: Icon, nota: NotaSaida) {
    if (nota.listEmailNota().isNotEmpty()) icon.color = "DarkGreen"
    else icon.color = ""
  }

  private fun configIconArq(icon: Icon, nota: NotaSaida) {
    if (nota.listFiles().isNotEmpty()) icon.color = "DarkGreen"
    else icon.color = ""
  }
}

enum class EOcorrencias(val num: Int, val descricao: String) {
  AvariaTransporte(1, "Avaria no Transporte"),
  AvariaFabrica(2, "Avaria de Fabrica"),
  FaltaTransporte(3, "Falta no Transporte"),
  FaltaFabrica(4, "Falta de Fabrica"),
  ProdutoDesacordo(5, "Produto em Desacordo com o Pedido"),
  DefeitoFabrica(6, "Defeito de Fabricação")
}