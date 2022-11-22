package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.util.format
import java.time.LocalDate

class PedidoCompraProduto(
  val origem: String,
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
  val codigo: String?,
  val descricao: String?,
  val refFab: String?,
  val refno: String?,
  val refname: String?,
  val grade: String?,
  val unidade: String?,
  val qtPedida: Int?,
  val qtCancelada: Int?,
  val qtRecebida: Int?,
  val qtPendente: Int?,
  val custoUnit: Double?,
  val barcode: String?,
                         ) {
  var item: Int = 0
  val dataPedidoStr
    get() = dataPedido.format()
  val vlPedido
    get() = if(qtPedida == null || custoUnit == null) null else qtPedida * custoUnit
  val vlCancelado
    get() = if(qtCancelada == null || custoUnit == null) null else qtCancelada * custoUnit
  val vlRecebido
    get() = if(qtRecebida == null || custoUnit == null) null else qtRecebida * custoUnit
  val vlPendente
    get() = if(qtPendente == null || custoUnit == null) null else qtPendente * custoUnit

  companion object {
    fun findAll(filtro: FiltroPedidoCompra): List<PedidoCompraProduto> {
      val list = saci.findPedidosCompraProduto(filtro)
      return list
    }
  }
}

data class FiltroPedidoCompra(val loja: Int, val pesquisa: String)