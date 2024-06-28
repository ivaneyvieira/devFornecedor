package br.com.astrosoft.devolucao.view.demanda

import br.com.astrosoft.devolucao.model.beans.AgendaDemanda
import br.com.astrosoft.framework.view.localePtBr
import com.github.mvysny.karibudsl.v10.datePicker
import com.github.mvysny.karibudsl.v10.responsiveSteps
import com.github.mvysny.karibudsl.v10.textArea
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.data.binder.Binder
import java.time.LocalDate

class FormAgendaDemanda(inicialValue: AgendaDemanda?, isReadOnly: Boolean) : FormLayout() {
  private val binder = Binder(AgendaDemanda::class.java)

  init {
    binder.bean = inicialValue
    this.responsiveSteps {
      "0px"(1, top)
      "200px"(4, aside)
    }

    datePicker("Data") {
      this.isReadOnly = isReadOnly
      focus()
      localePtBr()
      binder.bind(this, AgendaDemanda::date.name)
      setColspan(this, 1)
    }
    textField("Título") {
      setColspan(this, 3)
      this.isReadOnly = isReadOnly
      binder.bind(this, AgendaDemanda::titulo.name)
    }
    textField("Origem") {
      setColspan(this, 2)
      this.isReadOnly = isReadOnly
      binder.bind(this, AgendaDemanda::origem.name)
    }
    textField("Destino") {
      setColspan(this, 2)
      this.isReadOnly = isReadOnly
      binder.bind(this, br.com.astrosoft.devolucao.model.beans.AgendaDemanda::destino.name)
    }
    textArea("Conteúdo") {
      setColspan(this, 4)
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
          AgendaDemanda(
            id = 0,
            date = LocalDate.now(),
            titulo = "",
            conteudo = "",
            destino = "",
            origem = "",
            userno = 0,
          )
      else binder.bean = value
    }
}