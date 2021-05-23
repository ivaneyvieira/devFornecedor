package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel

abstract class DevolucaoAbstractViewModel<T : IDevolucaoAbstractView>(view: T) : ViewModel<T>(view)

interface IDevolucaoAbstractView : IView