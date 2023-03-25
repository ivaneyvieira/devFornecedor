package br.com.astrosoft.devolucao.view.entrada

import br.com.astrosoft.devolucao.model.beans.FiltroNotaEntradaXML
import br.com.astrosoft.devolucao.model.beans.Loja
import br.com.astrosoft.devolucao.model.beans.NotaEntradaXML
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaNddViewColumns.nfeFileCfop
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaNddViewColumns.nfeFileChave
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaNddViewColumns.nfeFileEmissao
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaNddViewColumns.nfeFileEntrada
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaNddViewColumns.nfeFileNomeFornecedor
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaNddViewColumns.nfeFileNotaForCad
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaNddViewColumns.nfeFileNotaForNot
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaNddViewColumns.nfeFileNotaLoja
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaNddViewColumns.nfeFileNotaNI
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaNddViewColumns.nfeFileNotaNumero
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaNddViewColumns.nfeFileTotal
import br.com.astrosoft.devolucao.view.entrada.columms.NotaEntradaNddViewColumns.nfeFileValorProduto
import br.com.astrosoft.devolucao.viewmodel.entrada.ITabFileNFEViewModel
import br.com.astrosoft.devolucao.viewmodel.entrada.TabFileNFEViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.*
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome
import com.github.mvysny.karibudsl.v10.datePicker
import com.github.mvysny.karibudsl.v10.integerField
import com.github.mvysny.karibudsl.v10.select
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode
import java.time.LocalDate

@CssImport("./styles/gridTotal.css", themeFor = "vaadin-grid")
class TabFileNFE(val viewModel: TabFileNFEViewModel) : ITabFileNFEViewModel,
        TabPanelGrid<NotaEntradaXML>(NotaEntradaXML::class) {
  private lateinit var edtNota: IntegerField
  private lateinit var edtFornecedorNota: TextField
  private lateinit var edtQuery: TextField
  private lateinit var edtDataF: DatePicker
  private lateinit var edtDataI: DatePicker
  private lateinit var edtCNPJ: TextField
  private lateinit var cmbLoja: Select<Loja>

  override fun getFiltro(): FiltroNotaEntradaXML {
    return FiltroNotaEntradaXML(
      loja = cmbLoja.value,
      dataInicial = edtDataI.value ?: LocalDate.now(),
      dataFinal = edtDataF.value ?: LocalDate.now(),
      numero = edtNota.value ?: 0,
      cnpj = edtCNPJ.value ?: "",
      fornecedor = edtFornecedorNota.value ?: "",
      query = edtQuery.value ?: "",
                                   )
  }

  override fun updateList(list: List<NotaEntradaXML>) {
    updateGrid(list)
  }

  override fun HorizontalLayout.toolBarConfig() {
    cmbLoja = select("Loja") {
      val lojas = Loja.allLojas() + Loja.lojaZero
      setItems(lojas.sortedBy { it.no })
      value = Loja.lojaZero
      this.width = "100px"
      setItemLabelGenerator {
        it.descricao
      }
      addValueChangeListener {
        viewModel.updateViewBD()
      }
    }
    edtQuery = textField("Pesquisa") {
      valueChangeMode = ValueChangeMode.LAZY
      this.valueChangeTimeout = 1000
      addValueChangeListener {
        viewModel.updateViewLocal()
      }
    }
    edtDataI = datePicker("Data Inicial") {
      localePtBr()
      addValueChangeListener {
        viewModel.updateViewBD()
      }
    }
    edtDataF = datePicker("Data Final") {
      localePtBr()
      addValueChangeListener {
        viewModel.updateViewBD()
      }
    }
    edtCNPJ = textField("CNPJ") {
      valueChangeMode = ValueChangeMode.LAZY
      this.valueChangeTimeout = 1000
      addValueChangeListener {
        viewModel.updateViewBD()
      }
    }
    edtFornecedorNota = textField("Fornecedor Nota") {
      valueChangeMode = ValueChangeMode.LAZY
      this.valueChangeTimeout = 1000
      addValueChangeListener {
        viewModel.updateViewBD()
      }
    }
    edtNota = integerField("Nota Fiscal") {
      valueChangeMode = ValueChangeMode.LAZY
      this.valueChangeTimeout = 1000
      addValueChangeListener {
        viewModel.updateViewBD()
      }
    }

    this.lazyDownloadButton(text = "XML NFe", icon = FontAwesome.Solid.FILE.create(), fileName = {
      "dadosXML${System.nanoTime()}.zip"
    }) {
      val notas = itensSelecionados()
      viewModel.zipXml(notas)
    }
    this.lazyDownloadButton(text = "XML CTe", icon = FontAwesome.Solid.FILE.create(), fileName = {
      "dadosCte${System.nanoTime()}.zip"
    }) {
      val notas = itensSelecionados()
      viewModel.zipCte(notas)
    }
    this.lazyDownloadButton(text = "PDF", icon = FontAwesome.Solid.FILE.create(), fileName = {
      "dadosDanfe${System.nanoTime()}.zip"
    }) {
      val notas = itensSelecionados()
      viewModel.zipPdf(notas)
    }
  }

  override fun Grid<NotaEntradaXML>.gridPanel() {
    setSelectionMode(Grid.SelectionMode.MULTI)
    addColumnSeq("Item")
    addColumnButton(iconButton = VaadinIcon.FILE_TABLE, tooltip = "Nota fiscal", header = "NF") { nota ->
      viewModel.createDanfe(nota)
    }
    nfeFileNotaLoja()
    nfeFileNotaNI()
    nfeFileNotaNumero()
    nfeFileEmissao()
    nfeFileEntrada()
    nfeFileCfop()
    nfeFileNotaForNot()
    nfeFileNotaForCad()
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
    viewModel.updateViewBD()
  }
}
