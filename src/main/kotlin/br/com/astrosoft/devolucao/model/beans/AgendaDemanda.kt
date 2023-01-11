package br.com.astrosoft.devolucao.model.beans

import java.time.LocalDate

class AgendaDemanda(
  val id: Int,
  val date: LocalDate,
  val titulo: String,
  val conteudo: String,
                   )