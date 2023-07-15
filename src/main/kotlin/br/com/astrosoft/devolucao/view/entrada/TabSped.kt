package br.com.astrosoft.devolucao.view.entrada

import br.com.astrosoft.devolucao.model.beans.*
import br.com.astrosoft.devolucao.viewmodel.entrada.ITabSpedViewModel
import br.com.astrosoft.devolucao.viewmodel.entrada.TabSpedViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.ITabPanel
import br.com.astrosoft.framework.view.localePtBr
import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextField
import java.time.LocalDate

@CssImport("./styles/gridTotal.css", themeFor = "vaadin-grid")
class TabSped(val viewModel: TabSpedViewModel) : ITabSpedViewModel, ITabPanel {
  private lateinit var edtProduto: TextField
  private lateinit var edtNota: TextField
  private lateinit var edtNi: IntegerField
  private lateinit var edtFornecedorCad: IntegerField
  private lateinit var edtFornecedorNota: IntegerField
  private lateinit var edtDataF: DatePicker
  private lateinit var edtDataI: DatePicker
  private lateinit var edtLoja: ComboBox<Loja>
  private lateinit var edtUlmNota: Checkbox
  private lateinit var edtRotulo: TextField
  private lateinit var edtCaracter: TextField
  private val lojas: List<Loja> = viewModel.findLojas() + Loja.lojaZero

  override fun setFiltro(filtro: FiltroRelatorio) {
    edtLoja.value = lojas.firstOrNull { it.no == filtro.storeno }
    edtDataI.value = filtro.di
    edtDataF.value = filtro.df
    edtFornecedorCad.value = if (filtro.mfno == 0) null else filtro.mfno
    edtFornecedorNota.value = if (filtro.vendno == 0) null else filtro.vendno
    edtNi.value = if (filtro.ni == 0) null else filtro.ni
    edtNota.value = filtro.nf
    edtProduto.value = filtro.prd
    edtUlmNota.value = filtro.ultimaNota
    edtRotulo.value = filtro.rotulo
    edtCaracter.value = filtro.caraterInicial
  }

  override fun getFiltro(): FiltroRelatorio {
    return FiltroRelatorio(
      storeno = edtLoja.value?.no ?: 0,
      di = edtDataI.value ?: LocalDate.now(),
      df = edtDataF.value ?: LocalDate.now(),
      vendno = edtFornecedorNota.value ?: 0,
      mfno = edtFornecedorCad.value ?: 0,
      ni = edtNi.value ?: 0,
      nf = edtNota.value ?: "",
      prd = edtProduto.value ?: "",
      cst = EDiferencaStr.T,
      icms = EDiferencaStr.T,
      ipi = EDiferencaStr.T,
      mva = EDiferencaStr.T,
      ncm = EDiferencaStr.T,
      barcode = EDiferencaStr.T,
      refPrd = EDiferencaStr.T,
      frete = EDiferencaNum.T,
      fretePer = EDiferencaNum.T,
      preco = EDiferencaNum.T,
      ultimaNota = edtUlmNota.value ?: false,
      rotulo = edtRotulo.value ?: "",
      caraterInicial = edtCaracter.value ?: "",
      comGrade = true,
      pesquisa = "",
      listaProdutos = "",
    )
  }

  override fun openRelatorio() {
    DlgRelatorioSped(viewModel, getFiltro()).show()
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
      edtUlmNota = checkBox("Últimas Nota")
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
      edtRotulo = textField("Rótulo")
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
    get() = "Sped"

  override fun updateComponent() {
  }
}
