package br.com.astrosoft.devolucao.view.saida

import br.com.astrosoft.devolucao.model.beans.FiltroNotaSaidaNdd
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
import br.com.astrosoft.framework.model.Config
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.localePtBr
import com.github.mvysny.karibudsl.v10.datePicker
import com.github.mvysny.karibudsl.v10.integerField
import com.github.mvysny.karibudsl.v10.textField
import com.github.mvysny.kaributools.getColumnBy
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.provider.SortDirection
import com.vaadin.flow.data.value.ValueChangeMode
import org.claspina.confirmdialog.ButtonOption
import org.claspina.confirmdialog.ConfirmDialog
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

  override fun limparFiltro() {
    val user = Config.user as? UserSaci
    cmbLoja.value = user?.storeno ?: 4
    if (cmbLoja.value == 0) cmbLoja.value = 4
    edtNota.value = ""
    edtCodigo.value = 0
    edtNome.value = "" //edtDataI.value = if(edtDataF.value == null) edtDataI.value else null
    //edtDataF.value = if(edtDataI.value == null) null else edtDataF.value
  }

  override fun setDateNow() {
    edtDataI.value = LocalDate.now()
    edtDataF.value = LocalDate.now()
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
    val user = Config.user as? UserSaci
    cmbLoja = integerField("Loja") {
      value = user?.storeno ?: 4
      if (value == 0) value = 4
      isReadOnly = user?.admin == false
      this.valueChangeMode = ValueChangeMode.LAZY
      this.valueChangeTimeout = 2000
      this.addValueChangeListener {
        if (it.isFromClient) viewModel.updateView()
      }
    }
    edtNota = textField("Nota") {
      this.valueChangeMode = ValueChangeMode.LAZY
      this.valueChangeTimeout = 2000
      this.addValueChangeListener {
        if (it.isFromClient) viewModel.updateView()
      }
    }
    edtDataI = datePicker("Data Inicial") {
      localePtBr()
      isClearButtonVisible = true
      value = null
      this.addValueChangeListener {
        if (it.isFromClient) viewModel.updateView()
      }
    }
    edtDataF = datePicker("Data Final") {
      localePtBr()
      isClearButtonVisible = true
      value = null
      this.addValueChangeListener {
        if (it.isFromClient) viewModel.updateView()
      }
    }
    edtCodigo = integerField("Código Cliente") {
      this.valueChangeMode = ValueChangeMode.LAZY
      this.valueChangeTimeout = 2000
      this.addValueChangeListener {
        if (it.isFromClient) viewModel.updateView()
      }
    }
    edtNome = textField("Nome Cliente") {
      this.valueChangeMode = ValueChangeMode.LAZY
      this.valueChangeTimeout = 2000
      this.addValueChangeListener {
        if (it.isFromClient) viewModel.updateView()
      }
    }
  }

  fun showWarning(msg: String) {
    ConfirmDialog.createWarning().withCaption("Aviso").withMessage(msg).open()
  }

  override fun Grid<NotaSaidaNdd>.gridPanel() {
    setSelectionMode(Grid.SelectionMode.MULTI)
    lojaNotaSaida()
    notaNotaSaida()
    dataNotaSaida()
    pedidoNotaSaida()
    codigoClienteNotaSaida()
    chaveNotaSaida()
    nomeClienteNotaSaida()

    this.setClassNameGenerator { nota ->
      if (nota.reimpresao() != null) "marcaRed" else null
    }

    val totalCol = valorTotalNotaSaida()

    this.dataProvider.addDataProviderListener {
      val totalPedido = listBeans().sumOf { it.valor }.format()
      totalCol.setFooter(Html("<b><font size=4>Total R$ &nbsp;&nbsp;&nbsp;&nbsp; ${totalPedido}</font></b>"))
    }

    sort(listOf(GridSortOrder(getColumnBy(NotaSaidaNdd::data), SortDirection.DESCENDING)))
  }
}

fun disableButton(bool: Boolean): ButtonOption {
  return if (bool) ButtonOption.disable()
  else object : ButtonOption() {
    override fun apply(confirmDialog: ConfirmDialog?, button: Button?) {
      button?.isEnabled = true
    }
  }
}