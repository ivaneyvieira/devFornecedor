package br.com.astrosoft.devolucao.view.entrada

import br.com.astrosoft.devolucao.model.beans.EDiferenca.T
import br.com.astrosoft.devolucao.model.beans.FiltroUltimaNotaEntrada
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.entrada.ITabUltimasEntradasViewModel
import br.com.astrosoft.devolucao.viewmodel.entrada.TabUltimasEntradasViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.ITabPanel
import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextField
import java.time.LocalDate

@CssImport("./styles/gridTotal.css", themeFor = "vaadin-grid")
class TabUltimasEntradas(val viewModel: TabUltimasEntradasViewModel) : ITabUltimasEntradasViewModel, ITabPanel {
  private lateinit var edtProduto: TextField
  private lateinit var edtNota: TextField
  private lateinit var edtNi: IntegerField
  private lateinit var edtFornecedor: IntegerField
  private lateinit var edtDataF: DatePicker
  private lateinit var edtDataI: DatePicker
  private lateinit var edtLoja: IntegerField

  override fun setFIltro(filtro: FiltroUltimaNotaEntrada) {
    edtLoja.value = filtro.storeno
    edtDataI.value = filtro.di
    edtDataF.value = filtro.df
    edtFornecedor.value = filtro.vendno
    edtNi.value = filtro.ni
    edtNota.value = filtro.nf
    edtProduto.value = filtro.prd
  }

  override fun getFiltro(): FiltroUltimaNotaEntrada {
    return FiltroUltimaNotaEntrada(storeno = edtLoja.value ?: 0,
                                   di = edtDataI.value ?: LocalDate.now(),
                                   df = edtDataF.value ?: LocalDate.now(),
                                   vendno = edtFornecedor.value ?: 0,
                                   ni = edtNi.value ?: 0,
                                   nf = edtNota.value ?: "",
                                   prd = edtProduto.value ?: "",
                                   cst = T,
                                   icms = T,
                                   ipi = T,
                                   mva = T,
                                   ncm = T)
  }

  override fun openRelatorio() {
    DlgRelatorioUltimaCompra(viewModel, getFiltro()).show()
  }

  override val createComponent = VerticalLayout().apply {
    horizontalLayout {
      edtLoja = integerField("Loja")
      edtDataI = datePicker("Data Inicial")
      edtDataF = datePicker("Data Final")
    }
    horizontalLayout {
      edtFornecedor = integerField("Fornecedor")
      edtNi = integerField("NI")
      edtNota = textField("Nota Fiscal")
      edtProduto = textField("Produto")
    }
    br()
    button("Relatório") {
      icon = VaadinIcon.RECORDS.create()
      onLeftClick {
        viewModel.openDlgRelatorio()
      }
    }
    setFIltro(getFiltro())
  }

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.entradaNdd == true
  }

  override val label: String
    get() = "NF x Prec"

  override fun updateComponent() {
  }
}
