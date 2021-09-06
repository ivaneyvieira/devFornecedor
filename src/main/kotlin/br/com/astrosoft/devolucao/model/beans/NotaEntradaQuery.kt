package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.util.format
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
  val cstn: String,
  val cstp: String,
  val cfop: String,
  val un: String,
  val quant: Int,
  val valorUnit: Double,
  val valorTotal: Double,
  val baseIcms: Double,
  val valorIPI: Double,
  val aliqIpi: Double,
  val valorIcms: Double,
  val aliqIcms: Double,
  val aliqIpiP: Double,
  val aliqIcmsP: Double,
                      ) {
  val dataStr
    get() = data.format()

  fun grupo(descricaoGrupo: String, valorNota: String, valorProduto: String): NotaEntradaQueryGrupo {
    return NotaEntradaQueryGrupo(
      lj,
      ni,
      data,
      dataStr,
      nfe,
      fornCad,
      fornNota,
      prod,
      descricao,
      ncm,
      cfop,
      un,
      quant,
      valorUnit,
      valorTotal,
      baseIcms,
      descricaoGrupo,
      valorNota,
      valorProduto,
                                )
  }

  companion object {
    fun findNotas(filter: FiltroNotaEntradaQuery) = saci.todasNotasEntradaQuery(filter).filter {
      it.filtroCaracter(filter.listaCaracter)
    }
  }
}

class NotaEntradaQueryGrupo(val lj: Int,
                            val ni: Int,
                            val data: LocalDate,
                            val dataStr: String,
                            val nfe: String,
                            val fornCad: Int,
                            val fornNota: Int,
                            val prod: String,
                            val descricao: String,
                            val ncm: String,
                            val cfop: String,
                            val un: String,
                            val quant: Int,
                            val valorUnit: Double,
                            val valorTotal: Double,
                            val baseIcms: Double,
                            val descricaoGrupo: String,
                            val valorNota: String,
                            val valorProduto: String) {

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
                                  val listaProdutos: String,
                                  val caraterInicial: String) {
  val listaCaracter
    get() = caraterInicial.split(",").map { it.trim() }.filter { it != "" }
}