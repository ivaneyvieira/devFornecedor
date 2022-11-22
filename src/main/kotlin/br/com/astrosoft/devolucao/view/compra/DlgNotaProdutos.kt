package br.com.astrosoft.devolucao.view.compra

import br.com.astrosoft.devolucao.model.beans.NFFile
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
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colQtde
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colRefFabrica
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colRefNota
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colUnidade
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colVlTotal
import br.com.astrosoft.devolucao.viewmodel.compra.ITabCompraViewModel
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.export.ExcelExporter
import br.com.astrosoft.framework.view.lazyDownloadButtonXlsx
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.upload.FileRejectedEvent
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer

class DlgNotaProdutos(val viewModel: ITabCompraViewModel) {
  private lateinit var gridNota: Grid<PedidoCompraProduto>

  fun showDialogNota(pedido: PedidoCompra?) {
    pedido ?: return

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

      if (viewModel.isConf()) {
        this.uploadFile { buffer, upload ->
          upload.addSucceededListener {
            val fileName = it.fileName
            val bytesPDF = buffer.getInputStream(fileName).readBytes()
            val bytesTxt = ExportTxt.toTxt(bytesPDF)

            println(String(bytesTxt))

            val fileText = FileText.fromFile(bytesTxt)
            viewModel.setFileText(fileText)
            gridNota.dataProvider.refreshAll()
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
      colCodigo()
      colBarcode()
      colDescricao()
      colGrade()
      colRefFabrica().apply {
        this.setClassNameGenerator {
          if (viewModel.pedidoOK()) {
            val line = viewModel.findLine(it)
            if (line?.find(it.refFab) == true) "marcaOk"
            else "marcaError"
          }
          else ""
        }
      }
      colDescNota()
      colRefNota().apply {
        this.setClassNameGenerator {
          if (viewModel.pedidoOK()) {
            val line = viewModel.findLine(it)
            if (line?.find(it.refno) == true) "marcaOk"
            else "marcaError"
          }
          else ""
        }
      }
      colUnidade()
      colQtde().apply {
        this.setClassNameGenerator {
          if (viewModel.pedidoOK()) {
            val line = viewModel.findLine(it)
            if (line?.find(it.qtPedida) == true) "marcaOk"
            else "marcaError"
          }
          else ""
        }
      }
      colCusto().apply {
        this.setClassNameGenerator {
          if (viewModel.pedidoOK()) {
            val line = viewModel.findLine(it)
            if (line?.find(it.custoUnit) == true) "marcaOk"
            else "marcaError"
          }
          else ""
        }
      }
      colVlTotal()
    }
  }

  private fun HasComponents.uploadFile(exec: (buffer: MultiFileMemoryBuffer, upload: Upload) -> Unit) {
    val buffer = MultiFileMemoryBuffer()
    val upload = Upload(buffer)
    upload.setAcceptedFileTypes("image/jpeg", "image/png", "application/pdf", "text/plain")
    val uploadButton = Button("Arquivo Pedido")
    upload.uploadButton = uploadButton
    upload.isAutoUpload = true
    upload.maxFileSize = 1024 * 1024 * 1024
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
