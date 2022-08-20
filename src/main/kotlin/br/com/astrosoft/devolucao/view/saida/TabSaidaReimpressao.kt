package br.com.astrosoft.devolucao.view.saida

import br.com.astrosoft.devolucao.model.beans.FiltroReimpressao
import br.com.astrosoft.devolucao.model.beans.ReimpressaoNota
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.saida.columns.ReimpressaoColumns.dataNotaSaida
import br.com.astrosoft.devolucao.view.saida.columns.ReimpressaoColumns.horaNotaSaida
import br.com.astrosoft.devolucao.view.saida.columns.ReimpressaoColumns.lojaNotaSaida
import br.com.astrosoft.devolucao.view.saida.columns.ReimpressaoColumns.numeroNotaSaida
import br.com.astrosoft.devolucao.view.saida.columns.ReimpressaoColumns.tipoNotaSaida
import br.com.astrosoft.devolucao.view.saida.columns.ReimpressaoColumns.usuarioNotaSaida
import br.com.astrosoft.devolucao.viewmodel.saida.ITabSaidaReimpressaoViewModel
import br.com.astrosoft.devolucao.viewmodel.saida.TabSaidaReimpressaoViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
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
    dataNotaSaida()
    horaNotaSaida()
    lojaNotaSaida()
    numeroNotaSaida()
    tipoNotaSaida()
    usuarioNotaSaida()
  }
}