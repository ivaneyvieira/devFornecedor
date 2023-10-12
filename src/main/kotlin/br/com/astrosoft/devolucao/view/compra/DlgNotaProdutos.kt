package br.com.astrosoft.devolucao.view.compra

import br.com.astrosoft.devolucao.model.beans.PedidoCompra
import br.com.astrosoft.devolucao.model.beans.PedidoCompraProduto
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colBarcode
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colCodigo
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colCustoCt
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colCustoDif
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colCustoEmb
import br.com.astrosoft.devolucao.view.compra.columns.PedidoCompraProdutoColumns.colCustoPed
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
import br.com.astrosoft.framework.view.*
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.kaributools.fetchAll
import com.github.mvysny.kaributools.refresh
import com.github.mvysny.kaributools.setSortOrder
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.Column
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.upload.FileRejectedEvent
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MemoryBuffer

class DlgNotaProdutos(val viewModel: ITabCompraViewModel, val pedido: PedidoCompra?) {
  private var form: SubWindowForm? = null
  private lateinit var gridNota: Grid<PedidoCompraProduto>
  private var btnExcel: Button? = null
  private var btnPdf: Button? = null

  fun showDialogNota() {
    pedido ?: return
    if (viewModel is ITabCompraConfViewModel) {
      viewModel.setFileExcel(null)
      pedido.produtos.sortedBy { it.codigo }.forEach { produto ->
        produto.linha = 0
        produto.pedidoExcel = null
      }
      val byteExcel = pedido.toExcel()
      if (byteExcel == null) {
        pedido.toPDF()?.let { bytes ->
          viewModel.setFilePDF(bytes)
          pedido.fileText = viewModel.fileText()
          pedido.produtos.forEach { produto ->
            viewModel.setPedidoPDF(produto)
          }
          pedido.processaQuantPDF()
          pedido.produtos.sortedBy { it.linha }.forEachIndexed { index, pedidoCompraProduto ->
            pedidoCompraProduto.seqItem = index + 1
          }
        }
      } else {
        byteExcel.let { bytes ->
          viewModel.setFileExcel(bytes)
          pedido.produtos.forEach { produto ->
            viewModel.setPedidoExcel(produto)
          }
          pedido.produtos.sortedBy { it.linha }.forEachIndexed { index, pedidoCompraProduto ->
            pedidoCompraProduto.seqItem = index + 1
          }
        }
      }
    }

    val produtos = pedido.produtos
    form = SubWindowForm(pedido.labelTitle, toolBar = {
      button("PDF") {
        icon = VaadinIcon.PRINT.create()
        onLeftClick {
          val produtosList = gridNota.asMultiSelect().selectedItems.toList()
          viewModel.imprimirPedidoCompra(PedidoCompra.group(produtosList.toList()))
        }
      }
      this.lazyDownloadButtonXlsx("Planilha", "produtosCompra") {
        val produtosList = gridNota.asMultiSelect().selectedItems.toList()
        viewModel.excelPedidoCompra(PedidoCompra.group(produtosList.toList()))
      }

      if (viewModel is ITabCompraConfViewModel) {
        if (viewModel.tipoPainel() == ETipoPainel.Conferir) {
          this.uploadArquivo { buffer, upload ->
            upload.isDropAllowed = false
            upload.addSucceededListener {
              val bytes = buffer.inputStream.readBytes()
              if (buffer.fileName.endsWith(".pdf", ignoreCase = true)) { //PDF
                viewModel.savePDFPedido(pedido, bytes)
              } else if (buffer.fileName.endsWith(".xlsx", ignoreCase = true)) {
                viewModel.saveExcelPedido(pedido, bytes)
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
              updateComponent()
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
        btnExcel =
            lazyDownloadButton(text = "Pedido Excel", icon = FontAwesome.Solid.FILE_EXCEL.create(), fileName = {
              "${pedido.filename()}.xlsx"
            }, byteArray = {
              pedido.toExcel() ?: ByteArray(0)
            })
        if (viewModel.tipoPainel() == ETipoPainel.Conferir) {
          this.button("Confirma Pedido") {
            icon = VaadinIcon.CHECK.create()
            onLeftClick {
              val itens = gridNota.selectedItems
              viewModel.confirmaProdutoSelecionado(itens)
              updateComponent()
              gridNota.refresh()
            }
          }
        } else {
          this.button("Voltar") {
            icon = VaadinIcon.CHECK.create()
            onLeftClick {
              val itens = gridNota.selectedItems
              viewModel.desconfirmaProdutoSelecionado(itens)
              updateComponent()
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
    form?.open()
  }

  fun buttonPedido(pedido: PedidoCompra?) {
    btnPdf?.isVisible = pedido?.toPDF() != null
    btnExcel?.isVisible = pedido?.toExcel() != null
  }

  private fun PedidoCompra.filename(): String {
    val forn = this.vendno
    val loja = this.sigla
    val numeroPedido = this.numeroPedido
    return "FORN $forn PEDIDO $loja $numeroPedido.xlsx"
  }

  private fun createGrid(listParcelas: List<PedidoCompraProduto>): Grid<PedidoCompraProduto> {
    return Grid(PedidoCompraProduto::class.java, false).apply<Grid<PedidoCompraProduto>> {
      setSizeFull()
      addThemeVariants()
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)
      setItems(listParcelas)
      colItem()
      colCodigo().marcaCodigo()
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

  private fun String.listNum(): List<String> {
    return listOf(this.toIntOrNull()?.toString(), this).distinct().filterNotNull()
  }

  private fun Column<PedidoCompraProduto>.marcaCodigo() {
    this.setClassNameGenerator { produto ->
      if (viewModel is ITabCompraConfViewModel) {
        when (viewModel.pedidoOK()) {
          XLSX -> {
            val ref1 = produto.codigoMatch ?: return@setClassNameGenerator ""
            val ref2 = produto.codigo?.listNum().orEmpty()
            if (ref1 in ref2) "marcaOk"
            else ""
          }

          PDF  -> {
            val line = produto.linePDF ?: return@setClassNameGenerator ""
            val ref2 = produto.codigo
            if (line.findRef(ref2)) "marcaOk"
            else ""
          }

          else -> ""
        }
      } else ""
    }
  }

  private fun Column<PedidoCompraProduto>.marcaRefFabrica() {
    this.setClassNameGenerator { produto ->
      if (viewModel is ITabCompraConfViewModel) {
        when (viewModel.pedidoOK()) {
          XLSX -> {
            val ref1 = produto.codigoMatch ?: return@setClassNameGenerator "marcaError"
            val ref2 = produto.refFab?.split("\t").orEmpty().flatMap { ref ->
              ref.listNum()
            }
            if (ref1 in ref2) "marcaOk"
            else "marcaError"
          }

          PDF  -> {
            val ref1 = produto.codigoMatch ?: return@setClassNameGenerator "marcaError"
            val ref2 = produto.refFab?.split("\t") ?: return@setClassNameGenerator ""
            if (ref1 in ref2) "marcaOk"
            else "marcaError"
          }

          else -> ""
        }
      } else ""
    }
  }

  private fun Column<PedidoCompraProduto>.marcaRefNota() {
    this.setClassNameGenerator { produto ->
      if (viewModel is ITabCompraConfViewModel) {
        when (viewModel.pedidoOK()) {
          XLSX -> {
            val ref1 = produto.codigoMatch ?: return@setClassNameGenerator "marcaError"
            val listRef = produto.refno?.split("\t").orEmpty().flatMap { ref ->
              ref.listNum()
            }
            if (ref1 in listRef) "marcaOk"
            else "marcaError"
          }

          PDF  -> {
            val ref1 = produto.codigoMatch ?: return@setClassNameGenerator "marcaError"
            val listRef = produto.refno?.split("\t") ?: return@setClassNameGenerator ""
            if (ref1 in listRef) "marcaOk"
            else "marcaError"
          }

          else -> ""
        }
      } else ""
    }
  }

  private fun Column<PedidoCompraProduto>.marcaQuant(isEmb: Boolean) {
    this.setClassNameGenerator { produto ->
      if (produto.calcEmbalagem == "S" == isEmb) {
        if (viewModel is ITabCompraConfViewModel) {
          when (viewModel.pedidoOK()) {
            XLSX -> {
              val pedidoExcel = produto.pedidoExcel ?: return@setClassNameGenerator "marcaError"
              if (pedidoExcel.quantidade?.toDouble().format() == produto.quantidadeCt?.toDouble()
                  .format()
              ) "marcaOk"
              else "marcaError"
            }

            PDF  -> {
              if (produto.quantCalculada.format() == produto.quantidadeCt?.toDouble().format()) "marcaOk"
              else "marcaError"
            }

            else -> ""
          }
        } else ""
      } else ""
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
        } else ""
      } else ""
    }
  }

  private fun HasComponents.uploadArquivo(exec: (buffer: MemoryBuffer, upload: Upload) -> Unit) {
    val buffer = MemoryBuffer()
    val upload = Upload(buffer)
    upload.setAcceptedFileTypes(
      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
      ".xlsx",
      "application/pdf",
      ".pdf"
    )
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

  private fun updateComponent() {
    if (form?.isOpened == true) {
      viewModel.updateComponent()
      val produtos = viewModel.listPedidosFornecedor().firstOrNull { pfor ->
        pfor.vendno == pedido?.vendno
      }?.pedidos?.firstOrNull { ped ->
        ped.loja == pedido?.loja && ped.numeroPedido == pedido.numeroPedido
      }?.produtos.orEmpty()
      gridNota.setItems(produtos)
    }
  }
}
