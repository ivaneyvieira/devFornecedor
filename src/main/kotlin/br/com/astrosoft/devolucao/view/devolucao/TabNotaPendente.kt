package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.viewmodel.devolucao.ESituacaoPendencia
import br.com.astrosoft.devolucao.viewmodel.devolucao.IDevolucaoPendenteView
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabNotaPendente
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabNotaPendenteViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.center
import br.com.astrosoft.framework.view.left
import br.com.astrosoft.framework.view.right
import com.vaadin.flow.component.grid.Grid

class TabNotaPendente(viewModel: TabNotaPendenteViewModel, private val situacao: () -> ESituacaoPendencia) :
        TabDevolucaoAbstract<IDevolucaoPendenteView>(viewModel), ITabNotaPendente {
  override val label: String
    get() = situacao().title

  override val situacaoPendencia: ESituacaoPendencia
    get() = situacao()

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci ?: return false
    return username.forPendente
  }

  init {
    val situacao = situacao()
    situacaoCol.configCol(situacao.situacaoCol)
    notaCol.configCol(situacao.notaCol)
    niCol.configCol(situacao.niCol)
    docCol.configCol(situacao.docCol)
    tituloCol.configCol(situacao.numeroCol)
    dataCol.configCol(situacao.dataCol)
    userCol.configCol(situacao.userCol)
    dataSitCol.configCol(situacao.dataSitCol)
    if (situacao == ESituacaoPendencia.CREDITO_CONTA) {
      val columns = gridPanel.columns.toMutableList()
      val pNi = columns.indexOf(niCol)
      val pSit = columns.indexOf(docCol)
      if (pNi >= 0 && pSit >= 0) {
        columns.removeAt(pNi)
        columns.add(pSit, niCol)
      }
      gridPanel.setColumnOrder(columns)
    }else if(situacao == ESituacaoPendencia.BASE){
      niCol.isVisible = false
      tituloCol.isVisible = false
      docCol.isVisible = false
      situacaoCol.setHeader("Situação")
    }
  }
}

fun <T : Any> Grid.Column<T>.configCol(head: String?) {
  if (head != null) {
    val colHead = head.split(":").getOrNull(0) ?: ""
    val aling = head.split(":").getOrNull(1) ?: ""
    this.setHeader(colHead)
    this.isVisible = colHead.isNotBlank()
    when (aling) {
      "L" -> this.left()
      "R" -> this.right()
      "C" -> this.center()
    }
  }
}
