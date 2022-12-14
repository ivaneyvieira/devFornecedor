package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.util.format
import io.github.rushuat.ocell.annotation.FieldExclude
import java.time.LocalDate

class PedidoCompraProduto(
  @FieldExclude
  val origem: String,
  @FieldExclude
  val vendno: Int,
  @FieldExclude
  val fornecedor: String,
  @FieldExclude
  val cnpj: String,
  @FieldExclude
  val loja: Int,
  @FieldExclude
  val sigla: String,
  @FieldExclude
  val numeroPedido: Int,
  @FieldExclude
  val status: Int,
  @FieldExclude
  val dataPedido: LocalDate,
  @FieldExclude
  val dataEntrega: LocalDate,
  @FieldExclude
  val obsercacaoPedido: String,
  val codigo: String?,
  @FieldExclude
  val seqno: Int?,
  val descricao: String?,
  val refFab: String?,
  var item: Int = 0,
  val refno: String?,
  @FieldExclude
  val refname: String?,
  val grade: String?,
  val unidade: String?,
  val qtPedida: Int?,
  @FieldExclude
  val qtCancelada: Int?,
  @FieldExclude
  val qtRecebida: Int?,
  @FieldExclude
  val qtPendente: Int?,
  val custoUnit: Double?,
  @FieldExclude
  val barcode: String?,
  @FieldExclude
  var confirmado: String?,
  val valorTotal: Double,
                         ) {
  fun marcaConferido() {
    confirmado = "S"
    saci.updateConferido(this)
  }

  fun desmarcaConferido() {
    confirmado = "N"
    saci.updateConferido(this)
  }

  val dataPedidoStr
    get() = dataPedido.format()
  val vlPedido
    get() = if (qtPedida == null || custoUnit == null) null else qtPedida * custoUnit
  val vlCancelado
    get() = if (qtCancelada == null || custoUnit == null) null else qtCancelada * custoUnit
  val vlRecebido
    get() = if (qtRecebida == null || custoUnit == null) null else qtRecebida * custoUnit
  val vlPendente
    get() = if (qtPendente == null || custoUnit == null) null else qtPendente * custoUnit

  companion object {
    fun findAll(filtro: FiltroPedidoCompra): List<PedidoCompraProduto> {
      val list = saci.findPedidosCompraProduto(filtro)
      return list
    }
  }
}

data class FiltroPedidoCompra(
  val loja: Int,
  val pesquisa: String,
  val onlyPendente: Boolean,
  val onlyConfirmado: Boolean,
                             )