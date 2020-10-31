package br.com.astrosoft.devolucao.view.devFornecedor

import br.com.astrosoft.devolucao.model.beans.NotaDevolucao
import br.com.astrosoft.devolucao.viewmodel.devolucao.INotaDevolucao
import br.com.astrosoft.devolucao.viewmodel.devolucao.NotaDevolucaoViewModel
import br.com.astrosoft.framework.view.TabPanelGrid
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import kotlin.reflect.KClass

class TabNotaDevolucao(val viewModel: NotaDevolucaoViewModel): TabPanelGrid<NotaDevolucao>(), INotaDevolucao {
  override fun classPanel(): KClass<NotaDevolucao> {
    TODO("Not yet implemented")
  }
  
  override fun HorizontalLayout.toolBarConfig() {
    TODO("Not yet implemented")
  }
  
  override fun Grid<NotaDevolucao>.gridPanel() {
    TODO("Not yet implemented")
  }
  
  override val label: String
    get() = TODO("Not yet implemented")
  
  override fun updateComponent() {
    TODO("Not yet implemented")
  }
}