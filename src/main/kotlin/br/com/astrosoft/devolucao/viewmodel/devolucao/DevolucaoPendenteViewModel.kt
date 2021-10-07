package br.com.astrosoft.devolucao.viewmodel.devolucao

class DevolucaoPendenteViewModel(view: IDevolucaoPendenteView) :
        DevolucaoAbstractViewModel<IDevolucaoPendenteView>(view) {
  val tabNotaPendenteSerie01ViewModel = TabNotaPendenteSerie01ViewModel(this)

  override fun listTab() = listOf(
    view.tabNotaPendenteSerie01,
                                 )
}

interface IDevolucaoPendenteView : IDevolucaoAbstractView {
  val tabNotaPendenteSerie01: ITabNotaPendenteSerie01
}

