package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel

class DevFornecedorViewModel(view: IDevFornecedorView): ViewModel<IDevFornecedorView>(view) {
  val tabNotaDevolucaoViewModel = NotaDevolucaoViewModel(this)
  val tabNotaVendaViewModel = NotaVendaViewModel(this)
}

interface IDevFornecedorView: IView {
  val tabNotaDevolucao: INotaDevolucao
  val tabNotaVenda: INotaVenda
}