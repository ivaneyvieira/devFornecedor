package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci

class PrdRef(val prdno: String, val grade: String, val prdrefname: String, val prdrefno: String) {
  val codigo get() = prdno.trim()

  fun list(): List<PrdRef> {
    return listPrdRef(codigo, grade)
  }

  fun add() {
    add(listOf(this))
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as PrdRef

    if (prdno != other.prdno) return false
    if (grade != other.grade) return false
    if (prdrefname != other.prdrefname) return false
    return prdrefno == other.prdrefno
  }

  override fun hashCode(): Int {
    var result = prdno.hashCode()
    result = 31 * result + grade.hashCode()
    result = 31 * result + prdrefname.hashCode()
    result = 31 * result + prdrefno.hashCode()
    return result
  }

  companion object {
    fun listPrdRef(codigo: String, grade: String) = saci.listPrdRef(codigo, grade)

    fun add(listPrdRef: List<PrdRef>) {
      saci.addPrdRef(listPrdRef)
    }
  }
}