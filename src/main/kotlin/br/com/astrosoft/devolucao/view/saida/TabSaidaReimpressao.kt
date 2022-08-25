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
import br.com.astrosoft.framework.model.Config.user
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.TabPanelGrid
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode
import org.claspina.confirmdialog.ButtonOption
import org.claspina.confirmdialog.ConfirmDialog

class TabSaidaReimpressao(val viewModel: TabSaidaReimpressaoViewModel) :
        TabPanelGrid<ReimpressaoNota>(ReimpressaoNota::class), ITabSaidaReimpressaoViewModel {
  private lateinit var edtFiltro: TextField
  override fun filtro(): FiltroReimpressao {
    val userSaci = user as? UserSaci
    val loja = if (userSaci?.admin == true) 0 else userSaci?.storeno ?: 0
    return FiltroReimpressao(filtro = edtFiltro.value ?: "", loja = loja)
  }

  private fun showQuestion(msg: String, execYes: () -> Unit) {
    showQuestion(msg, execYes) {}
  }

  private fun showQuestion(msg: String, execYes: () -> Unit, execNo: () -> Unit) {
    ConfirmDialog
      .createQuestion()
      .withCaption("Confirmação")
      .withMessage(msg)
      .withYesButton({
                       execYes()
                     },
                     ButtonOption.caption("Sim"))
      .withNoButton({ execNo() }, ButtonOption.caption("Não"))
      .open()
  }

  override fun confirmaRemocao(exec: () -> Unit) {
    showQuestion("Remove o registro?", exec)
  }

  override fun isAuthorized(user: IUser): Boolean {
    val userSaci = user as? UserSaci
    return userSaci?.reimpressao == true
  }

  override val label: String
    get() = "Reimpresso"

  override fun updateComponent() {
    viewModel.updateView()
  }

  override fun HorizontalLayout.toolBarConfig() {
    edtFiltro = textField("Filtro") {
      valueChangeMode = ValueChangeMode.TIMEOUT
      width = "200px"

      addValueChangeListener {
        viewModel.updateView()
      }
    }
    if (user?.admin == true) {
      button("Excluir") {
        this.icon = VaadinIcon.TRASH.create()
        onLeftClick {
          viewModel.removeReimpressao(gridPanel.selectedItems)
        }
      }
    }
  }

  override fun Grid<ReimpressaoNota>.gridPanel() {
    setSelectionMode(Grid.SelectionMode.MULTI)
    lojaReimpressao()
    dataReimpressao()
    horaReimpressao()
    tipoReimpressao()
    dataNotaReimpressao()
    numeroReimpressao()
    codCliReimpressao()
    nomecliReimpressao()
    valorReimpressao()
    usuarioReimpressao()
  }
}