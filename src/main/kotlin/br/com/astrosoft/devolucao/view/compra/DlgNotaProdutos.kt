package br.com.astrosoft.devolucao.view.compra

import br.com.astrosoft.devolucao.model.beans.PedidoCompra
import br.com.astrosoft.devolucao.model.beans.PedidoCompraProduto
import br.com.astrosoft.devolucao.model.pdftxt.Line
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colBarcode
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colCodigo
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colCustoCt
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colCustoDif
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colCustoEmb
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colCustoPed
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colDescNota
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colDescricao
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colGrade
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colItem
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colQtEmbalagem
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colQtdeCt
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colQtdeDif
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colQtdeEmb
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colQtdePed
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colRefDif
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colRefFabrica
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colRefNota
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colUnidade
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colVlTotal
import br.com.astrosoft.devolucao.viewmodel.compra.EFileType.PDF
import br.com.astrosoft.devolucao.viewmodel.compra.EFileType.XLSX
import br.com.astrosoft.devolucao.viewmodel.compra.ETipoPainel
import br.com.astrosoft.devolucao.viewmodel.compra.ITabCompraConfViewModel
import br.com.astrosoft.devolucao.viewmodel.compra.ITabCompraViewModel
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.util.lpad
import br.com.astrosoft.framework.view.*
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.text
import com.github.mvysny.kaributools.fetchAll
import com.github.mvysny.kaributools.refresh
import com.github.mvysny.kaributools.setSortOrder
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.Column
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.upload.FileRejectedEvent
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MemoryBuffer
import net.sf.jasperreports.web.util.FontWebResourceHandler

class DlgNotaProdutos(val viewModel: ITabCompraViewModel) {
  private lateinit var gridNota: Grid<PedidoCompraProduto>
  private var btnExcel: Button? = null
  private var btnPdf: Button? = null

