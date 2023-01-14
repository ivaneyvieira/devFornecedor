package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.util.format
import java.time.LocalDate
import kotlin.math.absoluteValue

class NfPrecEntrada(
  val lj: Int,
  val ni: Int,
  val data: LocalDate?,
  val dataEmissao: LocalDate?,
  val nfe: String,
  val fornCad: String,
  val fornNota: String,
  val prod: String,
  val grade: String,
  val descricao: String,
  val icmsn: Double,
  val icmsp: Double,
  val icmsd: Double?,
  val icmsc: Double?,
  val ipin: Double,
  val ipip: Double,
  val cstn: String,
  val cstp: String,
  val mvan: Double,
  val mvap: Double,
  val ncmn: String,
  val ncmp: String,
  val cstDif: String,
  val icmsDif: String,
  val ipiDif: String,
  val mvaDif: String,
  val ncmDif: String,
  val barcodep: String,
  val barcoden: String,
  val barcodeDif: String,
  val refPrdp: String,
  val refPrdn: String,
  val refPrdDif: String,
  val freten: Double,
  val fretep: Double,
  val freteDif: String,
  val frete: Double,
  val precon: Double,
  val precop: Double,
  val precopc: Double,
  val precoDif: String,
  val pesoBruto: Double,
  val pedidoCompra: Int?,
  val pesoBrutoTotal: Double?,
  val pesoBrutoPrd: Double?,
  val freteKg: Double?,
  val freteUnit: Double?,
  val fretePerNf: Double?,
  val fretePerPrc: Double?,
  val fretePerDif: String,
  val quant: Int?,
  val freteTotal: Double?,
  val preconTotal: Double?,
                   ) {
  val precoDifValue
    get() = precon - precop

  val precoPercen
    get() = precoDifValue * 100.00 / (precop * 1.00)

  val mvanApŕox: Double
    get() {
      val dif = (mvan - mvap).absoluteValue
      return if (dif < 0.01) mvap
      else mvan
    }

  val dataStr
    get() = data.format()

  fun difGeral(fiscal: Boolean) = if (fiscal) cstDif == "N" || icmsDif == "N" || ipiDif == "N" || mvaDif == "N"
  else ncmDif == "N" || barcodeDif == "N" || refPrdDif == "N" || freteDif == "N"

  val icmsRN
    get() = icmsc ?: icmsn

  companion object {
    fun findNotas(filter: FiltroRelatorio) = saci.ultimasNfPrec(filter).filter {
      it.filtroCaracter(filter.listaCaracter)
    }

    fun findNotasPreRec(filter: FiltroRelatorio) = saci.ultimasPreRecebimento(filter).filter {
      it.filtroCaracter(filter.listaCaracter)
    }
  }
}

private fun NfPrecEntrada.filtroCaracter(listaCaracter: List<String>): Boolean {
  return if (listaCaracter.isEmpty()) true
  else {
    val listBoolean = listaCaracter.map { character ->
      !this.descricao.startsWith(character)
    }
    listBoolean.all { it }
  }
}

open class FiltroRelatorio(
  open val storeno: Int,
  open val di: LocalDate,
  open val df: LocalDate,
  open val vendno: Int,
  open val mfno: Int,
  open val ni: Int,
  open val nf: String,
  open val prd: String,
  open var cst: EDiferencaStr,
  open var icms: EDiferencaStr,
  open var ipi: EDiferencaStr,
  open var mva: EDiferencaStr,
  open var ncm: EDiferencaStr,
  open var barcode: EDiferencaStr,
  open var refPrd: EDiferencaStr,
  open var frete: EDiferencaNum,
  open var fretePer: EDiferencaNum,
  open var preco: EDiferencaNum,
  open var pesquisa: String,
  open val ultimaNota: Boolean,
  open val rotulo: String,
  open val caraterInicial: String,
  open val comGrade: Boolean,
  open val listaProdutos: String,
                          ) {
  val listaCaracter
    get() = caraterInicial.split(",").map { it.trim() }.filter { it != "" }
}

enum class EDiferencaNum(val str: String, val descricao: String) {
  S("S", "Igual"), DP("DP", "Diferente >"), DN("DN", "Diferente <"), T("T", "Todos")
}

enum class EDiferencaStr(val str: String, val descricao: String) {
  S("S", "Igual"), N("N", "Diferente"), T("T", "Todos")
}

data class NfPrecEntradaGrupo(val nomeGrupo: String,
                              val nota: NfPrecEntrada,
                              val pedidoCompra: Int,
                              val valorNota: String,
                              val valorPrecificacao: String) {
  val lj = nota.lj
  val ni = nota.ni
  val dataStr = nota.dataStr
  val nfe = nota.nfe
  val fornCad = nota.fornCad
  val fornNota = nota.fornNota
  val prod = nota.prod
  val descricao = nota.descricao
  val grade = nota.grade
}