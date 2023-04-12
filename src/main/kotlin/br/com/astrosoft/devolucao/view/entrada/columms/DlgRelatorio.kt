package br.com.astrosoft.devolucao.view.entrada.columms

import br.com.astrosoft.devolucao.model.beans.EDiferencaNum
import br.com.astrosoft.devolucao.model.beans.EDiferencaStr
import br.com.astrosoft.devolucao.model.beans.NfPrecEntrada
import com.github.mvysny.karibudsl.v10.comboBox
import com.github.mvysny.karibudsl.v10.select
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.select.Select

fun HasComponents.comboDiferencaNum(label: String, block: Select<EDiferencaNum>.() -> Unit): Select<EDiferencaNum> {
  return select(label) {
    this.setItems(EDiferencaNum.values().toList())
    this.setItemLabelGenerator {
      it.descricao
    }
    block()
  }
}

fun HasComponents.comboDiferencaStr(label: String, block: Select<EDiferencaStr>.() -> Unit): Select<EDiferencaStr> {
  return select(label) {
    this.setItems(EDiferencaStr.values().toList())
    this.setItemLabelGenerator {
      it.descricao
    }
    block()
  }
}

fun Grid.Column<NfPrecEntrada>.marcaDiferenca(predicado: NfPrecEntrada.() -> Boolean) {
  this.setClassNameGenerator {
    if (it.predicado()) "marcaDiferenca" else null
  }
}

