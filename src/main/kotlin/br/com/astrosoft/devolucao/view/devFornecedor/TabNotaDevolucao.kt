package br.com.astrosoft.devolucao.view.devFornecedor

import br.com.astrosoft.devolucao.model.beans.NotaDevolucao
import br.com.astrosoft.devolucao.view.notaDataNota
import br.com.astrosoft.devolucao.view.notaDataPedido
import br.com.astrosoft.devolucao.view.notaFornecedor
import br.com.astrosoft.devolucao.view.notaLoja
import br.com.astrosoft.devolucao.view.notaNota
import br.com.astrosoft.devolucao.view.notaPedido
import br.com.astrosoft.devolucao.view.notaRepresentante
import br.com.astrosoft.devolucao.viewmodel.devolucao.INotaDevolucao
import br.com.astrosoft.devolucao.viewmodel.devolucao.NotaDevolucaoViewModel
import br.com.astrosoft.framework.view.TabPanelGrid
import com.github.mvysny.karibudsl.v10.button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode
import com.vaadin.flow.component.icon.VaadinIcon.PRINT
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import kotlin.reflect.KClass

class TabNotaDevolucao(val viewModel: NotaDevolucaoViewModel): TabPanelGrid<NotaDevolucao>(), INotaDevolucao {
  override fun classPanel(): KClass<NotaDevolucao> = NotaDevolucao::class
  
  override fun HorizontalLayout.toolBarConfig() {
    button("Imprimir") {
      icon = PRINT.create()
      addClickListener {
        viewModel.imprimirNotaDevolucao()
      }
    }
  }
  
  override fun Grid<NotaDevolucao>.gridPanel() {
    setSelectionMode(SelectionMode.MULTI)
    notaLoja()
    notaPedido()
    notaDataPedido()
    notaNota()
    notaDataNota()
    notaFornecedor()
    notaRepresentante()
  }
  
  override val label: String
    get() = "Devolução de Fornecedor"
  
  override fun updateComponent() {
    viewModel.updateGridNotaDevolucao()
  }
}