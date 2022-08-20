package br.com.astrosoft.devolucao.view.saida

import br.com.astrosoft.devolucao.model.beans.FiltroReimpressao
import br.com.astrosoft.devolucao.model.beans.ReimpressaoNota
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.saida.columns.ReimpressaoColumns.codCliReimpressao
import br.com.astrosoft.devolucao.view.saida.columns.ReimpressaoColumns.dataNotaReimpressao
import br.com.astrosoft.devolucao.view.saida.columns.ReimpressaoColumns.dataReimpressao
import br.com.astrosoft.devolucao.view.saida.columns.ReimpressaoColumns.horaReimpressao
import br.com.astrosoft.devolucao.view.saida.columns.ReimpressaoColumns.lojaReimpressao
import br.com.astrosoft.devolucao.view.saida.columns.ReimpressaoColumns.nomecliReimpressao
import br.com.astrosoft.devolucao.view.saida.columns.ReimpressaoColumns.numeroReimpressao
import br.com.astrosoft.devolucao.view.saida.columns.ReimpressaoColumns.tipoReimpressao
import br.com.astrosoft.devolucao.view.saida.columns.ReimpressaoColumns.usuarioReimpressao
import br.com.astrosoft.devolucao.view.saida.columns.ReimpressaoColumns.valorReimpressao
import br.com.astrosoft.devolucao.viewmodel.saida.ITabSaidaReimpressaoViewModel
import br.com.astrosoft.devolucao.viewmodel.saida.TabSaidaReimpressaoViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.TabPanelGrid
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode

class TabSaidaReimpressao(val viewModel: TabSaidaReimpressaoViewModel) :
        TabPanelGrid<ReimpressaoNota>(ReimpressaoNota::class), ITabSaidaReimpressaoViewModel {
  private lateinit var edtFiltro: TextField
  override fun filtro(): FiltroReimpressao {
    return FiltroReimpressao(filtro = edtFiltro.value ?: "")
  }

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.reimpressao == true
  }

  override val label: String
    get() = "Reimpressão"

  override fun updateComponent() {
    viewModel.updateView()
  }

  override fun HorizontalLayout.toolBarConfig() {
    edtFiltro = textField("Filtro") {
      valueChangeMode = ValueChangeMode.TIMEOUT

      addValueChangeListener {
        viewModel.updateView()
      }
    }
  }

  override fun Grid<ReimpressaoNota>.gridPanel() {
    lojaReimpressao()
    dataNotaReimpressao()
    numeroReimpressao()
    codCliReimpressao()
    nomecliReimpressao()
    valorReimpressao()
    dataReimpressao()
    horaReimpressao()
    tipoReimpressao()
    usuarioReimpressao()
  }
}