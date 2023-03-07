package br.com.astrosoft.devolucao.view.demanda

import br.com.astrosoft.devolucao.model.beans.FornecedorProduto
import com.github.mvysny.karibudsl.v10.responsiveSteps
import com.github.mvysny.karibudsl.v10.textArea
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.data.binder.Binder

class FormFornecedor(inicialValue: FornecedorProduto?, isReadOnly: Boolean) : FormLayout() {
  private val binder = Binder(FornecedorProduto::class.java)

  init {
    width = "840px"
    binder.bean = inicialValue
    this.responsiveSteps {
      "0px"(1, top)
      "200px"(1, aside)
    }

    textArea("Conteúdo") {
      setWidthFull()
      //this.minHeight = "400px"
      this.maxHeight = "500px"
      this.isReadOnly = isReadOnly

      binder.bind(this, FornecedorProduto::texto.name)
    }
  }

  var bean: FornecedorProduto?
    get() = binder.bean
    set(value) {
      if (value == null) binder.bean = null
      else binder.bean = value
    }
}