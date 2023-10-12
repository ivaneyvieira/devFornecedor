package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.framework.view.SubWindowForm
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.isExpand
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.data.value.ValueChangeMode

class DlgEditRmk {
  fun editRmk(nota: NotaSaida, save: (NotaSaida) -> Unit) {
    val form =
        SubWindowForm("PROCESSO INTERNO: ${nota.nota}|DEV FORNECEDOR: ${nota.fornecedor}", toolBar = { window ->
          button("Salva") {
            icon = VaadinIcon.CHECK.create()
            onLeftClick {
              save(nota)
              window.close()
            }
          }
        }) {
          createFormEditRmk(nota)
        }
    form.open()
  }

  private fun createFormEditRmk(nota: NotaSaida): Component {
    return TextArea().apply {
      this.style.set("overflow-y", "auto")
      this.isExpand = true
      this.focus()
      this.value = nota.rmk
      valueChangeMode = ValueChangeMode.LAZY
      valueChangeTimeout = 2000
      addValueChangeListener {
        val text = it.value
        nota.rmk = text
      }
    }
  }
}