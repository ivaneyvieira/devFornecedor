package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.viewmodel.devolucao.ESituacaoPendencia
import br.com.astrosoft.devolucao.viewmodel.devolucao.INFDFornecedoresView
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabNFDFornecedoresEditorViewModel
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabNFDFornecedoresEditorViewModel
import br.com.astrosoft.framework.model.IUser

class TabNFDFornecedoresEditor(viewModel: TabNFDFornecedoresEditorViewModel) :
        TabDevolucaoAbstract<INFDFornecedoresView>(viewModel),
        ITabNFDFornecedoresEditorViewModel {
  override val label: String
    get() = "Editor"
  override val situacaoPendencia: ESituacaoPendencia?
    get() = null

  override fun isAuthorized(user: IUser): Boolean {
    return true
  }
}