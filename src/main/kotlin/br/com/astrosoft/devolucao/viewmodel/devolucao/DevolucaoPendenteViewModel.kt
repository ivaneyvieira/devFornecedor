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

  override fun listTab() = listOf(view.tabNotaPendenteBase, view.tabNotaPendenteNota, view.tabNotaPendenteEmail)
}

interface IDevolucaoPendenteView : IDevolucaoAbstractView {
  val tabNotaPendenteBase: ITabNotaPendente
  val tabNotaPendenteNota: ITabNotaPendente
  val tabNotaPendenteEmail: ITabNotaPendente
}

enum class ESituacaoPendencia(val title: String, val value: String?) {
  BASE("Base", null), NOTA("Nota", ""), EMAIL("E-mail", "E-MAIL")
}