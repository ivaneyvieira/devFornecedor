package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.viewmodel.devolucao.*
import br.com.astrosoft.framework.model.IUser

class TabNFDFornecedoresPaga(viewModel: TabNFDFornecedoresPagaViewModel) :
        TabDevolucaoAbstract<INFDFornecedoresView>(viewModel),
        ITabNFDFornecedoresPagaViewModel {
  override val label: String
    get() = "Paga"
  override val situacaoPendencia: ESituacaoPendencia?
    get() = null

  override fun isAuthorized(user: IUser): Boolean {
    return true
  }
}