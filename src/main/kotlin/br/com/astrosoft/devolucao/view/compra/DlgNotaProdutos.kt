package br.com.astrosoft.devolucao.view.compra

import br.com.astrosoft.devolucao.model.beans.PedidoCompra
import br.com.astrosoft.devolucao.model.beans.PedidoCompraProduto
import br.com.astrosoft.devolucao.model.pdftxt.ExportTxt
import br.com.astrosoft.devolucao.model.pdftxt.FileText
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colBarcode
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colCodigo
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colCusto
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colDescNota
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colDescricao
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colGrade
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colItem
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colQtde
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colRefFabrica
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colRefNota
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colUnidade
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colVlTotal
import br.com.astrosoft.devolucao.viewmodel.compra.ITabCompraConfViewModel
import br.com.astrosoft.devolucao.viewmodel.compra.ITabCompraViewModel
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.util.lpad
import br.com.astrosoft.framework.view.*
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.kaributools.fetchAll
import com.github.mvysny.kaributools.setSortOrder
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.upload.FileRejectedEvent
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MemoryBuffer

class DlgNotaProdutos(val viewModel: ITabCompraViewModel) {
  private lateinit var gridNota: Grid<PedidoCompraProduto>

  fun showDialogNota(pedido: PedidoCompra?) {
    pedido ?: return
    if (viewModel is ITabCompraConfViewModel) {
      viewModel.setFileText(null)
      pedido.produtos.sortedBy { it.codigo }.forEach { pedidoCompraProduto ->
        pedidoCompraProduto.item = ""
        pedidoCompraProduto.linha = 0
      }
      pedido.toPdf()?.let { bytes ->
        val bytesTxt = ExportTxt.toTxt(bytes)
        val fileText = FileText.fromFile(bytesTxt)
        viewModel.setFileText(fileText)
        pedido.produtos.forEach { produto ->
          viewModel.findLineByProduto(produto)?.let { line ->
            val codigos = produto.listCodigo()
            val item = line.item()
            if( item !in codigos) {
              produto.linha = line.num
              produto.item = item
              produto.line = line
            }
          }
        }
      }
    }

    val produtos = pedido.produtos
    val form = SubWindowForm(pedido.labelTitle, toolBar = {
      button("PDF") {
        icon = VaadinIcon.PRINT.create()
        onLeftClick {
          val produtosList = gridNota.asMultiSelect().selectedItems.toList()
          viewModel.imprimirPedidoCompra(PedidoCompra.group(produtosList))
        }
      }
      this.lazyDownloadButtonXlsx("Planilha", "produtosCompra") {
        val produtosList = gridNota.asMultiSelect().selectedItems.toList()
        viewModel.excelPedidoCompra(PedidoCompra.group(produtosList))
      }

      if (viewModel is ITabCompraConfViewModel) {
        this.uploadFile { buffer, upload ->
          upload.isDropAllowed = false
          upload.addSucceededListener {
            val bytesPDF = buffer.inputStream.readBytes()
            val bytesTxt = ExportTxt.toTxt(bytesPDF)

            val fileText = FileText.fromFile(bytesTxt)
            viewModel.setFileText(fileText)
            gridNota.dataProvider.refreshAll()
            viewModel.savePdfPedido(pedido, bytesPDF)
          }
        }
        this.button("Remover Pedido") {
          icon = VaadinIcon.TRASH.create()
          onLeftClick {
            viewModel.removePedido(pedido)
            gridNota.dataProvider.refreshAll()
          }
        }
        this.button("Exibir Pedido") {
          icon = VaadinIcon.PRINT.create()
          onLeftClick {
            val bytes = pedido.toPdf()
            if (bytes != null) {
              SubWindowPDF(pedido.numeroPedido.toString(), bytes).open()
            }
          }
        }
        this.button("Confirma Pedido") {
          icon = VaadinIcon.CHECK.create()
          onLeftClick {
            val itens = gridNota.selectedItems
            viewModel.confirmaProdutoSelecionado(itens)
          }
        }
      }
    }) {
      gridNota = createGrid(produtos)
      HorizontalLayout().apply {
        setSizeFull()
        addAndExpand(gridNota)
      }
    }
    form.open()
  }

  private fun createGrid(listParcelas: List<PedidoCompraProduto>): Grid<PedidoCompraProduto> {
    return Grid(PedidoCompraProduto::class.java, false).apply<Grid<PedidoCompraProduto>> {
      setSizeFull()
      addThemeVariants(GridVariant.LUMO_COMPACT)
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)
      setItems(listParcelas)
      colItem()
      colCodigo()
      colBarcode()
      colDescricao()
      colGrade()
      colRefFabrica().apply {
        this.setClassNameGenerator { produto ->
          if (viewModel is ITabCompraConfViewModel) {
            if (viewModel.pedidoOK()) {
              val line = produto.line
              if (line?.find(produto.refFab) == true) "marcaOk"
              else "marcaError"
            }
            else ""
          }
          else ""
        }
      }
      colDescNota()
      colRefNota().apply {
        this.setClassNameGenerator { produto ->
          if (viewModel is ITabCompraConfViewModel) {
            if (viewModel.pedidoOK()) {
              val line = produto.line
              val listRef = produto.refno?.split("/").orEmpty()
              if (listRef.any { line?.find(it) == true }) "marcaOk"
              else "marcaError"
            }
            else ""
          }
          else ""
        }
      }
      colUnidade()
      colQtde().apply {
        this.setClassNameGenerator { produto ->
          if (viewModel is ITabCompraConfViewModel) {
            if (viewModel.pedidoOK()) {
              val line = produto.line
              if (line?.find(produto.qtPedida) == true) "marcaOk"
              else "marcaError"
            }
            else ""
          }
          else ""
        }
      }
      colCusto().apply {
        this.setClassNameGenerator { produto ->
          if (viewModel is ITabCompraConfViewModel) {
            if (viewModel.pedidoOK()) {
              val line = produto.line
              if (line?.find(produto.custoUnit) == true) "marcaOk"
              else "marcaError"
            }
            else ""
          }
          else ""
        }
      }
      colVlTotal().let { col ->
        val lista = this.dataProvider.fetchAll()
        val total = lista.sumOf { it.vlPedido ?: 0.00 }.format()
        col.setFooter(Html("<b><font size=4>${total}</font></b>"))
      }

      val colLinha = addColumnInt(PedidoCompraProduto::linha) {
        this.isVisible = false
      }

      this.setSortOrder(GridSortOrder.asc(colLinha).build())
    }
  }

  private fun HasComponents.uploadFile(exec: (buffer: MemoryBuffer, upload: Upload) -> Unit) {
    val buffer = MemoryBuffer()
    val upload = Upload(buffer)
    upload.setAcceptedFileTypes("application/pdf", ".pdf")
    val uploadButton = Button("Carregar Pedido")
    uploadButton.icon = VaadinIcon.PLUS.create()
    upload.uploadButton = uploadButton
    upload.isAutoUpload = true
    upload.maxFileSize = 1024 * 1024 * 1024
    upload.i18n = UploadPtBr()
    upload.addFileRejectedListener { event: FileRejectedEvent ->
      println(event.errorMessage)
    }
    upload.addFailedListener { event ->
      println(event.reason.message)
    }
    add(upload)
    exec(buffer, upload)
  }
}
