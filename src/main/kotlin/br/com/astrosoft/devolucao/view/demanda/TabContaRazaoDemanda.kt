package br.com.astrosoft.devolucao.view.demanda

import br.com.astrosoft.devolucao.model.beans.ContaRazao
import br.com.astrosoft.devolucao.model.beans.FiltroContaRazaoNota
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaContaRazao
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaContaRazaoNotas
import br.com.astrosoft.devolucao.viewmodel.demanda.ITabContaRazaoDemanda
import br.com.astrosoft.devolucao.viewmodel.demanda.TabContaRazaoDemandaViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.*
import br.com.astrosoft.framework.view.vaadin.columnGrid
import com.github.mvysny.karibudsl.v10.datePicker
import com.github.mvysny.karibudsl.v10.textField
import com.github.mvysny.kaributools.asc
import com.github.mvysny.kaributools.getColumnBy
import com.github.mvysny.kaributools.sort
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode
import java.time.LocalDate

class TabContaRazaoDemanda(private val viewModel: TabContaRazaoDemandaViewModel) :
  TabPanelGrid<ContaRazao>(ContaRazao::class), ITabContaRazaoDemanda {
  private lateinit var edtQuery: TextField
  private lateinit var edtDataInicial: DatePicker
  private lateinit var edtDataFinal: DatePicker

  override fun HorizontalLayout.toolBarConfig() {
    edtQuery = textField("Filtro") {
      width = "300px"
      valueChangeMode = ValueChangeMode.LAZY
      valueChangeTimeout = 2000
      addValueChangeListener {
        viewModel.updateView()
      }
    }

    edtDataInicial = datePicker("Data Inicial") {
      value = LocalDate.now().withDayOfMonth(1)
      this.localePtBr()
      addValueChangeListener {
        viewModel.updateView()
      }
    }

    edtDataFinal = datePicker("Data Final") {
      value = LocalDate.now()
      this.localePtBr()
      addValueChangeListener {
        viewModel.updateView()
      }
    }

    this.lazyDownloadButtonXlsx("Planilha", "contaRazão") {
      val list = itensSelecionados()
      PlanilhaContaRazao().grava(list)
    }
  }

  override fun Grid<ContaRazao>.gridPanel() {
    setSelectionMode(Grid.SelectionMode.MULTI)
    addColumnSeq("Item")

    addColumnButton(iconButton = VaadinIcon.MODAL_LIST, tooltip = "Nota", header = "Nota") { fornecedor ->
      viewModel.showNotas(fornecedor)
    }

    columnGrid(ContaRazao::numeroConta, "Número")
    columnGrid(ContaRazao::descricaoConta, "Descrição", isExpand = true)
    columnGrid(ContaRazao::quantNotas, "Quantidade"){
      this.setFooter(Html("<b><font size=4>Total</font></b>"))
    }
    columnGrid(ContaRazao::valorTotal, "Total")

    this.dataProvider.addDataProviderListener {
      val totalPedido = listBeans().sumOf { it.valorTotal }.format()
      val totalCol = getColumnBy(ContaRazao::valorTotal)
      totalCol.setFooter(Html("<b><font size=4>${totalPedido}</font></b>"))
    }


    this.sort(ContaRazao::numeroConta.asc)
  }

  override fun filtro(): FiltroContaRazaoNota {
    return FiltroContaRazaoNota(
      query = edtQuery.value ?: "",
      dataInicial = edtDataInicial.value,
      dataFinal = edtDataFinal.value
    )
  }

  override fun showNotas(fornecedor: ContaRazao) {
    DlgContaRazaoNota(viewModel, fornecedor).showDialogNota()
  }

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.pedidoContaRazao == true
  }

  override val label: String
    get() = "Conta Razão"

  override fun updateComponent() {
    viewModel.updateView()
  }
}
