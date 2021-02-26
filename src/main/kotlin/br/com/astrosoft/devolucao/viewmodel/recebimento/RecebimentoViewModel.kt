package br.com.astrosoft.devolucao.viewmodel.recebimento

import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel

class RecebimentoViewModel(view: IRecebimentoView) : ViewModel<IRecebimentoView>(view) {
    val tabNotaPendenteViewModel = TabNotaPendenteViewModel(this)
    override fun listTab(): List<ITabView> = listOf(view.tabNotaPendente)
}

interface IRecebimentoView : IView {
    val tabNotaPendente: ITabNotaPendente
}