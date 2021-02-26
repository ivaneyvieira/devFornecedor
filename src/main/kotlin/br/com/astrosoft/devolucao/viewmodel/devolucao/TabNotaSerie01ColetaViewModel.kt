package br.com.astrosoft.devolucao.viewmodel.devolucao

class TabNotaSerie01ColetaViewModel(viewModel: DevolucaoViewModel) :
        TabDevolucaoViewModelAbstract(viewModel) {
    override val subView
        get() = viewModel.view.tabNotaSerie01Coleta
}

interface ITabNotaSerie01Coleta : ITabNota {
    override val serie: Serie
        get() = Serie.Serie01
    override val pago66: SimNao
        get() = SimNao.NONE
    override val coleta01: SimNao
        get() = SimNao.SIM
    override val remessaConserto: SimNao
        get() = SimNao.NONE
}