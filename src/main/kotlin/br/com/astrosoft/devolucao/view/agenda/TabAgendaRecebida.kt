package br.com.astrosoft.devolucao.view.agenda

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.agenda.ITabAgendaRecebida
import br.com.astrosoft.devolucao.viewmodel.agenda.TabAgendaViewModelRecebida
import br.com.astrosoft.framework.model.IUser

class TabAgendaRecebida(viewModel: TabAgendaViewModelRecebida) : TabAgendaAbstract(viewModel),
                                                                 ITabAgendaRecebida {
    override fun isAuthorized(user: IUser): Boolean {
        val username = user as? UserSaci
        return username?.agendaRecebida == true
    }

    override val label: String
        get() = "Recebido"
}
