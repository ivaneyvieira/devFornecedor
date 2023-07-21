package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci

class PrdRef(val prdno: String, val grade: String, val prdrefname: String, val prdrefno: String) {
  val codigo get() = prdno.trim()

  fun list(): List<PrdRef> {
    return listPrdRef(codigo, grade)
  }

  fun add() {
    addPrdRef(listOf(this))
  }

  override fun equals(other: Any?): Boolean = when (other) {
    is PrdRef -> codigo == other.codigo && grade == other.grade && prdrefname == other.prdrefname && prdrefno == other.prdrefno
    else -> false
  }

  override fun hashCode(): Int {
    return codigo.hashCode() + grade.hashCode() + prdrefname.hashCode() + prdrefno.hashCode()
  }

  companion object {
    fun listPrdRef(codigo: String, grade: String) = saci.listPrdRef(codigo, grade)

    fun addPrdRef(listPrdRef: List<PrdRef>) {
      saci.addPrdRef(listPrdRef)
    }
  }
}