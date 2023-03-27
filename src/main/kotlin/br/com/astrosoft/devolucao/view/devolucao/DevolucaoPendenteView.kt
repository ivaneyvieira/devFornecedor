package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.view.DevFornecedorLayout
import br.com.astrosoft.devolucao.viewmodel.devolucao.DevolucaoPendenteViewModel
import br.com.astrosoft.devolucao.viewmodel.devolucao.ESituacaoPendencia.*
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
  override val tabNotaPendenteBase = TabNotaPendente(viewModel.tabNotaPendenteBaseViewModel) { BASE }
  override val tabNotaPendenteNota = TabNotaPendente(viewModel.tabNotaPendenteNotaViewModel) { NOTA }
  override val tabNotaPendenteCte = TabNotaPendente(viewModel.tabNotaPendenteCteViewModel) { AGUARDA_COLETA }
  override val tabNotaPendenteColeta = TabNotaPendente(viewModel.tabNotaPendenteColetaViewModel) { ASSINA_CTE }
  override val tabNotaPendenteEmail = TabNotaPendente(viewModel.tabNotaPendenteEmailViewModel) { EMAIL }
  override val tabNotaPendenteTransito = TabNotaPendente(viewModel.tabNotaPendenteTransitoViewModel) { TRANSITO }
  override val tabNotaPendenteFabrica = TabNotaPendente(viewModel.tabNotaPendenteFabricaViewModel) { FABRICA }
  override val tabNotaPendenteCConcedido =
    TabNotaPendente(viewModel.tabNotaPendenteCConcedidoViewModel) { CREDITO_CONCEDIDO }
  override val tabNotaPendenteCAplicado =
    TabNotaPendente(viewModel.tabNotaPendenteCAplicadoViewModel) { CREDITO_APLICADO }
  override val tabNotaPendenteCAguardar =
    TabNotaPendente(viewModel.tabNotaPendenteCAguardarViewModel) { CREDITO_AGUARDAR }
  override val tabNotaPendenteCConta = TabNotaPendente(viewModel.tabNotaPendenteCContaViewModel) { CREDITO_CONTA }
  override val tabNotaPendenteBonificada = TabNotaPendente(viewModel.tabNotaPendenteBonificadaViewModel) { BONIFICADA }
  override val tabNotaPendenteReposicao = TabNotaPendente(viewModel.tabNotaPendenteReposicaoViewModel) { REPOSICAO }
  override val tabNotaPendenteRetorno = TabNotaPendenteRetorno(viewModel.tabNotaPendenteRetornoViewModel) { RETORNO }

  override fun isAccept(user: IUser): Boolean {
    val userSaci = user as? UserSaci ?: return false
    return userSaci.forPendente
  }

  init {
    addTabSheat(viewModel)
  }
}

