package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.knfe.Detalhe
import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.model.MonitorHandler
import br.com.astrosoft.framework.util.format
import java.time.LocalDate
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlin.math.truncate

class NfPrecEntrada(
  val lj: Int,
  val ni: Int,
  val data: LocalDate?,
  val dataEmissao: LocalDate?,
  val nfe: String,
  val serie: String,
  val fornCad: String,
  val fornNota: String,
  val prod: String,
  val grade: String,
  val descricao: String,
  val icmsn: Double?,
  val icmsp: Double?,
  val icmsd: Double?,
  val icmsc: Double?,
  val ipin: Double?,
  val ipip: Double?,
  val cstn: String?,
  val cstp: String?,
  val mvan: Double?,
  val mvap: Double?,
  val ncmn: String?,
  val ncmp: String?,
  val cstDif: String?,
  val icmsDif: String?,
  val ipiDif: String?,
  val mvaDif: String?,
  val ncmDif: String?,
  val barcodepl: String?,
  val barcodecl: String?,
  val barcoden: String?,
  val barcodebp: String?,
  val barcodeDif: String?,
  val refPrdp: String?,
  val refPrdn: String?,
  val refPrdDif: String?,
  val freten: Double?,
  val fretep: Double?,
  val freteDif: String?,
  val frete: Double?,
  val precon: Double?,
  val precop: Double?,
  val precopc: Double?,
  val precoDif: String?,
  val pesoBruto: Double?,
  val pedidoCompra: Int?,
  val pesoBrutoTotal: Double?,
  val pesoBrutoPrd: Double?,
  val freteKg: Double?,
  val freteUnit: Double?,
  val fretePerNf: Double?,
  val fretePerPrc: Double?,
  val fretePerDif: String?,
  val quant: Int?,
  val freteTotal: Double?,
  val preconTotal: Double?,
  val estoque: Int?,
  val mesesValidade: Int?,
  val cstIcms: String?,
  val cfop: String?,
  val valor: Double?,
  val vlDesconto: Double?,
  val vlLiquido: Double?,
  val vlFrete: Double?,
  val vlDespesas: Double?,
  val vlIcms: Double?,
  val vlIpi: Double?,
  val baseSubst: Double?,
  val vlIcmsSubst: Double?,
  val vlTotal: Double?,
) {
  val barcodepList get() = barcodepl?.split(",") ?: emptyList()
  val barcodecList get() = barcodecl?.split(",") ?: emptyList()
  val barcodebpList get() = barcodebp?.split(",") ?: emptyList()

  fun toHead() = NotaEntradaHead(lj, ni, data, dataEmissao, nfe, fornCad, fornNota)

  val barcodep
    get() = (barcodepList + barcodebpList).firstOrNull {
      it == barcodex
    } ?: barcodepList.firstOrNull()

  val barcodec
    get() = barcodecList.firstOrNull {
      it == barcodex
    } ?: barcodecList.firstOrNull()

  private fun detalheXml(): List<Detalhe> {
    val ref = refPrdn ?: ""
    return NddXml.detalheProduto(ni, lj, nfe, serie, ref, barcodepList).distinct()
  }

  val refPrdDifx
    get() = if ((refPrdx ?: "") == (refPrdp ?: "")) "S" else "N"

  val barcodeDifxp
    get() = if ((barcodex ?: "") == (barcodep ?: "")) "S" else "N"

  val barcodeDifpc
    get() = if (grade == "") {
      if ((barcodep ?: "") == (barcodec ?: "")) "S"
      else "N"
    } else "S"

  val cfopDifxp: String
    get() = if (cfop.ajustaCFOP() == cfopx.ajustaCFOP()) "S" else "N"

  fun String?.ajustaCFOP(): String {
    return this?.let {
      if (it.length > 1) it.substring(1) else ""
    }?.let {
      when (it) {
        "401" -> "403"
        "101" -> "102"
        else -> it
      }
    } ?: ""
  }

  val mvaDifxn
    get() = if (mvan.format() == mvax.format()) "S" else "N"

  val mvaDifnp
    get() = if (mvan.format() == mvap.format()) "S" else "N"

  val icmsDifxn: String
    get() {
      val icx = vlIcmsx
      val icn = vlIcms ?: 0.00
      val dif = (truncate(icx * 100).roundToInt() - truncate(icn * 100).roundToInt()).absoluteValue
      return if (dif <= 2) "S" else "N"
    }

  val ipiDifxn
    get() = if (vlIpix.format() == vlIpi.format()) "S" else "N"

  val baseSubstxn
    get() = if (baseSubstx.format() == baseSubst.format()) "S" else "N"

  val vlIcmsSubstxn
    get() = if (vlIcmsSubstx.format() == vlIcmsSubst.format()) "S" else "N"

  val vlTotalxn: String
    get() {
      val icx = valorx
      val icn = valor ?: 0.00
      val dif = (truncate(icx * 100).roundToInt() - truncate(icn * 100).roundToInt()).absoluteValue
      return if (dif <= 2) "S" else "N"
    }

  val ncmDifx
    get() = if ((ncmx ?: "") == (ncmp ?: "")) "S" else "N"

  val cstDifxn
    get() = if ((cstx ?: "") == (cstIcms ?: "")) "S" else "N"

  val cstDifnp: String
    get() {
      val vcstn = cstIcms?.subSequence(1, 3).let {
        when (it) {
          "10" -> "06"
          "60" -> "06"
          else -> it
        }
      } ?: ""
      val vcstp = cstp ?: ""
      return if (vcstp == vcstn) "S" else "N"
    }

  val cstx: String?
    get() {
      val icms = detalheXml().firstOrNull()?.imposto?.icms ?: return null
      val orig = icms.orig
      val cst = icms.cst
      return "$orig$cst"
    }

  val vlIcmsx
    get() = if (baseSubstx.absoluteValue > 0.02) 0.00 else detalheXml().sumOf { it.imposto?.icms?.vICMS ?: 0.00 }

  val vlIpix
    get() = detalheXml().sumOf { it.imposto?.ipi?.vIPI ?: 0.00 }

  val baseSubstx
    get() = detalheXml().sumOf { it.imposto?.icms?.vBCST ?: 0.00 }

  val vlIcmsSubstx
    get() = detalheXml().sumOf { it.imposto?.icms?.vICMSST ?: 0.00 }

  val vlTotalx
    get() = (detalheXml().sumOf { it.prod?.vProd ?: 0.00 }) + (vlFrete ?: 0.00) + vlIcmsx + vlIpix + baseSubstx

  val valorx
    get() = detalheXml().sumOf { it.prod?.vProd ?: 0.00 }

  val mvax: Double?
    get() {
      val icms = detalheXml().firstOrNull()?.imposto?.icms ?: return null
      return icms.mvaST
    }

  val cfopx: String?
    get() {
      val prod = detalheXml().firstOrNull()?.prod ?: return null
      return prod.cfop
    }

  val refPrdx: String?
    get() {
      return detalheXml().firstOrNull()?.prod?.cProd ?: return null
    }

  val barcodex: String?
    get() {
      return detalheXml().firstOrNull()?.prod?.cEANTrib ?: return null
    }

  val ncmx
    get() = detalheXml().firstOrNull()?.prod?.ncm


  val ljCol
    get() = if (lj == 999) null else lj

  val niCol
    get() = if (lj == 999) null else ni

  val precoDifValue: Double?
    get() {
      val pn = precon ?: return null
      val pp = precop ?: return null
      return pn - pp
    }

  val precoPercen: Double?
    get() {
      val pd = precoDifValue ?: return null
      val pp = precop ?: return null
      return pd * 100.00 / (pp * 1.00)
    }

  val mvanAprox: Double?
    get() {
      val mp = mvap ?: return null
      val mn = mvan ?: return null
      val dif = (mn - mp).absoluteValue
      return if (dif < 0.01) mvap
      else mvan
    }

  val dataStr
    get() = data.format()

  fun difGeral(fiscal: Boolean) =
    if (fiscal) (cstIcms ?: "") != (cstx ?: "") || icmsDif == "N" || ipiDif == "N" || mvaDif == "N"
    else ncmDif == "N" || barcodeDif == "N" || refPrdDif == "N" || freteDif == "N"

  val icmsRN
    get() = icmsc ?: icmsn

  companion object {
    fun findNotas(filter: FiltroRelatorio, monitor: MonitorHandler? = null) =
      saci.ultimasNfPrec(filter, monitor = monitor).filter {
        it.filtroCaracter(filter.listaCaracter) && when (filter.tipoValidade) {
          EValidade.TODAS -> true
          EValidade.ComValidade -> it.mesesValidade != null && it.mesesValidade != 0
          EValidade.SemValidade -> it.mesesValidade == null || it.mesesValidade == 0
        }
      }

    fun findNotasPreRec(filter: FiltroRelatorio) = saci.ultimasPreRecebimento(filter).filter {
      it.filtroCaracter(filter.listaCaracter) && when (filter.tipoValidade) {
        EValidade.TODAS -> true
        EValidade.ComValidade -> it.mesesValidade != null && it.mesesValidade != 0
        EValidade.SemValidade -> it.mesesValidade == null || it.mesesValidade == 0
      }
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
  open var cfop: EDiferencaStr,
  open var baseST: EDiferencaStr,
  open var valorST: EDiferencaStr,
  open var totalNF: EDiferencaStr,
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
  var tipoValidade: EValidade = EValidade.TODAS,
) {
  val listaCaracter
    get() = caraterInicial.split(",").map { it.trim() }.filter { it != "" }
}

enum class EDiferencaNum(val str: String, val descricao: String) {
  S("S", "Igual"), DP("DP", "Diferente >"), DN("DN", "Diferente <"), T("T", "Todos")
}

enum class EValidade(val descricao: String) {
  TODAS("Todas"), ComValidade("Com Validade"), SemValidade("Sem Validade")
}

enum class EDiferencaStr(val str: String, val descricao: String) {
  S("S", "Igual"), N("N", "Diferente"), T("T", "Todos")
}

data class NfPrecEntradaGrupo(
  val nomeGrupo: String,
  val nota: NfPrecEntrada,
  val pedidoCompra: Int,
  val valorNota: String,
  val valorPrecificacao: String
) {
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

fun List<NfPrecEntrada>.group(): List<NfPrecEntrada> {
  val group = this.groupBy { it.ni }

  val listGroup = group.mapNotNull { (ni, list) ->
    NfPrecEntrada(
      lj = 999,
      ni = ni,
      data = null,
      dataEmissao = null,
      nfe = "",
      serie = "",
      fornCad = "",
      fornNota = "",
      prod = "",
      grade = "",
      descricao = "",
      icmsn = null,
      icmsp = null,
      icmsd = null,
      icmsc = null,
      ipin = null,
      ipip = null,
      cstn = null,
      cstp = null,
      mvan = null,
      mvap = null,
      ncmn = null,
      ncmp = null,
      cstDif = null,
      icmsDif = null,
      ipiDif = null,
      mvaDif = null,
      ncmDif = null,
      barcodepl = null,
      barcoden = null,
      barcodecl = null,
      barcodebp = null,
      barcodeDif = null,
      refPrdp = null,
      refPrdn = null,
      refPrdDif = null,
      freten = null,
      fretep = null,
      freteDif = null,
      frete = null,
      precon = null,
      precop = null,
      precopc = null,
      precoDif = null,
      pesoBruto = null,
      pedidoCompra = null,
      pesoBrutoTotal = null,
      pesoBrutoPrd = null,
      freteKg = null,
      freteUnit = null,
      fretePerNf = null,
      fretePerPrc = null,
      fretePerDif = null,
      quant = null,
      freteTotal = null,
      preconTotal = null,
      estoque = null,
      mesesValidade = null,
      cstIcms = null,
      cfop = null,
      valor = list.sumOf { it.valor ?: 0.00 },
      vlDesconto = list.sumOf { it.vlDesconto ?: 0.00 },
      vlLiquido = list.sumOf { it.vlLiquido ?: 0.00 },
      vlFrete = list.sumOf { it.vlFrete ?: 0.00 },
      vlDespesas = list.sumOf { it.vlDespesas ?: 0.00 },
      vlIcms = list.sumOf { it.vlIcms ?: 0.00 },
      vlIpi = list.sumOf { it.vlIpi ?: 0.00 },
      baseSubst = list.sumOf { it.baseSubst ?: 0.00 },
      vlIcmsSubst = list.sumOf { it.vlIcmsSubst ?: 0.00 },
      vlTotal = list.sumOf { it.vlTotal ?: 0.00 },
    )
  }

  return this + listGroup
}