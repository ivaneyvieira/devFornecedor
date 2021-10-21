package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.util.format
import java.time.LocalDate

class NfPrecEntrada(
  val lj: Int,
  val ni: Int,
  val data: LocalDate,
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
  val dataStr
    get() = data.format()

  fun difGeral(fiscal: Boolean) = if (fiscal) cstDif == "N" || icmsDif == "N" || ipiDif == "N" || mvaDif == "N"
  else ncmDif == "N" || barcodeDif == "N" || refPrdDif == "N" || freteDif == "N"

  val icmsRN
    get() = icmsc ?: icmsn

  companion object {
    fun findNotas(filter: FiltroNfPrecEntrada) = saci.ultimasNfPrec(filter).filter {
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

class FiltroNfPrecEntrada(
  val storeno: Int,
  val di: LocalDate,
  val df: LocalDate,
  val vendno: Int,
  val mfno: Int,
  val ni: Int,
  val nf: String,
  val prd: String,
  var cst: EDiferenca,
  var icms: EDiferenca,
  var ipi: EDiferenca,
  var mva: EDiferenca,
  var ncm: EDiferenca,
  var barcode: EDiferenca,
  var refPrd: EDiferenca,
  var frete: EDiferenca,
  val ultimaNota: Boolean,
  val rotulo: String,
  val caraterInicial: String,
  val comGrade: Boolean,
                         ) {
  val listaCaracter
    get() = caraterInicial.split(",").map { it.trim() }.filter { it != "" }
}

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