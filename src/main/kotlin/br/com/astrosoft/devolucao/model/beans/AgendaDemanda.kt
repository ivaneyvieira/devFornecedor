package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.devolucao.model.saci
import java.time.LocalDate

class AgendaDemanda(
  var id: Int = 0,
  var date: LocalDate,
  var titulo: String,
  var conteudo: String,
                   ){
  fun delete() = saci.deleteAgendaDemanda(this)

  fun save() {
    if(id == 0)
      insert()
    else
      update()
  }

  fun insert() = saci.insertAgendaDemanda(this)

  fun update() = saci.updateAgendaDemanda(this)

  companion object {
    fun findAll() : List<AgendaDemanda> {
      return saci.selectAgendaDemanda()
    }
  }
}