package br.com.astrosoft.devolucao.viewmodel.devolucao

class TabNotaRemessaConsertoViewModel(viewModel: DevolucaoViewModel) :
        TabDevolucaoViewModelAbstract(viewModel) {
    override val subView
        get() = viewModel.view.tabNotaRemessaConserto
}

interface ITabNotaRemessaConserto : ITabNota {
    override val serie: Serie
        get() = Serie.VAZIO
    override val pago66: SimNao
        get() = SimNao.NONE
    override val coleta01: SimNao
        get() = SimNao.NONE
    override val remessaConserto: SimNao
        get() = SimNao.SIM
}