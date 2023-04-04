package br.com.astrosoft.devolucao.viewmodel.devolucao

class Devolucao01ViewModel(view: IDevolucao01View) : DevolucaoAbstractViewModel<IDevolucao01View>(view) {
  val tabNotaSerie01ViewModel = TabNotaSerie01ViewModel(this)
  val tabNotaSerie01PagoViewModel = TabNotaSerie01PagoViewModel(this)

  val tabNotaSerie01ColetaViewModel = TabNotaSerie01ColetaViewModel(this)
  val tabNotaRemessaConsertoViewModel = TabNotaRemessaConsertoViewModel(this)
  val tabNotaFinanceiroViewModel = TabNotaFinanceiroViewModel(this)
  val tabConferenciaSap = TabConferenciaSapViewModel(this)
  val tabSap = TabSapViewModel(this)
  val tabDesconto = TabDescontoViewModel(this)
  val tabRetorno = TabRetornoSerie1ViewModel(this)

  override fun listTab() = listOf(
    view.tabNotaSerie01,
    view.tabNotaSerie01Pago,
    view.tabNotaSerie01Coleta,
    view.tabNotaRemessaConserto,
    view.tabNotaFinanceiro,
    view.tabConferenciaSap,
    view.tabSap,
    view.tabDesconto,
    view.tabRetorno,
                                 )
}

interface IDevolucao01View : IDevolucaoAbstractView {
  val tabNotaSerie01: ITabNotaSerie01
  val tabNotaSerie01Pago: ITabNotaSerie01Pago

  val tabNotaSerie01Coleta: ITabNotaSerie01Coleta
  val tabNotaRemessaConserto: ITabNotaRemessaConserto
  val tabNotaFinanceiro: ITabNotaFinanceiro
  val tabConferenciaSap: ITabConferenciaSap
  val tabSap: ITabSap
  val tabDesconto: ITabDesconto
  val tabRetorno: ITabRetornoSerie1
}

