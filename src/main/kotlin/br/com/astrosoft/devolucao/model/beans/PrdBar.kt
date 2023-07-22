package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci

class PrdBar(val prdno: String, val grade: String, val barcode: String) {
  val codigo get() = prdno.trim()

  fun add() {
    add(listOf(this))
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as PrdBar

    if (prdno != other.prdno) return false
    if (grade != other.grade) return false
    return barcode == other.barcode
  }

  override fun hashCode(): Int {
    var result = prdno.hashCode()
    result = 31 * result + grade.hashCode()
    result = 31 * result + barcode.hashCode()
    return result
  }

  companion object {
    fun add(listPrdBar: List<PrdBar>) {
      saci.addPrdBar(listPrdBar)
    }
  }
}