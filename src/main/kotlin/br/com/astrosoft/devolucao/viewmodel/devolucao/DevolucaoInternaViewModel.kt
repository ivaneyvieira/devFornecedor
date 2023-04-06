package br.com.astrosoft.devolucao.viewmodel.devolucao

class DevolucaoInternaViewModel(view: IDevolucaoInternaView) : DevolucaoAbstractViewModel<IDevolucaoInternaView>(view) {
  val tabAjusteGarantiaViewModel = TabAjusteGarantiaViewModel(this)
  val tabAjusteGarantiaPendenteViewModel = TabAjusteGarantiaPendenteViewModel(this)
  val tabAjusteGarantiaPagoViewModel = TabAjusteGarantiaPagoViewModel(this)
  val tabAjusteGarantiaPercaViewModel = TabAjusteGarantiaPercaViewModel(this)
  val tabAjusteGarantia66ViewModel = TabAjusteGarantia66ViewModel(this)

  override fun listTab() = listOf(
    view.tabAjusteGarantia,
    view.tabAjusteGarantiaPendente,
    view.tabAjusteGarantiaPago,
    view.tabAjusteGarantiaPerca,
    view.tabAjusteGarantia66,
  )
}

interface IDevolucaoInternaView : IDevolucaoAbstractView {
  val tabAjusteGarantia: ITabAjusteGarantia
  val tabAjusteGarantiaPendente: ITabAjusteGarantiaPendente
  val tabAjusteGarantiaPago: ITabAjusteGarantiaPago
  val tabAjusteGarantiaPerca: ITabAjusteGarantiaPerca
  val tabAjusteGarantia66: ITabAjusteGarantia66
}

