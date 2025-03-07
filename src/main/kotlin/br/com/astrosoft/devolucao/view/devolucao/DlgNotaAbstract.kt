package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.Fornecedor
import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.viewmodel.devolucao.*
import br.com.astrosoft.devolucao.viewmodel.devolucao.Serie.*
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.lazyDownloadButton
import br.com.astrosoft.framework.view.lazyDownloadButtonXlsx
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.comboBox
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.checkbox.CheckboxGroup
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.data.value.ValueChangeMode
import org.claspina.confirmdialog.ConfirmDialog

@CssImport("./styles/gridTotal.css")
abstract class DlgNotaAbstract<T : IDevolucaoAbstractView>(val viewModel: TabDevolucaoViewModelAbstract<T>) {
  lateinit var gridNota: Grid<NotaSaida>
  fun showDialogNota(
    fornecedor: Fornecedor?,
    serie: Serie,
    situacao: ESituacaoPendencia?,
    onClose: (Dialog) -> Unit = {}
  ) {
    fornecedor ?: return
    val listNotas = fornecedor.notas
    val form = SubWindowForm(fornecedor.labelTitle, toolBar = {
      textField("Pesquisa") {
        this.valueChangeTimeout = 1000
        this.valueChangeMode = ValueChangeMode.LAZY
        addValueChangeListener {
          val filter = it.value
          gridNota.setItems(listNotas.filter { nota ->
            nota.nota.contains(filter, true) ||
            nota.pedido.toString().contains(filter, true) ||
            nota.pedidos.orEmpty().contains(filter, true) ||
            nota.usuarioSituacao.contains(filter, true) ||
            nota.dataNota.format().contains(filter, true) ||
            nota.situacaoStr.contains(filter, true) ||
            nota.remarks.contains(filter, true) ||
            nota.chaveDesconto.orEmpty().contains(filter, true)
          })
        }
      }
      if (serie == PED) {
        button("Relatório Pedido") {
          icon = VaadinIcon.PRINT.create()
          onLeftClick {
            val notas = gridNota.asMultiSelect().selectedItems.toList()
            viewModel.imprimirRelatorioPedidos(notas)
          }
        }
        this.lazyDownloadButton(
          text = "Planilha Pedido",
          icon = FontAwesome.Solid.FILE_EXCEL.create(),
          fileName = {
            "Pedido ${fornecedor.fornecedor.trim().split(" ").getOrNull(0) ?: ""}.xlsx"
          }) {
          val notas = gridNota.asMultiSelect().selectedItems.toList()
          if (notas.isEmpty()) {
            ByteArray(0)
          } else
            viewModel.excelPedido(notas)
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
                viewModel.imprimirNotaFornecedor(
                  notas,
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
        this.lazyDownloadButton(text = "Excel", icon = FontAwesome.Solid.FILE_EXCEL.create(), fileName = {
          "Devolução ${fornecedor.fornecedor.trim().split(" ").getOrNull(0) ?: ""}.xlsx"
        }) {
          val notas = gridNota.asMultiSelect().selectedItems.toList()
          viewModel.excelRelatorio(notas)
        }
      }

      when {
        serie in listOf(Serie01)                                                               -> {
          val cmbSituacao = comboBox<ESituacaoPendencia>("Situação") {
            setItems(ESituacaoPendencia.entries.filter { !it.valueStr.isNullOrBlank() })
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

        viewModel is TabPedidoEditorViewModel || viewModel is TabPedidoPendenteViewModel       -> {
          val cmbSituacaoPedido = comboBox<ESituacaoPedido>("Situação") {

            setItems(ESituacaoPedido.entries)

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

        viewModel is TabAvariaRecEditorViewModel || viewModel is TabAvariaRecPendenteViewModel -> {
          val cmbSituacaoPedido = comboBox<ESituacaoPedido>("Situação") {
            if (viewModel is TabAvariaRecPendenteViewModel) {
              setItems(
                listOf(
                  ESituacaoPedido.NFD_AUTOZ,
                  ESituacaoPedido.ACERTO,
                  ESituacaoPedido.REPOSTO,
                )
              )
            } else {
              setItems(ESituacaoPedido.entries.filter { it.avaria })
            }

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

        viewModel is TabAvariaRecTransportadoraViewModel                                       -> {
          button("Volta") {
            this.icon = VaadinIcon.ARROW_LEFT.create()
            onLeftClick {
              val itens = gridNota.selectedItems.toList()
              viewModel.salvaSituacaoPedido(ESituacaoPedido.NFD_AUTOZ, itens)
              itens.forEach {
                gridNota.dataProvider.refreshItem(it)
              }
            }
          }
          button("E-mail") {
            this.icon = VaadinIcon.ARROW_RIGHT.create()
            onLeftClick {
              val itens = gridNota.selectedItems.toList()
              viewModel.salvaSituacaoPedido(ESituacaoPedido.EMAIL_ENVIADO, itens)
              itens.forEach {
                gridNota.dataProvider.refreshItem(it)
              }
            }
          }
        }

        viewModel is TabAvariaRecEmailViewModel                                                -> {
          button("Volta") {
            this.icon = VaadinIcon.ARROW_LEFT.create()
            onLeftClick {
              val itens = gridNota.selectedItems.toList()
              viewModel.salvaSituacaoPedido(ESituacaoPedido.NFD_AUTOZ, itens)
              itens.forEach {
                gridNota.dataProvider.refreshItem(it)
              }
            }
          }
        }

        viewModel is TabAvariaRecNFDViewModel                                                  -> {
          button("Volta") {
            this.icon = VaadinIcon.ARROW_LEFT.create()
            onLeftClick {
              val itens = gridNota.selectedItems.toList()
              viewModel.salvaSituacaoPedido(ESituacaoPedido.VAZIO, itens)
              itens.forEach {
                gridNota.dataProvider.refreshItem(it)
              }
            }
          }
          button("Transportadora") {
            this.icon = VaadinIcon.ARROW_RIGHT.create()
            onLeftClick {
              val itens = gridNota.selectedItems.toList()
              viewModel.salvaSituacaoPedido(ESituacaoPedido.TRANSPORTADORA, itens)
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

  abstract fun createGridNotas(
    listNotas: List<NotaSaida>,
    serie: Serie,
    situacao: ESituacaoPendencia?
  ): Grid<NotaSaida>

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
  DefeitoFabrica(6, "Defeito de Fabricação"),
  ProdutoPrestesVencer(7, "Produto Prestes a Vencer"),
  ProdutoVencido(7, "Produto Vencido"),
}