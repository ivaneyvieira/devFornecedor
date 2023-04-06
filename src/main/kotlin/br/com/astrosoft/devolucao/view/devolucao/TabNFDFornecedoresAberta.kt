package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.viewmodel.devolucao.ESituacaoPendencia
import br.com.astrosoft.devolucao.viewmodel.devolucao.INFDFornecedoresView
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabNFDFornecedoresAbertaViewModel
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabNFDFornecedoresAbertaViewModel
import br.com.astrosoft.framework.model.IUser

class TabNFDFornecedoresAberta(viewModel: TabNFDFornecedoresAbertaViewModel) :
  TabDevolucaoAbstract<INFDFornecedoresView>(viewModel),
  ITabNFDFornecedoresAbertaViewModel {
  override val label: String
    get() = "Aberta"
  override val situacaoPendencia: ESituacaoPendencia?
    get() = null

  override fun isAuthorized(user: IUser): Boolean {
    return true
  }
}