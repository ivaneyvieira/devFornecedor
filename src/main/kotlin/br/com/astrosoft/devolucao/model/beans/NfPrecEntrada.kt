package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.knfe.Detalhe
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
  val barcodec: String?,
  val barcoden: String?,
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
  val barcodepList = barcodepl?.split(",") ?: emptyList()

  fun toHead() = NotaEntradaHead(lj, ni, data, dataEmissao, nfe, fornCad, fornNota)

  val barcodep
    get() = barcodepList.firstOrNull {
      it == barcodex
    } ?: barcodepList.firstOrNull()

  private fun detalheXml(): Detalhe? {
    val ref = refPrdn ?: ""
    return NddXml.detalheProduto(ni, ref, barcodepList)
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
      val icms = detalheXml()?.imposto?.icms ?: return null
      val orig = icms.orig
      val cst = icms.cst
      return "$orig$cst"
    }

  val mvax: Double?
    get() {
      val icms = detalheXml()?.imposto?.icms ?: return null
      return icms.mvaST
    }

  val cfopx: String?
    get() {
      val prod = detalheXml()?.prod ?: return null
      return prod.cfop
    }

  val refPrdx: String?
    get() {
      return detalheXml()?.prod?.cProd ?: return null
    }

  val barcodex: String?
    get() {
      return detalheXml()?.prod?.cEANTrib ?: return null
    }

  val ncmx
    get() = detalheXml()?.prod?.ncm


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
    fun findNotas(filter: FiltroRelatorio) = saci.ultimasNfPrec(filter).filter {
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
      barcodec = null,
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