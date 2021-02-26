package br.com.astrosoft.devolucao.view.recebimento

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.DevFornecedorLayout
import br.com.astrosoft.devolucao.viewmodel.recebimento.IRecebimentoView
import br.com.astrosoft.devolucao.viewmodel.recebimento.RecebimentoViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.ViewLayout
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = DevFornecedorLayout::class)
@PageTitle("Recebimento")
@CssImport("./styles/gridTotal.css")
class RecebimentoView : ViewLayout<RecebimentoViewModel>(), IRecebimentoView {
    override val viewModel = RecebimentoViewModel(this)
    override val tabNotaPendente = TabNotaPendente(viewModel.tabNotaPendenteViewModel)

    override fun isAccept(user: IUser): Boolean {
        val userSaci = user as? UserSaci ?: return false
        return userSaci.menuRecebimento
    }

    init {
        addTabSheat(viewModel)
    }
}