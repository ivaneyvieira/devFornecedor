package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.Fornecedor
import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.chaveDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.dataAgendaDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.dataSituacaoDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.docSituacao
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.niSituacao
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaDataNota
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaDataPedido
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaFatura
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaLoja
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaNota
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaObservacao
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaPedido
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaSituacao
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaValor
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.situacaoDesconto
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.tituloSituacao
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.usuarioSituacao
import br.com.astrosoft.devolucao.viewmodel.devolucao.*
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.*
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.comboBox
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.kaributools.getColumnBy
import com.vaadin.flow.component.Focusable
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.checkbox.CheckboxGroup
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.data.provider.SortDirection
import org.claspina.confirmdialog.ConfirmDialog

@CssImport("./styles/gridTotal.css")
class DlgNota<T : IDevolucaoAbstractView>(val viewModel: TabDevolucaoViewModelAbstract<T>,
                                          val situacao: ESituacaoPendencia?) {
  lateinit var gridNota: Grid<NotaSaida>
  fun showDialogNota(fornecedor: Fornecedor?, serie: Serie, onClose: (Dialog) -> Unit = {}) {
    fornecedor ?: return
    val listNotas = fornecedor.notas
    val form = SubWindowForm(fornecedor.labelTitle, toolBar = {
      if (serie == Serie.PED) {
        button("Relatório Pedido") {
          icon = VaadinIcon.PRINT.create()
          onLeftClick {
            val notas = gridNota.asMultiSelect().selectedItems.toList()
            viewModel.imprimirRelatorioPedidos(notas)
          }
        }
      }

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
      this.lazyDownloadButtonXlsx("Planilha", "planilha") {
        val notas = gridNota.asMultiSelect().selectedItems.toList()
        viewModel.geraPlanilha(notas, serie)
      }
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

      if (viewModel is TabNotaPendenteViewModel) {
        val cmbSituacao = comboBox<ESituacaoPendencia>("Situação") {
          setItems(ESituacaoPendencia.values().filter { !it.valueStr.isNullOrBlank() })
          setItemLabelGenerator {
            it.title
          }
          isAutoOpen = true
          isClearButtonVisible = false
          isPreventInvalidInput = true
        }
        button("Muda situação") {
          onLeftClick {
            val itens = gridNota.selectedItems.toList()
            viewModel.salvaSituacao(cmbSituacao.value, itens)
          }
        }
      }
      else {
        if (viewModel is TabPedidoViewModel) {
          val cmbSituacaoPedido = comboBox<ESituacaoPedido>("Situação") {
            setItems(ESituacaoPedido.values().toList())
            setItemLabelGenerator {
              it.descricao
            }
            isAutoOpen = true
            isClearButtonVisible = false
            isPreventInvalidInput = true
          }

          button("Muda situação") {
            onLeftClick {
              val itens = gridNota.selectedItems.toList()
              viewModel.salvaSituacaoPedido(cmbSituacaoPedido.value, itens)
            }
          }
        }
      }
    }, onClose = onClose) {
      gridNota = createGridNotas(listNotas, serie)
      gridNota
    }
    form.open()
  }

  private fun createGridNotas(listNotas: List<NotaSaida>, serie: Serie): Grid<NotaSaida> {
    val gridDetail = Grid(NotaSaida::class.java, false)
    return gridDetail.apply {
      addThemeVariants(GridVariant.LUMO_COMPACT)
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)
      setItems(listNotas)
      if (serie in listOf(Serie.Serie01, Serie.PED)) {
        this.withEditor(NotaSaida::class, openEditor = {
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
      if (viewModel !is TabNotaPendenteViewModel) {
        addColumnButton(VaadinIcon.ENVELOPE_O, "Editor", "Email", ::configMostraEmail) { nota ->
          viewModel.mostrarEmailNota(nota)
        }
      }

      notaLoja()
      if (serie !in listOf(Serie.Serie01, Serie.FIN)) {
        notaDataPedido()
        notaPedido()
      }
      notaDataNota()
      if (serie !in listOf(Serie.PED)) {
        notaNota()
      }
      if (serie in listOf(Serie.AJP)) {
        notaObservacao()
      }
      else {
        if (serie !in listOf(Serie.PED)) {
          notaFatura()
        }
      }
      if (viewModel is TabNotaPendenteViewModel) {
        usuarioSituacao(situacao)
        dataSituacaoDesconto(situacao).apply {
          this.isVisible = false
        }
        situacaoDesconto(situacao)
        if (situacao == ESituacaoPendencia.CREDITO_CONTA) {
          niSituacao(situacao).textFieldEditor()
          docSituacao(situacao).textFieldEditor()
          notaSituacao(situacao).textFieldEditor()
          tituloSituacao(situacao).textFieldEditor()
        }
        else {
          docSituacao(situacao).textFieldEditor()
          notaSituacao(situacao).textFieldEditor()
          tituloSituacao(situacao).textFieldEditor()
          niSituacao(situacao).textFieldEditor()
        }
      }
      if (serie in listOf(Serie.Serie01, Serie.FIN, Serie.PED)) {
        if (serie in listOf(Serie.PED)) {
          usuarioSituacao(situacao)
          situacaoDesconto(situacao)
        }
        dataAgendaDesconto(situacao).dateFieldEditor()
        chaveDesconto().textFieldEditor().apply {
          this.setClassNameGenerator {
            "marcaRed"
          }
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

  fun updateNota() {
    gridNota.dataProvider.refreshAll()
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