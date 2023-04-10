package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.framework.model.Config
import br.com.astrosoft.framework.viewmodel.fail
import java.time.LocalDate

class TabNotaPendenteViewModel(viewModel: DevolucaoPendenteViewModel, val getSubView: () -> ITabNota) :
  TabDevolucaoViewModelAbstract<IDevolucaoPendenteView>(viewModel) {
  override fun  salvaSituacao(situacao: ESituacaoPendencia?, itens: List<NotaSaida>) = viewModel.exec {
    situacao ?: fail("A situação não foi selecionada")
    itens.ifEmpty {
      fail("Não foi selecionado nenhuma nota")
    }
    itens.forEach { nota ->
      val userSaci = Config.user?.login ?: ""
      nota.situacao = situacao.valueStr ?: ""
      if (!situacao.valueStr.isNullOrBlank()) {
        nota.dataSituacao = LocalDate.now()
        nota.usuarioSituacao = userSaci
      }
      NotaSaida.salvaDesconto(nota)
    }
    subView.updateComponent()
  }

  override val subView: ITabNota
    get() = getSubView()
}

interface ITabNotaPendente : ITabNota {
  override val serie: Serie
    get() = Serie.Serie01
  override val pago66: SimNao
    get() = SimNao.NONE
  override val pago01: SimNao
    get() = SimNao.NAO
  override val coleta01: SimNao
    get() = SimNao.NONE
  override val remessaConserto: SimNao
    get() = SimNao.NONE
}