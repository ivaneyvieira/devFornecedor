package br.com.astrosoft.devolucao.view.devolucao.columns

import br.com.astrosoft.devolucao.model.beans.EmailDB
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnLocalTime
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid

object EmailDBViewColumns {
  fun Grid<EmailDB>.emailTipo() = addColumnString(EmailDB::tipoEmail) {
    this.setHeader("Tipo")
  }
  
  fun Grid<EmailDB>.emailEmail() = addColumnString(EmailDB::email) {
    this.setHeader("E-mail")
  }
  
  fun Grid<EmailDB>.emailAssunto() = addColumnString(EmailDB::assunto) {
    this.setHeader("Assunto")
  }
  
  fun Grid<EmailDB>.emailData() = addColumnLocalDate(EmailDB::data) {
    this.setHeader("Data")
  }
  
  fun Grid<EmailDB>.emailHora() = addColumnLocalTime(EmailDB::hora) {
    this.setHeader("Hora")
  }
}