package br.com.astrosoft.devolucao.viewmodel.devolucao

class DevolucaoInternaViewModel(view: IDevolucaoInternaView) : DevolucaoAbstractViewModel<IDevolucaoInternaView>(view) {
  val tabAjusteGarantiaViewModel = TabAjusteGarantiaViewModel(this)
  val tabAjusteGarantiaPendenteViewModel = TabAjusteGarantiaPendenteViewModel(this)
  val tabAjusteGarantiaPagoViewModel = TabAjusteGarantiaPagoViewModel(this)
  val tabAjusteGarantiaPercaViewModel = TabAjusteGarantiaPercaViewModel(this)

  override fun listTab() = listOf(

    view.tabAjusteGarantia,
    view.tabAjusteGarantiaPendente,
    view.tabAjusteGarantiaPago,
    view.tabAjusteGarantiaPerca,
                                 )
}

interface IDevolucaoInternaView : IDevolucaoAbstractView {
  val tabAjusteGarantia: ITabAjusteGarantia
  val tabAjusteGarantiaPendente: ITabAjusteGarantiaPendente
  val tabAjusteGarantiaPago: ITabAjusteGarantiaPago
  val tabAjusteGarantiaPerca: ITabAjusteGarantiaPerca
}

