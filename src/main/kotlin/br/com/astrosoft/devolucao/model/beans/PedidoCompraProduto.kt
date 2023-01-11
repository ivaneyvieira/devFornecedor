package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.pdftxt.Line
import br.com.astrosoft.devolucao.model.pdftxt.LinePosition
import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.util.format
import io.github.rushuat.ocell.annotation.FieldExclude
import io.github.rushuat.ocell.annotation.FieldName
import java.time.LocalDate
import kotlin.math.roundToLong

class PedidoCompraProduto(
  @FieldName("Item")
  var seqItem: Int? = null,
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
  @FieldName("Qt Embalagem")
  val qtEmbalagem: Int?,
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
  var quantidadeDif: Double? = null,
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
  var calcEmbalagem: String,
                         ) {
  @FieldExclude
  var codigoMatch: String? = null

  val referenciaMatch: String?
    get() {
      val listRef = refno?.split("/").orEmpty()
      return listRef.firstOrNull { it == codigoMatch } ?: listRef.getOrNull(0)
    }
  val refFabMatch: String?
    get() {
      val listRef = refFab?.split("/").orEmpty()
      return listRef.firstOrNull { it == codigoMatch } ?: listRef.getOrNull(0)
    }

  val quantEmbalagem: Double?
    get() {
      val qtPed = qtPedida ?: return null
      val qtEmb = qtEmbalagem
      return if (qtEmb == null || qtEmb == 0) null
      else qtPed * 1.00 / qtEmb
    }

  val quantCalculada: Double?
    get() {
      val qtPed = qtPedida ?: return null
      return if (calcEmbalagem == "S") {
        quantEmbalagem
      }
      else qtPed * 1.00
    }

  val valorEmbalagem: Double?
    get() {
      val custo = custoUnit ?: return null
      val qtEmb = qtEmbalagem
      return if (qtEmb == null || qtEmb == 0) null
      else (custo * qtEmb).roundDouble()
    }

  val valorCalculado: Double?
    get() {
      val custo = custoUnit ?: return null
      val valor = if (calcEmbalagem == "S") {
        valorEmbalagem
      }
      else custo * 1.00

      return valor.roundDouble()
    }

  fun findQuant(): List<LinePosition> {
    val line = linePDF ?: return emptyList()
    val pos = line.findIndex(quantCalculada).toList()
    return pos
  }

  fun findValor(): List<LinePosition> {
    val line = linePDF ?: return emptyList()
    val pos = line.findIndex(valorCalculado)
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
      val referencia = value?.referencia
      val refPed = listCodigo()

      difRef = if (refPed.contains(referencia)) "Não" else "Sim"
      calculeDifs()
    }

  var linePDF: Line?
    get() = _linePDF
    set(value) {
      _linePDF = value
      linha = value?.num ?: 0
    }

  fun marcaConferido() {
    confirmado = "S"
    saci.updateConferido(this)
  }

  fun usaEmbalagem() {
    calcEmbalagem = if (calcEmbalagem == "S") "N" else "S"
    saci.updateConferido(this)
  }

  fun listCodigo(): List<String> {
    val listRef = refno?.split("/").orEmpty()
    val listFab = refFab?.split("/").orEmpty()
    val list = listRef + listFab + listOf(codigo)
    return list.filterNotNull().filter { it.isNotBlank() }
  }

  fun desmarcaConferido() {
    confirmado = "N"
    saci.updateConferido(this)
  }

  fun calculeDifs() {
    val qtCot = quantidadeCt
    val valorCot = valorUnitarioCt

    val quantPed = quantCalculada
    val valorPed = valorCalculado

    quantidadeDif = if (quantPed == null || qtCot == null) {
      null
    }
    else {
      quantPed - (qtCot * 1.0)
    }.roundDouble()

    valorUnitarioDif = if (valorPed == null || valorCot == null) {
      null
    }
    else {
      valorPed - valorCot
    }.roundDouble()
  }

  private fun Double?.roundDouble(): Double? {
    this ?: return null
    return (this * 100).roundToLong() / 100.00
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
  val dataPedido: LocalDate?,
  val onlyPendente: Boolean,
  val onlyConferido: Boolean,
  val onlyNotConferido: Boolean,
                             )