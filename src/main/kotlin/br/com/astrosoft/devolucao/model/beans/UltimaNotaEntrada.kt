package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.util.format
import java.time.LocalDate

class UltimaNotaEntrada(val lj: Int,
                        val ni: Int,
                        val data: LocalDate,
                        val nfe: String,
                        val fornCad: String,
                        val fornNota: String,
                        val prod: String,
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
                        val ncmDif: String) {
  val dataStr
    get() = data.format()

  val difGeral
    get() = cstDif == "N" || icmsDif == "N" || ipiDif == "N" || mvaDif == "N" || ncmDif == "N"

  val icmsRN
    get() = icmsc ?: icmsn

  companion object {
    fun findNotas(filter: FiltroUltimaNotaEntrada) = saci.ultimasNotasEntrada(filter).filter {
      it.filtroCaracter(filter.listaCaracter)
    }
  }
}

private fun UltimaNotaEntrada.filtroCaracter(listaCaracter: List<String>): Boolean {
  return if (listaCaracter.isEmpty()) true
  else {
    val listBoolean = listaCaracter.map { character ->
      !this.descricao.startsWith(character)
    }
    listBoolean.all { it }
  }
}

class FiltroUltimaNotaEntrada(val storeno: Int,
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
                              val ultimaNota: Boolean,
                              val rotulo: String,
                              val caraterInicial: String) {
  val listaCaracter
    get() = caraterInicial.split(",").map { it.trim() }.filter { it != "" }
}

enum class EDiferenca(val str: String, val descricao: String) {
  S("S", "Igual"), N("N", "Diferente"), T("T", "Todos")
}

data class UltimaNotaEntradaGrupo(val nomeGrupo: String,
                                  val nota: UltimaNotaEntrada,
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
}