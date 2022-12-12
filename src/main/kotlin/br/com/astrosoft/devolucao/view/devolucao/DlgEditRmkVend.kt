package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.Fornecedor
import br.com.astrosoft.framework.view.SubWindowForm
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.isExpand
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.data.value.ValueChangeMode

class DlgEditRmkVend {
  fun editRmk(fornecedor: Fornecedor, save: (Fornecedor) -> Unit) {
    val form = SubWindowForm(fornecedor.labelTitle, toolBar = { window ->
      button("Salva") {
        icon = VaadinIcon.CHECK.create()
        onLeftClick {
          save(fornecedor)
          window.close()
        }
      }
    }) {
      createFormEditRmk(fornecedor)
    }
    form.open()
  }

  private fun createFormEditRmk(fornecedor: Fornecedor): Component {
    return TextArea().apply {
      this.style.set("overflow-y", "auto")
      this.isExpand = true
      this.focus()
      this.value = fornecedor.obs
      valueChangeMode = ValueChangeMode.LAZY
      valueChangeTimeout = 2000
      addValueChangeListener {
        val text = it.value
        fornecedor.obs = text
      }
    }
  }
}