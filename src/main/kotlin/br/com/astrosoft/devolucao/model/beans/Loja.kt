package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci

class Loja(
  val no: Int,
  val sname: String,
  val name: String,
          ) {
  val descricao
    get() = "$no - $sname : $name"
  
  companion object {
    fun allLojas() = saci.allLojas()
  }
}