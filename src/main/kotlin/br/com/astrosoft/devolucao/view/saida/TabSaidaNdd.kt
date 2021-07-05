package br.com.astrosoft.devolucao.view.saida

import br.com.astrosoft.devolucao.model.beans.FiltroNotaSaidaNdd
import br.com.astrosoft.devolucao.model.beans.Loja
import br.com.astrosoft.devolucao.model.beans.NotaSaidaNdd
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.saida.columns.SaidaNddColumns.chaveNotaSaida
import br.com.astrosoft.devolucao.view.saida.columns.SaidaNddColumns.codigoClienteNotaSaida
import br.com.astrosoft.devolucao.view.saida.columns.SaidaNddColumns.dataNotaSaida
import br.com.astrosoft.devolucao.view.saida.columns.SaidaNddColumns.lojaNotaSaida
import br.com.astrosoft.devolucao.view.saida.columns.SaidaNddColumns.nomeClienteNotaSaida
import br.com.astrosoft.devolucao.view.saida.columns.SaidaNddColumns.notaNotaSaida
import br.com.astrosoft.devolucao.view.saida.columns.SaidaNddColumns.pedidoNotaSaida
import br.com.astrosoft.devolucao.view.saida.columns.SaidaNddColumns.valorTotalNotaSaida
import br.com.astrosoft.devolucao.viewmodel.saida.ITabSaidaNddViewModel
import br.com.astrosoft.devolucao.viewmodel.saida.TabSaidaNddViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnButton
import br.com.astrosoft.framework.view.localePtBr
import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.provider.SortDirection
import java.time.LocalDate

class TabSaidaNdd(val viewModel: TabSaidaNddViewModel) : TabPanelGrid<NotaSaidaNdd>(NotaSaidaNdd::class),
        ITabSaidaNddViewModel {
  private lateinit var cmbLoja: IntegerField
  private lateinit var edtNota: TextField
  private lateinit var edtDataI: DatePicker
  private lateinit var edtDataF: DatePicker
  private lateinit var edtCodigo: IntegerField
  private lateinit var edtNome: TextField

  override fun filtro(): FiltroNotaSaidaNdd {
    return FiltroNotaSaidaNdd(loja = cmbLoja.value,
                              nota = edtNota.value,
                              codigoCliente = edtCodigo.value,
                              nomeCliente = edtNome.value,
                              dataI = edtDataI.value,
                              dataF = edtDataF.value)
  }

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.notaSaida == true
  }

  override val label: String
    get() = "Nota de saída"

  override fun updateComponent() {
    viewModel.updateView()
  }

  override fun HorizontalLayout.toolBarConfig() {
    cmbLoja = integerField("Loja") {
      value = 4
      addValueChangeListener {
        viewModel.updateView()
      }
    }
    edtNota = textField("Nota") {
      addValueChangeListener {
        viewModel.updateView()
      }
    }
    edtDataI = datePicker("Data Inicial") {
      localePtBr()
      value = LocalDate.now().minusDays(60)
      addValueChangeListener {
        viewModel.updateView()
      }
    }
    edtDataF = datePicker("Data Final") {
      localePtBr()
      value = LocalDate.now()
      addValueChangeListener {
        viewModel.updateView()
      }
    }
    edtCodigo = integerField("Código Cliente") {
      addValueChangeListener {
        viewModel.updateView()
      }
    }
    edtNome = textField("Nome Cliente") {
      addValueChangeListener {
        viewModel.updateView()
      }
    }
  }

  override fun Grid<NotaSaidaNdd>.gridPanel() {
    setSelectionMode(Grid.SelectionMode.MULTI)
    addColumnButton(VaadinIcon.FILE_TABLE, "Notas", "Notas") { nota ->
      viewModel.createDanfe(nota)
    }

    lojaNotaSaida()
    notaNotaSaida()
    dataNotaSaida()
    pedidoNotaSaida()
    codigoClienteNotaSaida()
    chaveNotaSaida()
    nomeClienteNotaSaida()
    val totalCol = valorTotalNotaSaida()

    this.dataProvider.addDataProviderListener {
      val totalPedido = listBeans().sumOf { it.valor }.format()
      totalCol.setFooter(Html("<b><font size=4>Total R$ &nbsp;&nbsp;&nbsp;&nbsp; ${totalPedido}</font></b>"))
    }

    sort(listOf(GridSortOrder(getColumnBy(NotaSaidaNdd::data), SortDirection.ASCENDING)))
  }
}