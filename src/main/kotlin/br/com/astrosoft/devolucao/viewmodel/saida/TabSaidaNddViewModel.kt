package br.com.astrosoft.devolucao.viewmodel.saida

import br.com.astrosoft.devolucao.model.beans.FiltroNotaSaidaNdd
import br.com.astrosoft.devolucao.model.beans.Loja
import br.com.astrosoft.devolucao.model.beans.NotaSaidaNdd
import br.com.astrosoft.devolucao.model.beans.ReimpressaoNota
import br.com.astrosoft.devolucao.model.reports.DanfeReport
import br.com.astrosoft.devolucao.model.reports.ETIPO_COPIA
import br.com.astrosoft.framework.model.Config
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.viewmodel.ITabView
import java.time.LocalDate
import java.time.LocalTime

class TabSaidaNddViewModel(val viewModel: SaidaViewModel) {
  val subView
    get() = viewModel.view.tabSaidaNddViewModel

  fun updateView() {
    val filtro = subView.filtro()
    if (filtro.isEmpty()) {
      limparView()
    }
    else {
      val resultList = NotaSaidaNdd.findAll(subView.filtro())
      subView.limparFiltro()
      subView.updateGrid(resultList)
    }
  }

  fun limparView() {
    subView.limparFiltro()
    subView.updateGrid(emptyList())
  }

  fun createDanfe(listaNota: List<NotaSaidaNdd>, tipo: ETIPO_COPIA) {
    val listItensNotaReport = listaNota.map { nota ->
      nota.itensNotaReport()
    }
    val report = DanfeReport.create(listItensNotaReport, tipo)
    viewModel.view.showReport("Danfee", report)
    listaNota.forEach { nota ->
      val reimpressaoNota =
        ReimpressaoNota(
          data = LocalDate.now(),
          hora = LocalTime.now().format(),
          loja = nota.loja,
          nota = "${nota.numero}/${nota.serie}",
          tipo = tipo.descricao,
          usuario = Config.user?.login ?: "",
          dataNota = nota.data,
          codcli = nota.codigoCliente,
          nomecli = nota.nomeCliente,
          valor = nota.valor,
                       )
      reimpressaoNota.insertReimpressao()
    }
    updateView()
  }

  fun findLojas(): List<Loja> {
    return Loja.allLojas().sortedBy { it.no }
  }
}

interface ITabSaidaNddViewModel : ITabView {
  fun filtro(): FiltroNotaSaidaNdd
  fun updateGrid(itens: List<NotaSaidaNdd>)
  fun limparFiltro()
  fun setDateNow()
}