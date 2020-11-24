package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import java.time.LocalDate

class NFFile(
  val storeno: Int,
  val pdvno: Int,
  val xano: Int,
  val date: LocalDate,
  var nome: String,
  var file: ByteArray
            ) {
  fun insert() = saci.insertFile(this)
  
  fun update() = saci.updateFile(this)
  
  fun delete() = saci.deleteFile(this)
  
  fun saveFile() {
    saci.insertFile(this)
  }
}