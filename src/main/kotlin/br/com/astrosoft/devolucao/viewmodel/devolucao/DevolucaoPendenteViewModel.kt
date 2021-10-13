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

enum class ESituacaoPendencia(val title: String,
                              val valueStr: String?,
                              val descricao: String,
                              val docCol: String? = null,
                              val numeroCol: String? = null,
                              val dataCol: String? = null) {
  BASE(title = "Base", valueStr = null, descricao = "Base"),
  NOTA(title = "Nota", valueStr = "", descricao = "Nota"),
  EMAIL(title = "E-mail", valueStr = "E-MAIL", descricao = "E-mail"),
  TRANSITO(title = "Trânsito", valueStr = "TRANSITO", descricao = "Trânsito"),
  FABRICA(title = "Fabrica", valueStr = "FABRICA", descricao = "Fábrica"),
  CREDITO_CONCEDIDO(title = "Crédito Concedido", valueStr = "CREDITO_CONCEDIDO", descricao = "Consedido"),
  CREDITO_APLICADO(title = "Crédito Aplicado",
                   valueStr = "CREDITO_APLICADO",
                   descricao = "Aplicado",
                   docCol = "Doc",
                   numeroCol = "Título",
                   dataCol = "Vencimento"),
  CREDITO_AGUARDAR(title = "Crédito Aguardar", valueStr = "CREDITO_AGUARDAR", descricao = "Aguardar"),
  CREDITO_CONTA(title = "Crédito Conta", valueStr = "CREDITO_CONTA", descricao = "Conta"),
  BONIFICADA(title = "Bonificada", valueStr = "BONIFICADA", descricao = "Bonificado"),
  REPOSICAO(title = "Reposição", valueStr = "REPOSICAO", descricao = "Reposição"),
  RETORNO(title = "Retorno", valueStr = "RETORNO", descricao = "Retorno")
}