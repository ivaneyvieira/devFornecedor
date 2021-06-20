package br.com.astrosoft.devolucao.view.entrada

import br.com.astrosoft.devolucao.model.beans.EDiferenca
import br.com.astrosoft.devolucao.model.beans.FiltroUltimaNotaEntrada
import br.com.astrosoft.devolucao.model.beans.UltimaNotaEntrada
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaCstn
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaCstp
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaData
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaDescricao
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaForn
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaIcmsn
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaIcmsp
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaIpin
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaIpip
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaLoja
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaMvan
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaMvap
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaNcmn
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaNcmp
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaNfe
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaNi
import br.com.astrosoft.devolucao.view.entrada.columms.UltimaNotaEntradaColumns.notaProd
import br.com.astrosoft.devolucao.viewmodel.entrada.TabUltimasEntradasViewModel
import br.com.astrosoft.framework.view.SubWindowForm
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome.Solid.FILE_EXCEL
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.comboBox
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import org.vaadin.stefan.LazyDownloadButton
import java.io.ByteArrayInputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@CssImport("./styles/gridTotal.css", themeFor = "vaadin-grid")
class DlgRelatorioUltimaCompra(val viewModel: TabUltimasEntradasViewModel, val filtro: FiltroUltimaNotaEntrada) {
  private lateinit var gridNota: Grid<UltimaNotaEntrada>

  fun show() {
    val listNotas = viewModel.findNotas(filtro)
    val form = SubWindowForm("Relatório", toolBar = {
      this.button("Relatório") {
        icon = VaadinIcon.PRINT.create()
        onLeftClick {
          viewModel.imprimeRelatorio(gridNota.selectedItems.toList())
        }
      }
      add(buttonPlanilha { gridNota.selectedItems.toList() })
      this.comboDiferenca("ICMS") {
        value = filtro.icms

        this.addValueChangeListener {
          filtro.icms = it.value
          gridNota.setItems(viewModel.findNotas(filtro))
        }
      }
      this.comboDiferenca("IPI") {
        value = filtro.ipi

        this.addValueChangeListener {
          filtro.ipi = it.value
          gridNota.setItems(viewModel.findNotas(filtro))
        }
      }
      this.comboDiferenca("CST") {
        value = filtro.cst

        this.addValueChangeListener {
          filtro.cst = it.value
          gridNota.setItems(viewModel.findNotas(filtro))
        }
      }
      this.comboDiferenca("MVA") {
        value = filtro.mva

        this.addValueChangeListener {
          filtro.mva = it.value
          gridNota.setItems(viewModel.findNotas(filtro))
        }
      }
      this.comboDiferenca("NCM") {
        value = filtro.ncm

        this.addValueChangeListener {
          filtro.ncm = it.value
          gridNota.setItems(viewModel.findNotas(filtro))
        }
      }
    }) {
      gridNota = createGrid(listNotas)
      HorizontalLayout().apply {
        setSizeFull()
        addAndExpand(gridNota)
      }
    }
    form.open()
  }

  private fun buttonPlanilha(notas: () -> List<UltimaNotaEntrada>): LazyDownloadButton {
    return LazyDownloadButton("Planilha", FILE_EXCEL.create(), ::filename) {
      ByteArrayInputStream(viewModel.geraPlanilha(notas()))
    }
  }

  private fun filename(): String {
    val sdf = DateTimeFormatter.ofPattern("yyMMddHHmmss")
    val textTime = LocalDateTime.now().format(sdf)
    return "notas$textTime.xlsx"
  }

  private fun createGrid(listParcelas: List<UltimaNotaEntrada>): Grid<UltimaNotaEntrada> {
    return Grid(UltimaNotaEntrada::class.java, false).apply {
      setSizeFull()
      addThemeVariants(GridVariant.LUMO_COMPACT)
      isMultiSort = false
      setSelectionMode(Grid.SelectionMode.MULTI)
      setItems(listParcelas)

      notaLoja()
      notaNi()
      notaData()
      notaNfe()
      notaForn()
      notaProd()
      notaDescricao()
      notaIcmsn().marcaDiferenca { icmsDif == "N" }
      notaIcmsp().marcaDiferenca { icmsDif == "N" }
      notaIpin().marcaDiferenca { ipiDif == "N" }
      notaIpip().marcaDiferenca { ipiDif == "N" }
      notaCstn().marcaDiferenca { cstDif == "N" }
      notaCstp().marcaDiferenca { cstDif == "N" }
      notaMvan().marcaDiferenca { mvaDif == "N" }
      notaMvap().marcaDiferenca { mvaDif == "N" }
      notaNcmn().marcaDiferenca { ncmDif == "N" }
      notaNcmp().marcaDiferenca { ncmDif == "N" }
    }
  }
}

fun Grid.Column<UltimaNotaEntrada>.marcaDiferenca(predicado: UltimaNotaEntrada.() -> Boolean) {
  this.setClassNameGenerator {
    if (it.predicado()) "marcaDiferenca" else null
  }
}

private fun HasComponents.comboDiferenca(label: String, block: ComboBox<EDiferenca>.() -> Unit): ComboBox<EDiferenca> {
  return comboBox(label) {
    this.setItems(EDiferenca.values().toList())
    this.setItemLabelGenerator {
      it.descricao
    }
    this.isAllowCustomValue = false
    this.isClearButtonVisible = false
    block()
  }
}

