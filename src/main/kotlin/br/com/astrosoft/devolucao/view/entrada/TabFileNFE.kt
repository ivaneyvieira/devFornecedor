package br.com.astrosoft.devolucao.view.entrada

import br.com.astrosoft.devolucao.model.beans.FiltroNotaEntradaFileXML
import br.com.astrosoft.devolucao.model.beans.NotaEntradaFileXML
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaNddViewColumns.nfeFileCNPJ
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaNddViewColumns.nfeFileChave
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaNddViewColumns.nfeFileData
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaNddViewColumns.nfeFileNomeFornecedor
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaNddViewColumns.nfeFileNotaSaci
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaNddViewColumns.nfeFileTotal
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaNddViewColumns.nfeFileValorProduto
import br.com.astrosoft.devolucao.viewmodel.entrada.ITabFileNFEViewModel
import br.com.astrosoft.devolucao.viewmodel.entrada.TabFileNFEViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.lazyDownloadButton
import br.com.astrosoft.framework.view.localePtBr
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.datePicker
import com.github.mvysny.karibudsl.v10.integerField
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode
import java.time.LocalDate

@CssImport("./styles/gridTotal.css", themeFor = "vaadin-grid")
class TabFileNFE(val viewModel: TabFileNFEViewModel) : ITabFileNFEViewModel,
        TabPanelGrid<NotaEntradaFileXML>(NotaEntradaFileXML::class) {
  private lateinit var edtNota: IntegerField
  private lateinit var edtFornecedorNota: TextField
  private lateinit var edtDataF: DatePicker
  private lateinit var edtDataI: DatePicker
  private lateinit var edtCNPJ: TextField

  override fun getFiltro(): FiltroNotaEntradaFileXML {
    return FiltroNotaEntradaFileXML(
      dataInicial = edtDataI.value ?: LocalDate.now(),
      dataFinal = edtDataF.value ?: LocalDate.now(),
      numero = edtNota.value ?: 0,
      cnpj = edtCNPJ.value ?: "",
      fornecedor = edtFornecedorNota.value ?: "",
                                   )
  }

  override fun updateList(list: List<NotaEntradaFileXML>) {
    updateGrid(list)
  }

  override fun HorizontalLayout.toolBarConfig() {
    edtDataI = datePicker("Data Inicial") {
      localePtBr()
      addValueChangeListener {
        viewModel.updateView()
      }
    }
    edtDataF = datePicker("Data Final") {
      localePtBr()
      addValueChangeListener {
        viewModel.updateView()
      }
    }
    edtCNPJ = textField("CNPJ") {
      valueChangeMode = ValueChangeMode.LAZY
      this.valueChangeTimeout = 1000
      addValueChangeListener {
        viewModel.updateView()
      }
    }
    edtFornecedorNota = textField("Fornecedor Nota") {
      valueChangeMode = ValueChangeMode.LAZY
      this.valueChangeTimeout = 1000
      addValueChangeListener {
        viewModel.updateView()
      }
    }
    edtNota = integerField("Nota Fiscal") {
      valueChangeMode = ValueChangeMode.LAZY
      this.valueChangeTimeout = 1000
      addValueChangeListener {
        viewModel.updateView()
      }
    }

    this.lazyDownloadButton(text = "XML", icon = FontAwesome.Solid.FILE.create(), fileName = {
      "dadosXML${System.nanoTime()}.zip"
    }) {
      val notas = itensSelecionados()
      viewModel.zipXml(notas)
    }
  }

  override fun Grid<NotaEntradaFileXML>.gridPanel() {
    setSelectionMode(Grid.SelectionMode.MULTI)
    nfeFileNotaSaci()
    nfeFileCNPJ()
    nfeFileData()
    nfeFileNomeFornecedor()
    nfeFileChave()
    nfeFileValorProduto()
    nfeFileTotal()
  }

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.entradaFileNFE == true
  }

  override val label: String
    get() = "XML"

  override fun updateComponent() {
    viewModel.updateView()
  }
}
