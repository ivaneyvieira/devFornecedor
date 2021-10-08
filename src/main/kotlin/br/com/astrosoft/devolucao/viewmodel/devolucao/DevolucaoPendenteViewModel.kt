package br.com.astrosoft.devolucao.viewmodel.devolucao

class DevolucaoPendenteViewModel(view: IDevolucaoPendenteView) :
        DevolucaoAbstractViewModel<IDevolucaoPendenteView>(view) {
  val tabNotaPendenteBaseViewModel = TabNotaPendenteViewModel(this) {
    view.tabNotaPendenteBase
  }
  val tabNotaPendenteNotaViewModel = TabNotaPendenteViewModel(this) {
    view.tabNotaPendenteNota
  }
  val tabNotaPendenteEmailViewModel = TabNotaPendenteViewModel(this) {
    view.tabNotaPendenteEmail
  }
  val tabNotaPendenteTransitoViewModel = TabNotaPendenteViewModel(this) {
    view.tabNotaPendenteTransito
  }
  val tabNotaPendenteFabricaViewModel = TabNotaPendenteViewModel(this) {
    view.tabNotaPendenteFabrica
  }
  val tabNotaPendenteCConcedidoViewModel = TabNotaPendenteViewModel(this) {
    view.tabNotaPendenteCConcedido
  }
  val tabNotaPendenteCAplicadoViewModel = TabNotaPendenteViewModel(this) {
    view.tabNotaPendenteCAplicado
  }
  val tabNotaPendenteCAguardarViewModel = TabNotaPendenteViewModel(this) {
    view.tabNotaPendenteCAguardar
  }
  val tabNotaPendenteCContaViewModel = TabNotaPendenteViewModel(this) {
    view.tabNotaPendenteCConta
  }
  val tabNotaPendenteBonificadaViewModel = TabNotaPendenteViewModel(this) {
    view.tabNotaPendenteBonificada
  }
  val tabNotaPendenteReposicaoViewModel = TabNotaPendenteViewModel(this) {
    view.tabNotaPendenteReposicao
  }
  val tabNotaPendenteRetornoViewModel = TabNotaPendenteViewModel(this) {
    view.tabNotaPendenteRetorno
  }

  override fun listTab() = listOf(
    view.tabNotaPendenteBase,
    view.tabNotaPendenteNota,
    view.tabNotaPendenteEmail,
    view.tabNotaPendenteTransito,
    view.tabNotaPendenteFabrica,
    view.tabNotaPendenteCConcedido,
    view.tabNotaPendenteCAplicado,
    view.tabNotaPendenteCAguardar,
    view.tabNotaPendenteCConta,
    view.tabNotaPendenteBonificada,
    view.tabNotaPendenteReposicao,
    view.tabNotaPendenteRetorno,
                                 )
}

interface IDevolucaoPendenteView : IDevolucaoAbstractView {
  val tabNotaPendenteBase: ITabNotaPendente
  val tabNotaPendenteNota: ITabNotaPendente
  val tabNotaPendenteEmail: ITabNotaPendente
  val tabNotaPendenteTransito: ITabNotaPendente
  val tabNotaPendenteFabrica: ITabNotaPendente
  val tabNotaPendenteCConcedido: ITabNotaPendente
  val tabNotaPendenteCAplicado: ITabNotaPendente
  val tabNotaPendenteCAguardar: ITabNotaPendente
  val tabNotaPendenteCConta: ITabNotaPendente
  val tabNotaPendenteBonificada: ITabNotaPendente
  val tabNotaPendenteReposicao: ITabNotaPendente
  val tabNotaPendenteRetorno: ITabNotaPendente
}

enum class ESituacaoPendencia(val title: String, val valueStr: String?) {
  BASE(title = "Base", valueStr = null),
  NOTA(title = "Nota", valueStr = ""),
  EMAIL(title = "E-mail", valueStr = "E-MAIL"),
  TRANSITO(title = "Trânsito", valueStr = "TRANSITO"),
  FABRICA(title = "Fabrica", valueStr = "FABRICA"),
  CREDITO_CONCEDIDO(title = "Crédito Concedido", valueStr = "CREDITO_CONCEDIDO"),
  CREDITO_APLICADO(title = "Crédito Aplicado", valueStr = "CREDITO_APLICADO"),
  CREDITO_AGUARDAR(title = "Crédito Aguardar", valueStr = "CREDITO_AGUARDAR"),
  CREDITO_CONTA(title = "Crédito Conta", valueStr = "CREDITO_CONTA"),
  BONIFICADA(title = "Bonificada", valueStr = "BONIFICADA"),
  REPOSICAO(title = "Reposição", valueStr = "REPOSICAO"),
  RETORNO(title = "Retorno", valueStr = "RETORNO")
}