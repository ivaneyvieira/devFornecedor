package br.com.astrosoft.devolucao.viewmodel.compra

import br.com.astrosoft.devolucao.model.beans.PedidoCompra
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

abstract class TabAbstractConfViewModel(val viewModel: CompraViewModel) : ITabCompraConfViewModel {
  private val listPedidoExcel = mutableListOf<PedidoExcel>()
  private val fileText = FileText()

  final override fun saveExcelPedido(pedido: PedidoCompra, bytes: ByteArray) {
    pedido.saveExcel(bytes)
    setFileExcel(bytes)
    pedido.produtos.forEach {
      setPedidoExcel(it)
    }
  }

  final override fun removeExcelPedido(pedido: PedidoCompra) {
    pedido.removeExcel()
    setFileExcel(null)
  }

  final override fun savePDFPedido(pedido: PedidoCompra, bytes: ByteArray) {
    pedido.savePDF(bytes)
    setFilePDF(bytes)
    pedido.fileText = fileText()
    pedido.produtos.forEach {
      setPedidoPDF(it)
    }
    pedido.processaQuantPDF()
  }

  final override fun fileText(): FileText {
    return fileText
  }

  final override fun removePDFPedido(pedido: PedidoCompra) {
    pedido.removePDF()
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
      fileText.isNotEmpty() -> EFileType.PDF
      else -> EFileType.NONE
    }
  }

  final override fun setFileExcel(fileText: ByteArray?) = viewModel.exec {
    if (fileText == null) {
      listPedidoExcel.clear()
    } else {
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
  }

  fun DataRow<Any?>.getValue(colname: String): Any? {
    val col = this.columnNames().firstOrNull { name ->
      name.unaccent().trimNull() == colname.unaccent().trimNull()
    } ?: return null
    return get(col)
  }

  final override fun setFilePDF(fileText: ByteArray?) {
    if (fileText == null) {
      this.fileText.clear()
    } else {
      this.fileText.loadPDF(fileText)
    }
  }

  final override fun setPedidoExcel(produto: PedidoCompraProduto) {
    val listRef = produto.listCodigo().flatMap { ref ->
      val refInt = ref.toBigIntegerOrNull()?.toString()
      listOf(ref, refInt).distinct().filterNotNull()
    }
    val pedidoExcel = listRef.firstNotNullOfOrNull { ref ->
      listPedidoExcel.firstOrNull { ped ->
        ref == ped.referencia
      }
    }
    produto.pedidoExcel = pedidoExcel
    produto.codigoMatch = pedidoExcel?.referencia
  }

  final override fun setPedidoPDF(produto: PedidoCompraProduto) {
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
    } else {
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
      is Int -> this.toString()
      is Long -> this.toString()
      is Double -> this.toLong().toString()
      else -> this.toString()
    }
  }

  private fun Any?.toInt(): Int? {
    this ?: return null
    return when (this) {
      is String -> this.toIntOrNull()
      is Int -> this.toInt()
      is Long -> this.toInt()
      is Double -> this.toInt()
      else -> this.toString().toIntOrNull()
    }
  }

  private fun Any?.toDouble(): Double? {
    this ?: return null
    return when (this) {
      is String -> this.toDoubleOrNull()
      is Int -> this.toDouble()
      is Long -> this.toDouble()
      is Double -> this
      else -> this.toString().toDoubleOrNull()
    }
  }

  fun excelRelatorioFornecedor(pedidos: List<PedidoCompra>): ByteArray {
    return if (pedidos.isEmpty()) {
      viewModel.showError("Nenhuma item foi selecionado")
      ByteArray(0)
    } else DocumentOOXML().use { document ->
      document.addSheet(pedidos.sortedWith(compareBy({ it.vendno }, { it.loja }, { it.dataPedido })))
      document.toBytes()
    }
  }
}