package br.com.astrosoft.devolucao.view.devFornecedor

import br.com.astrosoft.devolucao.model.beans.NotaDevolucao
import br.com.astrosoft.devolucao.model.beans.Representante
import br.com.astrosoft.devolucao.view.notaDataNota
import br.com.astrosoft.devolucao.view.notaDataPedido
import br.com.astrosoft.devolucao.view.notaEmail
import br.com.astrosoft.devolucao.view.notaFornecedor
import br.com.astrosoft.devolucao.view.notaLoja
import br.com.astrosoft.devolucao.view.notaNota
import br.com.astrosoft.devolucao.view.notaPedido
import br.com.astrosoft.devolucao.view.notaRepresentante
import br.com.astrosoft.devolucao.view.notaTelefone
import br.com.astrosoft.devolucao.view.reports.RelatorioNotaDevolucao
import br.com.astrosoft.devolucao.viewmodel.devolucao.INotaDevolucao
import br.com.astrosoft.devolucao.viewmodel.devolucao.NotaDevolucaoViewModel
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.SubWindowPDF
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnButton
import com.github.mvysny.karibudsl.v10.button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode
import com.vaadin.flow.component.grid.GridVariant.LUMO_COMPACT
import com.vaadin.flow.component.icon.VaadinIcon
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
    addColumnButton(VaadinIcon.TABLE, "Representantes", execButton = {nota ->
      showDialogRepresentante(nota)
    }, block = {
      setHeader("Representante")
    })
    notaLoja()
    notaPedido()
    notaDataPedido()
    notaNota()
    notaDataNota()
    notaFornecedor()
  }
  
  override val label: String
    get() = "Devolução de Fornecedor"
  
  override fun updateComponent() {
    viewModel.updateGridNotaDevolucao()
  }
  
  override fun itensSelecionados(): List<NotaDevolucao> {
    return itensSelecionado()
  }
  
  override fun imprimeSelecionados(itens: List<NotaDevolucao>) {
    val report = RelatorioNotaDevolucao.processaRelatorio(itens)
    val chave = "DevReport"
    SubWindowPDF(chave, report).open()
  }
  
  private fun showDialogRepresentante(nota: NotaDevolucao?) {
    nota ?: return
    val listRepresentantes = nota.listRepresentantes()
    val form = SubWindowForm("${nota.nota}") {
      createGridRepresentantes(listRepresentantes)
    }
    form.open()
  }
  
  private fun createGridRepresentantes(listRepresentantes: List<Representante>): Grid<Representante> {
    val gridDetail = Grid(Representante::class.java, false)
    return gridDetail.apply {
      addThemeVariants(LUMO_COMPACT)
      isMultiSort = false
      setItems(listRepresentantes)
      //
      notaRepresentante()
      notaTelefone()
      notaEmail()
    }
  }
}