  fun showDialogNota(pedido: PedidoCompra?) {
    pedido ?: return
    if (viewModel is ITabCompraConfViewModel) {
      viewModel.setFileExcel(null)
      pedido.produtos.sortedBy { it.codigo }.forEach { produto ->
        produto.item = ""
        produto.linha = 0
        produto.pedidoExcel = null
      }
      val byteExcel = pedido.toExcel()
      if (byteExcel == null) {
        pedido.toPDF()?.let { bytes ->
          viewModel.setFilePDF(bytes)
          pedido.fileText = viewModel.fileText()
          pedido.produtos.forEach { produto ->
            viewModel.findPedidoPDF(produto)
          }
          pedido.processaQuantPDF()
          pedido.produtos.sortedBy { it.linha }.forEachIndexed { index, pedidoCompraProduto ->
            val codigos = pedidoCompraProduto.listCodigo()
            val item = pedidoCompraProduto.item
            if (item in codigos) {
              pedidoCompraProduto.item = (index + 1).toString().lpad(5, "0")
            }
          }
        }
      }
      else {
        byteExcel.let { bytes ->
          viewModel.setFileExcel(bytes)
          pedido.produtos.forEach { produto ->
            viewModel.findPedidoExcel(produto)
          }
          pedido.produtos.sortedBy { it.linha }.forEachIndexed { index, pedidoCompraProduto ->
            val codigos = pedidoCompraProduto.listCodigo()
            val item = pedidoCompraProduto.item
            if (item in codigos) {
              pedidoCompraProduto.item = (index + 1).toString().lpad(5, "0")
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
        if (viewModel.tipoPainel() == ETipoPainel.Conferir) {
          this.uploadArquivo { buffer, upload ->
            upload.isDropAllowed = false
            upload.addSucceededListener {
              val bytes = buffer.inputStream.readBytes()
              if (buffer.fileName.endsWith(".pdf", ignoreCase = true)) { //PDF
                viewModel.savePDFPedido(pedido, bytes)
                pedido.produtos.forEach {
                  viewModel.findPedidoPDF(it)
                }
                pedido.processaQuantPDF()
              }
              else if (buffer.fileName.endsWith(".xlsx", ignoreCase = true)) {
                viewModel.saveExcelPedido(pedido, bytes)
                pedido.produtos.forEach {
                  viewModel.findPedidoExcel(it)
                }
              }
              gridNota.dataProvider.refreshAll()
              buttonPedido(pedido)
            }
          }
        }
        if (viewModel.tipoPainel() == ETipoPainel.Conferir) {
          this.button("Remover Pedido") {
            icon = VaadinIcon.TRASH.create()
            onLeftClick {
              viewModel.removeExcelPedido(pedido)
              viewModel.removePDFPedido(pedido)
              pedido.produtos.forEach {
                it.pedidoExcel = null
              }
              gridNota.dataProvider.refreshAll()
              buttonPedido(pedido)
            }
          }
        }
        btnPdf = this.button("Pedido PDF") {
          icon = FontAwesome.Solid.FILE_PDF.create()
          onLeftClick {
            val bytes = pedido.toPDF()
            if (bytes != null) {
              val filename = pedido.filename()
              SubWindowPDF(filename, bytes, false).open()
            }
          }
        }
        btnExcel = lazyDownloadButton(
          text = "Pedido Excel",
          icon = FontAwesome.Solid.FILE_EXCEL.create(),
          fileName = {
            "${pedido.filename()}.xlsx"
          },
          byteArray={
            pedido.toExcel() ?: ByteArray(0)
          }
                                     )
        if (viewModel.tipoPainel() == ETipoPainel.Conferir) {
          this.button("Confirma Pedido") {
            icon = VaadinIcon.CHECK.create()
            onLeftClick {
              val itens = gridNota.selectedItems
              viewModel.confirmaProdutoSelecionado(itens)
              gridNota.refresh()
            }
          }
        }
        else {
          this.button("Voltar") {
            icon = VaadinIcon.CHECK.create()
            onLeftClick {
              val itens = gridNota.selectedItems
              viewModel.desconfirmaProdutoSelecionado(itens)
              gridNota.refresh()
            }
          }
        }
        if (viewModel.tipoPainel() == ETipoPainel.Conferir) {
          this.button("Usa Embalagem") {
            this.isVisible = true
            icon = VaadinIcon.CHECK.create()
            onLeftClick {
              val itens = gridNota.selectedItems
              viewModel.usaEmbalagemProdutoSelecionado(itens)
              viewModel.ajustaSaldoEmbalagem(itens)
              gridNota.refresh()
            }
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
    buttonPedido(pedido)
    form.open()
  }

  fun buttonPedido(pedido: PedidoCompra?) {
    btnPdf?.isVisible = pedido?.toPDF() != null
    btnExcel?.isVisible = pedido?.toExcel() != null
  }

  private fun PedidoCompra.filename() : String {
    val forn = this.vendno
    val loja = this.sigla
    val numeroPedido = this.numeroPedido
    return "FORN $forn PEDIDO $loja $numeroPedido.xlsx"
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
      colGrade() //colDescNota()
      colRefFabrica().marcaRefFabrica()
      colRefNota().marcaRefNota()
      colRefDif()
      colUnidade()
      colQtEmbalagem()
      colQtdePed().marcaQuant(isEmb = false)
      colQtdeEmb().marcaQuant(isEmb = true)
      colQtdeCt()
      colQtdeDif()
      colCustoPed().marcaValor(isEmb = false)
      colCustoEmb().marcaValor(isEmb = true)
      colCustoCt()
      colCustoDif()
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

  private fun Column<PedidoCompraProduto>.marcaRefFabrica() {
    this.setClassNameGenerator { produto ->
      if (viewModel is ITabCompraConfViewModel) {
        when (viewModel.pedidoOK()) {
          XLSX -> {
            val pedidoExcel = produto.pedidoExcel ?: return@setClassNameGenerator "marcaError"
            val ref1 = pedidoExcel.referencia?.toIntOrNull()?.toString() ?: pedidoExcel.referencia
            val ref2 = produto.refFab?.toIntOrNull()?.toString() ?: produto.refFab
            if (ref1 == ref2) "marcaOk"
            else "marcaError"
          }

          PDF  -> {
            val line = produto.linePDF ?: return@setClassNameGenerator "marcaError"
            val ref2 = produto.refFab?.toIntOrNull()?.toString() ?: produto.refFab
            if (line.findRef(ref2)) "marcaOk"
            else "marcaError"
          }

          else -> ""
        }
      }
      else ""
    }
  }

  private fun Column<PedidoCompraProduto>.marcaRefNota() {
    this.setClassNameGenerator { produto ->
      if (viewModel is ITabCompraConfViewModel) {
        when (viewModel.pedidoOK()) {
          XLSX -> {
            val pedidoExcel = produto.pedidoExcel ?: return@setClassNameGenerator "marcaError"
            val ref1 = pedidoExcel.referencia?.toIntOrNull()?.toString() ?: pedidoExcel.referencia
            val listRef = produto.refno?.split("/").orEmpty().map { ref ->
              ref.toIntOrNull()?.toString() ?: ref
            }
            if (ref1 in listRef) "marcaOk"
            else "marcaError"
          }

          PDF  -> {
            val line = produto.linePDF ?: return@setClassNameGenerator "marcaError"
            val listRef = produto.refno?.split("/") ?: return@setClassNameGenerator ""
            if (listRef.any { line.findRef(it) }) "marcaOk"
            else "marcaError"
          }

          else -> ""
        }
      }
      else ""
    }
  }

  private fun Column<PedidoCompraProduto>.marcaQuant(isEmb: Boolean) {
    this.setClassNameGenerator { produto ->
      if (produto.calcEmbalagem == "S" == isEmb) {
        if (viewModel is ITabCompraConfViewModel) {
          when (viewModel.pedidoOK()) {
            XLSX -> {
              val pedidoExcel = produto.pedidoExcel ?: return@setClassNameGenerator "marcaError"
              if (pedidoExcel.quantidade?.toDouble().format() == produto.quantidadeCt?.toDouble().format()) "marcaOk"
              else "marcaError"
            }

            PDF  -> {
              if (produto.quantCalculada.format() == produto.quantidadeCt?.toDouble().format()) "marcaOk"
              else "marcaError"
            }

            else -> ""
          }
        }
        else ""
      }
      else ""
    }
  }

  private fun Column<PedidoCompraProduto>.marcaValor(isEmb: Boolean) {
    this.setClassNameGenerator { produto ->
      if (produto.calcEmbalagem == "S" == isEmb) {
        if (viewModel is ITabCompraConfViewModel) {
          when (viewModel.pedidoOK()) {
            XLSX -> {
              val pedidoExcel = produto.pedidoExcel ?: return@setClassNameGenerator "marcaError"
              if (pedidoExcel.valorUnitario?.format() == produto.custoUnit.format()) "marcaOk"
              else "marcaError"
            }

            PDF  -> {
              if (produto.valorCalculado.format() == produto.valorUnitarioCt.format()) "marcaOk"
              else "marcaError"
            }

            else -> ""
          }
        }
        else ""
      }
      else ""
    }
  }

  private fun HasComponents.uploadArquivo(exec: (buffer: MemoryBuffer, upload: Upload) -> Unit) {
    val buffer = MemoryBuffer()
    val upload = Upload(buffer)
    upload.setAcceptedFileTypes("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                                ".xlsx",
                                "application/pdf",
                                ".pdf")
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
