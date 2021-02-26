package br.com.astrosoft.devolucao.viewmodel.devolucao

class TabEntradaViewModel(viewModel: DevolucaoViewModel) :
        TabDevolucaoViewModelAbstract(viewModel) {
    override val subView
        get() = viewModel.view.tabEntrada
}

interface ITabEntrada : ITabNota {
    override val serie: String
        get() = "ENT"
    override val pago66: String
        get() = ""
    override val coleta01: String
        get() = ""
}