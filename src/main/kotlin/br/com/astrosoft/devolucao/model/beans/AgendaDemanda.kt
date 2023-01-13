package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import java.time.LocalDate

class AgendaDemanda(
  var id: Int = 0,
  var date: LocalDate,
  var titulo: String,
  var conteudo: String,
                   ) {
  fun delete() = saci.deleteAgendaDemanda(this)

  fun save() {
    if (id == 0) insert()
    else update()
  }

  fun insert() = saci.insertAgendaDemanda(this)

  fun update() = saci.updateAgendaDemanda(this)

  fun findAnexos(): List<NFFile> {
    return saci.selectFile(this)
  }

  fun addAnexo(nome: String, file: ByteArray) {
    val nfFile = NFFile(storeno = 1, pdvno = 8888, xano = id, date = date, nome = nome, file = file)
    saci.insertFile(nfFile)
  }

  fun delAnexo(nfFile: NFFile) {
    saci.deleteFile(nfFile)
  }


  companion object {
    fun findAll(): List<AgendaDemanda> {
      return saci.selectAgendaDemanda()
    }
  }
}