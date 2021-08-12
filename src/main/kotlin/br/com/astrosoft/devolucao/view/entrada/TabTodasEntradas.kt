package br.com.astrosoft.devolucao.view.entrada

import br.com.astrosoft.devolucao.model.beans.FiltroNotaEntradaQuery
import br.com.astrosoft.devolucao.model.beans.Loja
import br.com.astrosoft.devolucao.model.beans.NotaEntradaQuery
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.entrada.ITabTodasEntradasViewModel
import br.com.astrosoft.devolucao.viewmodel.entrada.TabTodasEntradasViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.ITabPanel
import br.com.astrosoft.framework.view.localePtBr
import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextField
import java.time.LocalDate

@CssImport("./styles/gridTotal.css", themeFor = "vaadin-grid")
class TabTodasEntradas(val viewModel: TabTodasEntradasViewModel) : ITabTodasEntradasViewModel, ITabPanel {
  private lateinit var edtProduto: TextField
  private lateinit var edtNota: TextField
  private lateinit var edtNi: IntegerField
  private lateinit var edtFornecedorCad: IntegerField
  private lateinit var edtFornecedorNota: IntegerField
  private lateinit var edtDataF: DatePicker
  private lateinit var edtDataI: DatePicker
  private lateinit var edtLoja: ComboBox<Loja>
  private lateinit var edtCaracter: TextField
  private val lojas: List<Loja> = viewModel.findLojas() + Loja(0, "Todas", "")

  override fun setFiltro(filtro: FiltroNotaEntradaQuery) {
    edtLoja.value = lojas.firstOrNull { it.no == filtro.storeno }
    edtDataI.value = filtro.di
    edtDataF.value = filtro.df
    edtFornecedorCad.value = if (filtro.mfno == 0) null else filtro.mfno
    edtFornecedorNota.value = if (filtro.vendno == 0) null else filtro.vendno
    edtNi.value = if (filtro.ni == 0) null else filtro.ni
    edtNota.value = filtro.nf
    edtProduto.value = filtro.prd
    edtCaracter.value = filtro.caraterInicial
  }

  override fun getFiltro(): FiltroNotaEntradaQuery {
    return FiltroNotaEntradaQuery(storeno = edtLoja.value?.no ?: 0,
                                   di = edtDataI.value ?: LocalDate.now(),
                                   df = edtDataF.value ?: LocalDate.now(),
                                   vendno = edtFornecedorNota.value ?: 0,
                                   mfno = edtFornecedorCad.value ?: 0,
                                   ni = edtNi.value ?: 0,
                                   nf = edtNota.value ?: "",
                                   prd = edtProduto.value ?: "",
                                   caraterInicial = edtCaracter.value ?: "")
  }

  override fun openRelatorio(list: List<NotaEntradaQuery>) {
    DlgRelatorioTodasEntradas(viewModel, list).show()
  }

  override val createComponent = VerticalLayout().apply {
    horizontalLayout {
      edtLoja = comboBox("Loja") {
        setItems(lojas.sortedBy { it.no })
        setItemLabelGenerator {
          if (it == null) "Todas as lojas"
          else "${it.no} - ${it.sname}"
        }
        isAllowCustomValue = false
        isClearButtonVisible = true
      }
      edtDataI = datePicker("Data Inicial") {
        localePtBr()
      }
      edtDataF = datePicker("Data Final") {
        localePtBr()
      }
      edtCaracter = textField("Inibir Produto") {
        placeholder = "Separado por ,"
      }
    }
    horizontalLayout {
      edtFornecedorCad = integerField("Fornecedor Cad.")
      edtFornecedorNota = integerField("Fornecedor Nota")
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
    setFiltro(getFiltro())
  }

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.entradaNddNFPrec == true
  }

  override val label: String
    get() = "Entradas"

  override fun updateComponent() {
  }
}
