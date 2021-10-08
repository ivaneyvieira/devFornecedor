package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.DevFornecedorLayout
import br.com.astrosoft.devolucao.viewmodel.devolucao.DevolucaoPendenteViewModel
import br.com.astrosoft.devolucao.viewmodel.devolucao.ESituacaoPendencia
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucaoPendenteView
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.ViewLayout
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route("", layout = DevFornecedorLayout::class)
@PageTitle("Pendente")
@CssImport("./styles/gridTotal.css")
class DevolucaoPendenteView : ViewLayout<DevolucaoPendenteViewModel>(), IDevolucaoPendenteView {
  override val viewModel: DevolucaoPendenteViewModel = DevolucaoPendenteViewModel(this)
  override val tabNotaPendenteBase = TabNotaPendente(viewModel.tabNotaPendenteBaseViewModel, ESituacaoPendencia.BASE)
  override val tabNotaPendenteNota = TabNotaPendente(viewModel.tabNotaPendenteNotaViewModel, ESituacaoPendencia.NOTA)
  override val tabNotaPendenteEmail = TabNotaPendente(viewModel.tabNotaPendenteEmailViewModel, ESituacaoPendencia.EMAIL)
  override val tabNotaPendenteTransito =
          TabNotaPendente(viewModel.tabNotaPendenteTransitoViewModel, ESituacaoPendencia.TRANSITO)
  override val tabNotaPendenteFabrica =
          TabNotaPendente(viewModel.tabNotaPendenteFabricaViewModel, ESituacaoPendencia.FABRICA)
  override val tabNotaPendenteCConcedido =
          TabNotaPendente(viewModel.tabNotaPendenteCConcedidoViewModel, ESituacaoPendencia.CREDITO_CONCEDIDO)
  override val tabNotaPendenteCAplicado =
          TabNotaPendente(viewModel.tabNotaPendenteCAplicadoViewModel, ESituacaoPendencia.CREDITO_APLICADO)
  override val tabNotaPendenteCAguardar =
          TabNotaPendente(viewModel.tabNotaPendenteCAguardarViewModel, ESituacaoPendencia.CREDITO_AGUARDAR)
  override val tabNotaPendenteCConta =
          TabNotaPendente(viewModel.tabNotaPendenteCContaViewModel, ESituacaoPendencia.CREDITO_CONTA)
  override val tabNotaPendenteBonificada =
          TabNotaPendente(viewModel.tabNotaPendenteBonificadaViewModel, ESituacaoPendencia.BONIFICADA)
  override val tabNotaPendenteReposicao =
          TabNotaPendente(viewModel.tabNotaPendenteReposicaoViewModel, ESituacaoPendencia.REPOSICAO)
  override val tabNotaPendenteRetorno =
          TabNotaPendente(viewModel.tabNotaPendenteRetornoViewModel, ESituacaoPendencia.RETORNO)

  override fun isAccept(user: IUser): Boolean {
    val userSaci = user as? UserSaci ?: return false
    return userSaci.admin
  }

  init {
    addTabSheat(viewModel)
  }
}

