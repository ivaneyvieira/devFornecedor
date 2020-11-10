package br.com.astrosoft.devolucao.view.devFornecedor

import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.model.beans.ProdutosNotaSaida
import br.com.astrosoft.devolucao.model.beans.Representante
import br.com.astrosoft.devolucao.view.notaCelular
import br.com.astrosoft.devolucao.view.notaDataNota
import br.com.astrosoft.devolucao.view.notaDataPedido
import br.com.astrosoft.devolucao.view.notaEmail
import br.com.astrosoft.devolucao.view.notaFatura
import br.com.astrosoft.devolucao.view.notaFornecedor
import br.com.astrosoft.devolucao.view.notaLoja
import br.com.astrosoft.devolucao.view.notaNota
import br.com.astrosoft.devolucao.view.notaPedido
import br.com.astrosoft.devolucao.view.notaRepresentante
import br.com.astrosoft.devolucao.view.notaTelefone
import br.com.astrosoft.devolucao.view.produtoCodigo
import br.com.astrosoft.devolucao.view.produtoDescricao
import br.com.astrosoft.devolucao.view.produtoGrade
import br.com.astrosoft.devolucao.view.produtoQtde
import br.com.astrosoft.devolucao.view.reports.RelatorioNotaDevolucao
import br.com.astrosoft.devolucao.viewmodel.devolucao.INotaDevolucao
import br.com.astrosoft.devolucao.viewmodel.devolucao.INotaVenda
import br.com.astrosoft.devolucao.viewmodel.devolucao.NotaDevolucaoViewModel
import br.com.astrosoft.devolucao.viewmodel.devolucao.NotaVendaViewModel
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.SubWindowPDF
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnButton
import br.com.astrosoft.framework.view.localePtBr
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.datePicker
import com.github.mvysny.karibudsl.v10.isExpand
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode
import com.vaadin.flow.component.grid.GridVariant.LUMO_COMPACT
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.icon.VaadinIcon.EDIT
import com.vaadin.flow.component.icon.VaadinIcon.PHONE_LANDLINE
import com.vaadin.flow.component.icon.VaadinIcon.PRINT
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode
import java.time.LocalDate
import kotlin.reflect.KClass

class TabNotaVenda(val viewModel: NotaVendaViewModel): TabPanelGrid<NotaSaida>(), INotaVenda {
  private lateinit var dataInicialEdt: DatePicker
  private lateinit var dataFinalEdt: DatePicker
  private lateinit var fornecedorEdt: TextField
  private lateinit var notaEdt: TextField

  override fun classPanel(): KClass<NotaSaida> = NotaSaida::class
  
  override fun HorizontalLayout.toolBarConfig() {
    dataInicialEdt = datePicker("Data Incial"){
      localePtBr()
      isClearButtonVisible = true
      addValueChangeListener {
        updateComponent()
      }
    }
    dataFinalEdt = datePicker("Data Final"){
      localePtBr()
      isClearButtonVisible = true
      addValueChangeListener {
        updateComponent()
      }
    }
    fornecedorEdt = textField("Fornecedor") {
      valueChangeMode = ValueChangeMode.TIMEOUT
      addValueChangeListener {
        updateComponent()
      }
    }
    notaEdt = textField("Nota") {
      valueChangeMode = ValueChangeMode.TIMEOUT
      addValueChangeListener {
        updateComponent()
      }
    }
  }
  
  override fun Grid<NotaSaida>.gridPanel() {
    setSelectionMode(SelectionMode.MULTI)
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
    notaLoja()
    notaPedido()
    notaDataPedido()
    notaNota()
    notaFatura()
    notaDataNota()
    notaFornecedor()
  }
  
  override val label: String
    get() = "Notas série 1"
  
  override fun updateComponent() {
    viewModel.updateGridNotaVenda()
  }
  
  override fun itensSelecionados(): List<NotaSaida> {
    return itensSelecionado()
  }
  
  override fun imprimeSelecionados(itens: List<NotaSaida>) {
    val report = RelatorioNotaDevolucao.processaRelatorio(itens)
    val chave = "DevReport"
    SubWindowPDF(chave, report).open()
  }
  
  override fun editRmk(nota: NotaSaida, save: (NotaSaida) -> Unit) {
    val form = SubWindowForm("PROCESSO INTERNO: ${nota.nota}|DEV FORNECEDOR: ${nota.fornecedor}",
                             toolBar = {window ->
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
  
  override fun dataInicial(): LocalDate? = dataInicialEdt.value
  override fun dataFinal(): LocalDate? = dataFinalEdt.value
  override fun fornecedor(): String = fornecedorEdt.value?.trim() ?: ""
  override fun nota(): String = notaEdt.value?.trim() ?: ""
  
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