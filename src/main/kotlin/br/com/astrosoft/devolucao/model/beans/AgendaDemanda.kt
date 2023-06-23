package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import java.time.LocalDate

class AgendaDemanda(
    var id: Int = 0,
    var date: LocalDate,
    var titulo: String,
    var conteudo: String,
    var concluido: String = "N",
    var quantAnexo: Int = 0,
    var vendno: Int = 0,
    var destino: String,
    var origem: String,
) {

  val conteudoSingle
    get() = conteudo.split("\n").firstOrNull()

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
    val nfFile = NFFile(storeno = 1, pdvno = 8888, xano = id, date = LocalDate.now(), nome = nome, file = file)
    saci.insertFile(nfFile)
  }

  fun delAnexo(nfFile: NFFile) {
    saci.deleteFile(nfFile)
  }

  fun marcaConcluido() {
    concluido = "S"
    save()
  }

  fun marcaVolta() {
    concluido = "N"
    save()
  }

  companion object {
    fun findAll(filter: FilterAgendaDemanda): List<AgendaDemanda> {
      return saci.selectAgendaDemanda(filter)
    }
  }
}

data class FilterAgendaDemanda(val pesquisa: String, val concluido: Boolean?, val vendno: Int)