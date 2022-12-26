package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.pdftxt.Line
import br.com.astrosoft.devolucao.model.pdftxt.LinePosition
import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.util.format
import io.github.rushuat.ocell.annotation.FieldExclude
import io.github.rushuat.ocell.annotation.FieldName
import java.time.LocalDate

class PedidoCompraProduto(
  @FieldName("Item")
  var item: String = "",
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
  @FieldName("Código")
  val codigo: String?,
  @FieldExclude
  val seqno: Int?,
  @FieldName("Descrição")
  val descricao: String?,
  @FieldName("Grade")
  val grade: String?,
  @FieldName("Ref Fab")
  val refFab: String?,
  @FieldExclude
  var linha: Int = 0,
  @FieldName("Ref NF")
  val refno: String?,
  @FieldExclude
  val refname: String?,
  @FieldName("Dif Ref")
  var difRef: String?,
  @FieldName("Und")
  val unidade: String?,
  @FieldName("Qtd Ped")
  val qtPedida: Int?,
  @FieldExclude
  val qtCancelada: Int?,
  @FieldExclude
  val qtRecebida: Int?,
  @FieldExclude
  val qtPendente: Int?,
  @FieldName("Qtd Cot")
  var quantidadeCt: Int? = null,
  @FieldName("Dif Qtd")
  var quantidadeDif: Int? = null,
  @FieldName("V. Unt Ped")
  val custoUnit: Double?,
  @FieldName("V. Unt Cot")
  var valorUnitarioCt: Double? = null,
  @FieldName("Dif Valor")
  var valorUnitarioDif: Double? = null,
  @FieldExclude
  val barcode: String?,
  @FieldExclude
  var confirmado: String?,
  @FieldName("Valor Total")
  val valorTotal: Double,
                         ) {
  fun findQuant(): LinePosition? {
    val line = linePDF ?: return null
    val pos = line.findIndex(qtPedida)
    return pos
  }

  @FieldExclude
  private var _pedidoExcel: PedidoExcel? = null

  @FieldExclude
  private var _linePDF: Line? = null

  var pedidoExcel: PedidoExcel?
    get() = _pedidoExcel
    set(value) {
      _pedidoExcel = value
      quantidadeCt = value?.quantidade
      valorUnitarioCt = value?.valorUnitario
      linha = pedidoExcel?.linha ?: 0
      item = pedidoExcel?.item ?: ""
      val referencia = value?.referencia
      val refPed = "$refno/$refFab"

      difRef = if (refPed.contains("$referencia")) "Não" else "Sim"
      quantidadeDif = (qtPedida ?: 0) - (quantidadeCt ?: 0)
      valorUnitarioDif = (custoUnit ?: 0.00) - (valorUnitarioCt ?: 0.00)
    }

  var linePDF: Line?
    get() = _linePDF
    set(value) {
      _linePDF = value
      linha = value?.num ?: 0
      item = value?.item() ?: ""
    }

  fun marcaConferido() {
    confirmado = "S"
    saci.updateConferido(this)
  }

  fun listCodigo(): List<String> {
    val listRef = refno?.split("/").orEmpty()
    val refFab = refFab
    val list = listRef + refFab
    return list.filterNotNull().filter { it.isNotBlank() }
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