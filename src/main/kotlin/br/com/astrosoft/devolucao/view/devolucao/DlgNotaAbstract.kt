package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.Fornecedor
import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.viewmodel.devolucao.*
import br.com.astrosoft.devolucao.viewmodel.devolucao.Serie.*
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.lazyDownloadButtonXlsx
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.comboBox
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.vaadin.flow.component.checkbox.CheckboxGroup
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import org.claspina.confirmdialog.ConfirmDialog

@CssImport("./styles/gridTotal.css")
abstract class DlgNotaAbstract<T : IDevolucaoAbstractView>(val viewModel: TabDevolucaoViewModelAbstract<T>) {
  lateinit var gridNota: Grid<NotaSaida>
  fun showDialogNota(fornecedor: Fornecedor?,
                     serie: Serie,
                     situacao: ESituacaoPendencia?,
                     onClose: (Dialog) -> Unit = {}) {
    fornecedor ?: return
    val listNotas = fornecedor.notas
    val form = SubWindowForm(fornecedor.labelTitle, toolBar = {
      if (serie == PED) {
        button("Relatório Pedido") {
          icon = VaadinIcon.PRINT.create()
          onLeftClick {
            val notas = gridNota.asMultiSelect().selectedItems.toList()
            viewModel.imprimirRelatorioPedidos(notas)
          }
        }
      }

      val captionImpressoa = if (serie in listOf(Serie66, PED, AJT, AJC, AJD, AJP, A66)) "Impressão Completa"
      else "Impressão"
      button(captionImpressoa) {
        icon = VaadinIcon.PRINT.create()
        onLeftClick {
          val notas = gridNota.asMultiSelect().selectedItems.toList()
          viewModel.imprimirNotaDevolucao(notas)
        }
      }

      if (serie == Serie01) {
        button("Impressão Fornecedor") {
          icon = VaadinIcon.PRINT.create()
          onLeftClick {
            val multList = CheckboxGroup<EOcorrencias>()
            multList.setItems(EOcorrencias.values().toList().sortedBy { it.num })
            multList.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL)
            multList.setItemLabelGenerator { it.descricao }
            ConfirmDialog
              .createQuestion()
              .withCaption("Lista de Ocorrência")
              .withMessage(multList)
              .withOkButton({
                              val notas =
                                gridNota.asMultiSelect().selectedItems.toList()
                              viewModel.imprimirNotaFornecedor(notas,
                                                               multList.value
                                                                 .toList()
                                                                 .sortedBy { it.num }
                                                                 .map { it.descricao })
                            })
              .withCancelButton()
              .open()
          }
        }
      }
      if (serie in listOf(Serie66, PED, AJT, AJC, AJD, AJP, A66)) {
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
      if (serie in listOf(Serie01, Serie66)) {
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
            itens.forEach {
              gridNota.dataProvider.refreshItem(it)
            }
          }
        }
      }
      else {
        if (viewModel is TabPedidoBaseViewModel || viewModel is TabPedidoPendenteViewModel || viewModel is
                        TabPedidoLiberadoViewModel || viewModel is TabPedidoEmailViewModel || viewModel is TabPedidoAjusteViewModel) {
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
              itens.forEach {
                gridNota.dataProvider.refreshItem(it)
              }
            }
          }
        }
      }
    }, onClose = onClose) {
      gridNota = createGridNotas(listNotas, serie, situacao)
      gridNota
    }
    form.open()
  }

  abstract fun createGridNotas(listNotas: List<NotaSaida>, serie: Serie, situacao: ESituacaoPendencia?): Grid<NotaSaida>

  protected fun configIconEdt(icon: Icon, nota: NotaSaida) {
    if (nota.rmk.isNotBlank()) icon.color = "DarkGreen"
    else icon.color = ""
  }

  protected fun configMostraEmail(icon: Icon, nota: NotaSaida) {
    if (nota.listEmailNota().isNotEmpty()) icon.color = "DarkGreen"
    else icon.color = ""
  }

  protected fun configIconArq(icon: Icon, nota: NotaSaida) {
    if (nota.listFiles().isNotEmpty()) icon.color = "DarkGreen"
    else icon.color = ""
  }

  open fun updateNota() {
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