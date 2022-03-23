package br.com.astrosoft.devolucao.viewmodel.devolucao

class DevolucaoInternaViewModel(view: IDevolucaoInternaView) : DevolucaoAbstractViewModel<IDevolucaoInternaView>(view) {
  val tabAjusteGarantiaViewModel = TabAjusteGarantiaViewModel(this)
  val tabAjusteGarantiaPagoViewModel = TabAjusteGarantiaPagoViewModel(this)
  val tabAjusteGarantiaPercaViewModel = TabAjusteGarantiaPercaViewModel(this)

  override fun listTab() = listOf(

    view.tabAjusteGarantia,
    view.tabAjusteGarantiaPago,
    view.tabAjusteGarantiaPerca,
                                 )
}

interface IDevolucaoInternaView : IDevolucaoAbstractView {
  val tabAjusteGarantia: ITabAjusteGarantia
  val tabAjusteGarantiaPago: ITabAjusteGarantiaPago
  val tabAjusteGarantiaPerca: ITabAjusteGarantiaPerca
}

