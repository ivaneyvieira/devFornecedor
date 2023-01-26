package br.com.astrosoft.devolucao.view.demanda

import br.com.astrosoft.devolucao.model.beans.AgendaDemanda
import br.com.astrosoft.framework.view.localePtBr
import com.github.mvysny.karibudsl.v10.datePicker
import com.github.mvysny.karibudsl.v10.textArea
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.data.binder.Binder
import java.time.LocalDate

class FormAgendaDemanda(inicialValue: AgendaDemanda?, isReadOnly: Boolean) : FormLayout() {
  private val binder = Binder(AgendaDemanda::class.java)

  init {
    binder.bean = inicialValue

    datePicker("Data") {
      this.isReadOnly = isReadOnly
      focus()
      localePtBr()
      binder.bind(this, AgendaDemanda::date.name)
    }
    textField("Título") {
      this.isReadOnly = isReadOnly
      binder.bind(this, AgendaDemanda::titulo.name)
    }
    textArea("Conteúdo") {
      this.isReadOnly = isReadOnly
      this.minHeight = "200px"
      this.maxHeight = "300px"
      binder.bind(this, AgendaDemanda::conteudo.name)
    }
  }

  var bean: AgendaDemanda?
    get() = binder.bean
    set(value) {
      if (value == null) binder.bean =
        AgendaDemanda(id = 0, date = LocalDate.now(), titulo = "", conteudo = "")
      else binder.bean = value
    }
}