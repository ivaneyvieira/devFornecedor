package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.framework.model.Config
import br.com.astrosoft.framework.viewmodel.fail
import java.time.LocalDate

class TabPedidoViewModel(viewModel: Devolucao01ViewModel) : TabDevolucaoViewModelAbstract<IDevolucao01View>(viewModel) {
  fun salvaSituacaoPedido(situacao: ESituacaoPedido?, itens: List<NotaSaida>) = viewModel.exec {
    situacao ?: fail("A situação não foi selecionada")
    itens.ifEmpty {
      fail("Não foi selecionado nenhum pedido")
    }
    itens.forEach { nota ->
      val userSaci = Config.user?.login ?: ""
      nota.situacao = situacao.valueStr
      nota.dataSituacao = LocalDate.now()
      nota.usuarioSituacao = userSaci
      NotaSaida.salvaDesconto(nota)
    }
    subView.updateNota()
  }

  override val subView
    get() = viewModel.view.tabPedido
}

interface ITabPedido : ITabNota {
  override val serie: Serie
    get() = Serie.PED
  override val pago66: SimNao
    get() = SimNao.NONE
  override val pago01: SimNao
    get() = SimNao.NAO
  override val coleta01: SimNao
    get() = SimNao.NONE
  override val remessaConserto: SimNao
    get() = SimNao.NONE
}