package br.com.astrosoft.devolucao.viewmodel.compra

import br.com.astrosoft.devolucao.model.beans.PedidoCompra
import br.com.astrosoft.devolucao.model.beans.PedidoCompraFornecedor
import br.com.astrosoft.devolucao.model.beans.PedidoCompraProduto
import br.com.astrosoft.devolucao.model.beans.PedidoExcel
import br.com.astrosoft.devolucao.model.pdftxt.FileText
import br.com.astrosoft.framework.util.trimNull
import br.com.astrosoft.framework.util.unaccent
import br.com.astrosoft.framework.viewmodel.fail
import io.github.rushuat.ocell.document.DocumentOOXML
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.DataRow
import org.jetbrains.kotlinx.dataframe.api.columnNames
import org.jetbrains.kotlinx.dataframe.api.map
import org.jetbrains.kotlinx.dataframe.io.readExcel
import java.io.ByteArrayInputStream

abstract class TabAbstractConfirViewModel(val viewModel: CompraViewModel) : ITabCompraConfViewModel {
  private val listPedidoExcel = mutableListOf<PedidoExcel>()
  private val fileText = FileText()

  abstract fun updateComponent()

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

  final override fun fileText(): FileText {
    return fileText
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

  final override fun usaEmbalagemProdutoSelecionado(itens: Set<PedidoCompraProduto>) = viewModel.exec {
    if (itens.isEmpty()) {
      fail("Newnhum item selecionado")
    }
    itens.forEach { item ->
      item.usaEmbalagem()
    }
  }

  final override fun ajustaSaldoEmbalagem(itens: Set<PedidoCompraProduto>) = viewModel.exec {
    if (itens.isEmpty()) {
      fail("Newnhum item selecionado")
    }
    itens.forEach { item ->
      item.calculeDifs()
    }
  }

  final override fun desconfirmaProdutoSelecionado(itens: Set<PedidoCompraProduto>) = viewModel.exec {
    if (itens.isEmpty()) {
      fail("Newnhum item selecionado")
    }
    itens.forEach { item ->
      item.desmarcaConferido()
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
          val item = row.getValue("ITEM")
          val referencia = row.getValue("REFERÊNCIA") ?: row.getValue("REF")
          val descricao = row.getValue("DESCRIÇÃO") ?: row.getValue("PRODUTO")
          val quantidade = row.getValue("QUANTIDADE") ?: row.getValue("QTD")
          val valorUnitario = row.getValue("VALOR UNITÁRIO") ?: row.getValue("V. Unit")
          val valorTotal = row.getValue("VALOR TOTAL") ?: row.getValue("V. total")
          PedidoExcel(
            item = item.toStr(),
            referencia = referencia.toStr(),
            descricao = descricao.toStr(),
            quantidade = quantidade.toInt(),
            valorUnitario = valorUnitario.toDouble(),
            valorTotal = valorTotal.toDouble(),
                     )
        }
        listPedidoExcel.clear()
        listPedidoExcel.addAll(list)
        listPedidoExcel.forEachIndexed { index, pedidoExcel ->
          pedidoExcel.linha = index + 1
        }
      } catch (e: Throwable) {
        listPedidoExcel.clear()
        e.printStackTrace()
      }
    }
    updateComponent()
  }

  fun DataRow<Any?>.getValue(colname: String): Any? {
    val col = this.columnNames().firstOrNull { name ->
      name.unaccent().trimNull() == colname.unaccent().trimNull()
    } ?: return null
    return get(col)
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
    val listCodigo = produto.listCodigo()
    val codLinha = listCodigo.firstNotNullOfOrNull { cod ->
      fileText.findLine(cod).map { linha ->
        Pair(cod, linha)
      }.minByOrNull { p ->
        p.second.lineStr.indexOf(p.first)
      }
    }
    produto.linePDF = codLinha?.second
    produto.codigoMatch = codLinha?.first
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