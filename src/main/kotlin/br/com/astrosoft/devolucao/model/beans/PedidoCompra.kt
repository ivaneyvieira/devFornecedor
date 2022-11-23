package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.util.format
import java.time.LocalDate



class PedidoCompra(
  val vendno: Int,
  val fornecedor: String,
  val cnpj: String,
  val loja: Int,
  val sigla: String,
  val numeroPedido: Int,
  val status: Int,
  val dataPedido: LocalDate,
  val dataEntrega: LocalDate,
  val obsercacaoPedido: String,
  val vlPedido: Double,
  val vlCancelado: Double,
  val vlRecebido: Double,
  val vlPendente: Double,
  val produtos: List<PedidoCompraProduto>,
                  ) {
  fun savePdf(bytes: ByteArray) {
    val nfFile =
      NFFile(storeno = loja,
             pdvno = PDV_COMPRA,
             xano = numeroPedido,
             date = DATA_COMPRA,
             nome = "Pedido${numeroPedido}.pdf",
             file = bytes)
    nfFile.insert()
  }

  fun toPdf() : ByteArray? {
    return saci.selectFile(this).firstOrNull()?.file
  }

  fun removePdf() {
    val nfFile =
      NFFile(storeno = loja,
             pdvno = PDV_COMPRA,
             xano = numeroPedido,
             date = DATA_COMPRA,
             nome = "Pedido${numeroPedido}.pdf",
             file = ByteArray(0))
    saci.deleteFile(nfFile)
  }

  val labelTitle
    get() = "FORNECEDOR: ${this.vendno} ${this.fornecedor}                  LOJA: $sigla PEDIDO: $numeroPedido"

  val labelGroup
    get() = "FORNECEDOR: ${this.vendno} ${this.fornecedor}"

  val dataPedidoStr
    get() = dataPedido.format()

  companion object {
    val PDV_COMPRA = 9990
    val DATA_COMPRA = LocalDate.of(2022,1,1)

    fun findAll(filtro: FiltroPedidoCompra): List<PedidoCompra> {
      val list = PedidoCompraProduto.findAll(filtro)
      return group(list)
    }

    fun group(list: List<PedidoCompraProduto>): List<PedidoCompra> {
      return list.groupBy {
        ChavePedidoCompra(loja = it.loja, numeroPedido = it.numeroPedido)
      }.mapNotNull { entry ->
        val produtos = entry.value
        val bean = produtos.firstOrNull() ?: return@mapNotNull null
        PedidoCompra(
          vendno = bean.vendno,
          fornecedor = bean.fornecedor,
          cnpj = bean.cnpj,
          loja = bean.loja,
          sigla = bean.sigla,
          numeroPedido = bean.numeroPedido,
          status = bean.status,
          dataPedido = bean.dataPedido,
          dataEntrega = bean.dataEntrega,
          obsercacaoPedido = bean.obsercacaoPedido,
          vlPedido = produtos.sumOf { it.vlPedido ?: 0.00 },
          vlCancelado = produtos.sumOf { it.vlCancelado ?: 0.00},
          vlRecebido = produtos.sumOf { it.vlRecebido ?: 0.00},
          vlPendente = produtos.sumOf { it.vlPendente ?: 0.00},
          produtos = produtos,
                    )
      }
    }
  }
}

data class ChavePedidoCompra(val loja: Int, val numeroPedido: Int)