package br.com.astrosoft.devolucao.view.entrada.columms

import br.com.astrosoft.devolucao.model.beans.EDiferenca
import br.com.astrosoft.devolucao.model.beans.NfPrecEntrada
import com.github.mvysny.karibudsl.v10.comboBox
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.grid.Grid

fun HasComponents.comboDiferenca(label: String, block: ComboBox<EDiferenca>.() -> Unit): ComboBox<EDiferenca> {
  return comboBox(label) {
    this.setItems(EDiferenca.values().toList())
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

