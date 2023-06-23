package br.com.astrosoft.devolucao.viewmodel.devolucao

class NFDFornecedoresViewModel(view: INFDFornecedoresView) :
    DevolucaoAbstractViewModel<INFDFornecedoresView>(view) {

  val tabNFDFornecedoresEditorViewModel = TabNFDFornecedoresEditorViewModel(this)
  val tabNFDFornecedoresAbertaViewModel = TabNFDFornecedoresAbertaViewModel(this)
  val tabNFDFornecedoresPagaViewModel = TabNFDFornecedoresPagaViewModel(this)

  override fun listTab() = listOf(
      view.tabNFDFornecedoresEditor,
      view.tabNFDFornecedoresAberta,
      view.tabNFDFornecedoresPaga,
  )
}

interface INFDFornecedoresView : IDevolucaoAbstractView {
  val tabNFDFornecedoresEditor: ITabNFDFornecedoresEditorViewModel
  val tabNFDFornecedoresAberta: ITabNFDFornecedoresAbertaViewModel
  val tabNFDFornecedoresPaga: ITabNFDFornecedoresPagaViewModel
}
