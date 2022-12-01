package br.com.astrosoft.devolucao.view.entrada.columms

import br.com.astrosoft.devolucao.model.beans.EDiferencaNum
import br.com.astrosoft.devolucao.model.beans.EDiferencaStr
import br.com.astrosoft.devolucao.model.beans.NfPrecEntrada
import com.github.mvysny.karibudsl.v10.comboBox
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.grid.Grid

fun HasComponents.comboDiferencaNum(label: String, block: ComboBox<EDiferencaNum>.() -> Unit): ComboBox<EDiferencaNum> {
  return comboBox(label) {
    this.setItems(EDiferencaNum.values().toList())
    this.setItemLabelGenerator {
      it.descricao
    }
    this.isAllowCustomValue = false
    this.isClearButtonVisible = false
    block()
  }
}

fun HasComponents.comboDiferencaStr(label: String, block: ComboBox<EDiferencaStr>.() -> Unit): ComboBox<EDiferencaStr> {
  return comboBox(label) {
    this.setItems(EDiferencaStr.values().toList())
    this.setItemLabelGenerator {
      it.descricao
    }
    this.isAllowCustomValue = false
    this.isClearButtonVisible = false
    block()
  }
}

fun Grid.Column<NfPrecEntrada>.marcaDiferenca(predicado: NfPrecEntrada.() -> Boolean) {
  this.setClassNameGenerator {
    if (it.predicado()) "marcaDiferenca" else null
  }
}

