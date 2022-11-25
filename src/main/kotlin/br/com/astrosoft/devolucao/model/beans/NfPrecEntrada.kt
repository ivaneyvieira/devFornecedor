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
                   ) {
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
  open var cst: EDiferenca,
  open var icms: EDiferenca,
  open var ipi: EDiferenca,
  open var mva: EDiferenca,
  open var ncm: EDiferenca,
  open var barcode: EDiferenca,
  open var refPrd: EDiferenca,
  open var frete: EDiferenca,
  open val ultimaNota: Boolean,
  open val rotulo: String,
  open val caraterInicial: String,
  open val comGrade: Boolean,
                          ) {
  val listaCaracter
    get() = caraterInicial.split(",").map { it.trim() }.filter { it != "" }
}

class FiltroNfPrecEntrada(
  override val storeno: Int,
  override val di: LocalDate,
  override val df: LocalDate,
  override val vendno: Int,
  override val mfno: Int,
  override val ni: Int,
  override val nf: String,
  override val prd: String,
  override var cst: EDiferenca,
  override var icms: EDiferenca,
  override var ipi: EDiferenca,
  override var mva: EDiferenca,
  override var ncm: EDiferenca,
  override var barcode: EDiferenca,
  override var refPrd: EDiferenca,
  override var frete: EDiferenca,
  override val ultimaNota: Boolean,
  override val rotulo: String,
  override val caraterInicial: String,
  override val comGrade: Boolean,
                         ) : FiltroRelatorio(storeno,
                                             di,
                                             df,
                                             vendno,
                                             mfno,
                                             ni,
                                             nf,
                                             prd,
                                             cst,
                                             icms,
                                             ipi,
                                             mva,
                                             ncm,
                                             barcode,
                                             refPrd,
                                             frete,
                                             ultimaNota,
                                             rotulo,
                                             caraterInicial,
                                             comGrade)

class FiltroFreteEntrada(
  override val storeno: Int,
  override val di: LocalDate,
  override val df: LocalDate,
  override val vendno: Int,
  override val mfno: Int,
  override val ni: Int,
  override val nf: String,
  override val prd: String,
  override var cst: EDiferenca,
  override var icms: EDiferenca,
  override var ipi: EDiferenca,
  override var mva: EDiferenca,
  override var ncm: EDiferenca,
  override var barcode: EDiferenca,
  override var refPrd: EDiferenca,
  override var frete: EDiferenca,
  override val ultimaNota: Boolean,
  override val rotulo: String,
  override val caraterInicial: String,
  override val comGrade: Boolean,
                        ) : FiltroRelatorio(storeno,
                                            di,
                                            df,
                                            vendno,
                                            mfno,
                                            ni,
                                            nf,
                                            prd,
                                            cst,
                                            icms,
                                            ipi,
                                            mva,
                                            ncm,
                                            barcode,
                                            refPrd,
                                            frete,
                                            ultimaNota,
                                            rotulo,
                                            caraterInicial,
                                            comGrade)

enum class EDiferenca(val str: String, val descricao: String) {
  S("S", "Igual"), N("N", "Diferente"), T("T", "Todos")
}

data class NfPrecEntradaGrupo(val nomeGrupo: String,
                              val nota: NfPrecEntrada,
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