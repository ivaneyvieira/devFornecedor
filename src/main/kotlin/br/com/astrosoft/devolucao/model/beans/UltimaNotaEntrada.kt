package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.util.format
import java.time.LocalDate

class UltimaNotaEntrada(val lj: Int,
                        val ni: Int,
                        val data: LocalDate,
                        val nfe: String,
                        val forn: String,
                        val prod: String,
                        val descricao: String,
                        val icmsn: Double,
                        val icmsp: Double,
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

  companion object {
    fun findAll(filtro: FiltroUltimaNotaEntrada) = saci.ultimasNotasEntrada(filtro)
  }
}

class FiltroUltimaNotaEntrada(val storeno: Int,
                              val di: LocalDate,
                              val df: LocalDate,
                              val vendno: Int,
                              val ni: Int,
                              val nf: String,
                              val prd: String,
                              var cst: EDiferenca,
                              var icms: EDiferenca,
                              var ipi: EDiferenca,
                              var mva: EDiferenca,
                              var ncm: EDiferenca)

enum class EDiferenca(val str: String, val descricao: String) {
  S("S", "Igual"), N("N", "Diferente"), T("T", "Todos")
}