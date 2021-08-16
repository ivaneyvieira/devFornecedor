package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import java.time.LocalDate

class NotaEntradaQuery(
  val lj: Int,
  val ni: Int,
  val data: LocalDate,
  val nfe: String,
  val fornCad: Int,
  val fornNota: Int,
  val prod: String,
  val descricao: String,
  val ncm: String,
  val cst: String,
  val cfop: String,
  val un: String,
  val quant: Double,
  val valorUnit: Double,
  val valorTotal: Double,
  val baseIcms: Double,
  val valorIPI: Double,
  val aliqIpi: Double,
  val valorIcms: Double,
  val aliqIcms: Double,
                      ) {
  companion object {
    fun findNotas(filter: FiltroNotaEntradaQuery) = saci.todasNotasEntradaQuery(filter).filter {
      it.filtroCaracter(filter.listaCaracter)
    }
  }
}

private fun NotaEntradaQuery.filtroCaracter(listaCaracter: List<String>): Boolean {
  return if (listaCaracter.isEmpty()) true
  else {
    val listBoolean = listaCaracter.map { character ->
      !this.descricao.startsWith(character)
    }
    listBoolean.all { it }
  }
}

data class FiltroNotaEntradaQuery(val storeno: Int,
                                  val di: LocalDate,
                                  val df: LocalDate,
                                  val vendno: Int,
                                  val mfno: Int,
                                  val ni: Int,
                                  val nf: String,
                                  val prd: String,
                                  val caraterInicial: String) {
  val listaCaracter
    get() = caraterInicial.split(",").map { it.trim() }.filter { it != "" }
}