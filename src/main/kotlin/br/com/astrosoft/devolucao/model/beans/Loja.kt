package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci

class Loja(val no: Int, val sname: String, val name: String, val cnpjLoja: String) {
  val descricao
    get() = "$no - $sname"

  companion object {
    val lojaZero = Loja(0, "Todas", "", cnpjLoja = "")
    fun allLojas() = saci.allLojas().toList()
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Loja

    return no == other.no
  }

  override fun hashCode(): Int {
    return no
  }
}