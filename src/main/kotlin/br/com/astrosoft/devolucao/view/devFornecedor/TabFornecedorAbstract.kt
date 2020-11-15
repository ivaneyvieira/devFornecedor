package br.com.astrosoft.devolucao.view.devFornecedor

import br.com.astrosoft.devolucao.model.beans.Fornecedor
import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.model.beans.ProdutosNotaSaida
import br.com.astrosoft.devolucao.model.beans.Representante
import br.com.astrosoft.devolucao.view.notaCelular
import br.com.astrosoft.devolucao.view.notaEmail
import br.com.astrosoft.devolucao.view.notaFornecedorCodigo
import br.com.astrosoft.devolucao.view.notaFornecedorCodigoCliente
import br.com.astrosoft.devolucao.view.notaRepresentante
import br.com.astrosoft.devolucao.view.notaTelefone
import br.com.astrosoft.devolucao.view.produtoCodigo
import br.com.astrosoft.devolucao.view.produtoDescricao
import br.com.astrosoft.devolucao.view.produtoGrade
import br.com.astrosoft.devolucao.view.produtoQtde
import br.com.astrosoft.devolucao.view.reports.RelatorioNotaDevolucao
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.SubWindowPDF
import br.com.astrosoft.framework.view.TabPanelGrid
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.isExpand
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode
import com.vaadin.flow.component.grid.GridVariant.LUMO_COMPACT
import com.vaadin.flow.component.icon.VaadinIcon.CHECK
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.data.value.ValueChangeMode
import kotlin.reflect.KClass

abstract class TabFornecedorAbstract: TabPanelGrid<Fornecedor>() {
  override fun classPanel(): KClass<Fornecedor> = Fornecedor::class
  
  override fun HorizontalLayout.toolBarConfig() {
  }
  
  override fun Grid<Fornecedor>.gridPanel() {
    setSelectionMode(SelectionMode.MULTI)
    /*
    addColumnButton(PRINT, "Impressão", "Imp") {nota ->
      //showDialogImpressao(nota)
      viewModel.imprimirNotaVenda(nota)
    }
    addColumnButton(EDIT, "Editor", "Edt") {nota ->
      //showDialogImpressao(nota)
      viewModel.editRmk(nota)
    }
    addColumnButton(PHONE_LANDLINE, "Representantes", "Rep") {nota ->
      showDialogRepresentante(nota)
    }
    
     */
    notaFornecedorCodigoCliente()
    notaFornecedorCodigoCliente()
    notaFornecedorCodigo()
  }
  
  fun itensSelecionados(): List<Fornecedor> {
    return itensSelecionado()
  }
  
  fun imprimeSelecionados(itens: List<NotaSaida>) {
    val report = RelatorioNotaDevolucao.processaRelatorio(itens)
    val chave = "DevReport"
    SubWindowPDF(chave, report).open()
  }
  
  fun editRmk(nota: NotaSaida, save: (NotaSaida) -> Unit) {
    val form = SubWindowForm("PROCESSO INTERNO: ${nota.nota}|DEV FORNECEDOR: ${nota.fornecedor}",
                             toolBar = {window ->
                               button("Salva") {
                                 icon = CHECK.create()
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
      this.style.set("overflow-y", "auto");
      this.isExpand = true
      this.focus()
      this.value = nota.rmk
      valueChangeMode = ValueChangeMode.TIMEOUT
      addValueChangeListener {
        val text = it.value
        nota.rmk = text
      }
    }
  }
  
  private fun showDialogRepresentante(nota: NotaSaida?) {
    nota ?: return
    val listRepresentantes = nota.listRepresentantes()
    val form = SubWindowForm("PROCESSO INTERNO: ${nota.nota}|DEV FORNECEDOR: ${nota.fornecedor}") {
      createGridRepresentantes(listRepresentantes)
    }
    form.open()
  }
  
  private fun showDialogImpressao(nota: NotaSaida?) {
    nota ?: return
    val listProdutos = nota.listaProdutos()
    val form = SubWindowForm("PROCESSO INTERNO: ${nota.nota}|DEV FORNECEDOR: ${nota.fornecedor}") {
      createGridImpressao(listProdutos)
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
      notaCelular()
      notaEmail()
    }
  }
  
  private fun createGridImpressao(listProdutos: List<ProdutosNotaSaida>): Grid<ProdutosNotaSaida> {
    val gridDetail = Grid(ProdutosNotaSaida::class.java, false)
    return gridDetail.apply {
      addThemeVariants(LUMO_COMPACT)
      isMultiSort = false
      setItems(listProdutos)
      //
      produtoCodigo()
      produtoDescricao()
      produtoGrade()
      produtoQtde()
    }
  }
}