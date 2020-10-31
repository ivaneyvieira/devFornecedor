package br.com.astrosoft.devolucao.viewmodel

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel

class DevFornecedorViewModel (view: IDevFornecedorView): ViewModel<IDevFornecedorView>(view) {
  val tabNotaDevolucaoViewModel = NotaDevolucaoViewModel(this)
}

interface IDevFornecedorView: IView {
  val tabEntregaImprimir: INotaDevolucao

  

}