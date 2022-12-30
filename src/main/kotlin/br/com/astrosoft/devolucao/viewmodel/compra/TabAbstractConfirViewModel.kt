package br.com.astrosoft.devolucao.viewmodel.compra

import br.com.astrosoft.devolucao.model.beans.PedidoCompra
import br.com.astrosoft.devolucao.model.beans.PedidoCompraFornecedor
import br.com.astrosoft.devolucao.model.beans.PedidoCompraProduto
import br.com.astrosoft.devolucao.model.beans.PedidoExcel
import br.com.astrosoft.devolucao.model.pdftxt.FileText
import br.com.astrosoft.framework.viewmodel.fail
import io.github.rushuat.ocell.document.DocumentOOXML
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.map
import org.jetbrains.kotlinx.dataframe.io.readExcel
import java.io.ByteArrayInputStream

open class TabAbstractConfirViewModel(val viewModel: CompraViewModel) : ITabCompraConfViewModel {
  private val listPedidoExcel = mutableListOf<PedidoExcel>()
  private val fileText = FileText()

  val subView
    get() = viewModel.view.tabConfirmadoViewModel

  final override fun saveExcelPedido(pedido: PedidoCompra, bytes: ByteArray) {
    pedido.saveExcel(bytes)
    setFileExcel(bytes)
  }

  final override fun removeExcelPedido(pedido: PedidoCompra) {
    pedido.removeExcel()
    setFileExcel(null)
  }

  final override fun savePDFPedido(pedido: PedidoCompra, bytes: ByteArray) {
    pedido.savePDF(bytes)
    setFilePDF(bytes)
  }

  final override fun removePDFPedido(pedido: PedidoCompra) {
    pedido.removeExcel()
    setFilePDF(null)
  }

  final override fun confirmaProdutoSelecionado(itens: Set<PedidoCompraProduto>) = viewModel.exec {
    if (itens.isEmpty()) {
      fail("Newnhum item selecionado")
    }
    itens.forEach { item ->
      item.marcaConferido()
    }
  }

  final override fun pedidoOK(): EFileType {
    return when {
      listPedidoExcel.isNotEmpty() -> EFileType.XLSX
      fileText.isNotEmpty()        -> EFileType.PDF
      else                         -> EFileType.NONE
    }
  }

  final override fun setFileExcel(fileText: ByteArray?) {
    if (fileText == null) {
      listPedidoExcel.clear()
    }
    else {
      try {
        val df = DataFrame.readExcel(ByteArrayInputStream(fileText))
        val list = df.map { row ->
          PedidoExcel(
            item = row[0].toStr(),
            referencia = row[1].toStr(),
            descricao = row[2].toStr(),
            quantidade = row[3].toInt(),
            valorUnitario = row[4].toDouble(),
            valorTotal = row[5].toDouble(),
                     )
        }
        listPedidoExcel.clear()
        listPedidoExcel.addAll(list)
        listPedidoExcel.forEachIndexed { index, pedidoExcel ->
          pedidoExcel.linha = index + 1
        }
      }catch (e: Throwable){
        listPedidoExcel.clear()
        e.printStackTrace()
      }
    }
    updateComponent()
  }

  final override fun setFilePDF(bytes: ByteArray?) {
    if (bytes == null) {
      fileText.clear()
    }
    else {
      fileText.loadPDF(bytes)
    }
    updateComponent()
  }

  final override fun findPedidoExcel(produto: PedidoCompraProduto) {
    val listStr = produto.listCodigo()
    val listNum = listStr.map { it.toIntOrNull().toString() }
    val list = (listNum + listStr).distinct()
    val pedidoExcel = listPedidoExcel.firstOrNull { list.distinct().contains(it.referencia) }
    produto.pedidoExcel = pedidoExcel
  }

  final override fun findPedidoPDF(produto: PedidoCompraProduto) {
    val listStr = produto.listCodigo()
    val linha = listStr.flatMap { cod ->
      fileText.findLine(cod)
    }.firstOrNull()
    produto.linePDF = linha
  }

  final override fun imprimirPedidoCompra(pedidos: List<PedidoCompra>) = viewModel.exec {
    pedidos.ifEmpty {
      fail("Nenhuma item foi selecionado")
    }
    subView.imprimeSelecionados(pedidos)
  }

  final override fun excelPedidoCompra(pedidos: List<PedidoCompra>): ByteArray {
    return if (pedidos.isEmpty()) {
      viewModel.showError("Nenhuma item foi selecionado")
      ByteArray(0)
    }
    else {
      DocumentOOXML().use { document ->
        document.addSheet(pedidos.flatMap { it.produtos }.sortedWith(compareBy({ it.vendno }, { it.loja }, {
          it.dataPedido
        }, { it.codigo })))
        document.toBytes()
      }
    }
  }

  final override fun imprimirRelatorioFornecedor(pedido: List<PedidoCompra>) = viewModel.exec {
    pedido.ifEmpty {
      fail("Nenhuma item foi selecionado")
    }
    subView.imprimirRelatorioFornecedor(pedido)
  }

  private fun Any?.toStr(): String? {
    this ?: return null
    return when (this) {
      is String -> this
      is Int    -> this.toString()
      is Long   -> this.toString()
      is Double -> this.toLong().toString()
      else      -> this.toString()
    }
  }

  private fun Any?.toInt(): Int? {
    this ?: return null
    return when (this) {
      is String -> this.toIntOrNull()
      is Int    -> this.toInt()
      is Long   -> this.toInt()
      is Double -> this.toInt()
      else      -> this.toString().toIntOrNull()
    }
  }

  private fun Any?.toDouble(): Double? {
    this ?: return null
    return when (this) {
      is String -> this.toDoubleOrNull()
      is Int    -> this.toDouble()
      is Long   -> this.toDouble()
      is Double -> this
      else      -> this.toString().toDoubleOrNull()
    }
  }

  fun updateComponent() = viewModel.exec {
    val list = pedidoCompraFornecedors()
    subView.updateGrid(list)
  }

  private fun pedidoCompraFornecedors(): List<PedidoCompraFornecedor> {
    val filtro = subView.filtro()
    val list = PedidoCompraFornecedor.findAll(filtro).filter {
      (!it.fornecedor.startsWith("ENGECOPI"))
    }
    return list
  }

  fun imprimirRelatorioResumido(fornecedores: List<PedidoCompraFornecedor>) = viewModel.exec {
    fornecedores.ifEmpty {
      fail("Nenhuma fornecedor foi selecionada")
    }
    subView.imprimirRelatorioResumido(fornecedores)
  }

  fun excelRelatorioFornecedor(pedidos: List<PedidoCompra>): ByteArray {
    return if (pedidos.isEmpty()) {
      viewModel.showError("Nenhuma item foi selecionado")
      ByteArray(0)
    }
    else DocumentOOXML().use { document ->
      document.addSheet(pedidos.sortedWith(compareBy({ it.vendno }, { it.loja }, { it.dataPedido })))
      document.toBytes()
    }
  }
}