package br.com.astrosoft.devolucao.viewmodel.devolucao

class DevolucaoAvariaRecViewModel(view: IDevolucaoAvariaRecView) : DevolucaoAbstractViewModel<IDevolucaoAvariaRecView>(view) {
  val tabAvariaRecEditorViewModel = TabAvariaRecEditorViewModel(this)
  val tabAvariaRecPendenteViewModel = TabAvariaRecPendenteViewModel(this)
  val tabAvariaRecTransportadoraViewModel = TabAvariaRecTransportadoraViewModel(this)
  val tabAvariaRecEmailViewModel = TabAvariaRecEmailViewModel(this)
  val tabAvariaRecUsrViewModel = TabAvariaRecUsrViewModel(this)

  override fun listTab() = listOf(
    view.tabAvariaRecPendente,
    view.tabAvariaRecTransportadora,
    view.tabAvariaRecEmail,
    view.tabAvariaRecEditor,
    view.tabAvariaRecUsr
  )
}

interface IDevolucaoAvariaRecView : IDevolucaoAbstractView {
  val tabAvariaRecEditor: ITabAvariaRecEditor
  val tabAvariaRecPendente: ITabAvariaRecPendente
  val tabAvariaRecTransportadora: ITabAvariaRecTransportadora
  val tabAvariaRecEmail: ITabAvariaRecEmail
  val tabAvariaRecUsr: ITabAvariaRecUsr
}

