package br.com.astrosoft.devolucao.view.devFornecedor

import br.com.astrosoft.devolucao.viewmodel.devolucao.INota66Pago
import br.com.astrosoft.devolucao.viewmodel.devolucao.INotaDevolucao
import br.com.astrosoft.devolucao.viewmodel.devolucao.NotaSerie66PagoViewModel
import br.com.astrosoft.devolucao.viewmodel.devolucao.NotaSerie66ViewModel

class TabNota66Pago(viewModel: NotaSerie66PagoViewModel): TabFornecedorAbstract(viewModel), INota66Pago {
  override val label: String
    get() = "Notas série 66 Pago"
}