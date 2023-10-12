package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.devolucao.model.beans.FiltroNotaEntradaQuery
import br.com.astrosoft.devolucao.model.beans.Loja
import br.com.astrosoft.devolucao.model.beans.NotaEntradaQuery
import br.com.astrosoft.devolucao.model.reports.RelatorioTodasEntradasGrupo
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail

class TabTodasEntradasViewModel(val viewModel: EntradaViewModel) {
  val subView
    get() = viewModel.view.tabTodasEntradasViewModel

  fun openDlgRelatorio() = viewModel.exec {
    val list = findNotas(subView.getFiltro())
    subView.openRelatorio(list)
  }

  fun findLojas(): List<Loja> {
    return Loja.allLojas().sortedBy { it.no }
  }

  private fun findNotas(filtro: FiltroNotaEntradaQuery): List<NotaEntradaQuery> {
    return NotaEntradaQuery.findNotas(filtro).toList()
  }

  fun imprimeRelatorio(listNotas: List<NotaEntradaQuery>, markCst: Boolean, markIpi: Boolean, markIcms: Boolean) =
      viewModel.exec {
        if (!markCst && !markIcms && !markIpi) fail("Nenhuma s opções CST, IPI ou ICMS foram selecionadas")
        if (listNotas.isEmpty()) fail("Nenhuma nota selecionada")
        val grupoCst = listNotas.map { nt ->
          nt.grupo("CST", nt.cstn, nt.cstp)
        }
        val grupoIpi = listNotas.map { nt ->
          nt.grupo("IPI", nt.aliqIpi.format(), nt.aliqIpiP.format())
        }
        val grupoIcms = listNotas.map { nt ->
          nt.grupo("ICMS", nt.aliqIcms.format(), nt.aliqIcmsP.format())
        }
        val grupos = grupoCst + grupoIpi + grupoIcms
        val relatorio = RelatorioTodasEntradasGrupo.processaRelatorio(grupos)
        viewModel.showReport("nfPrecificacaoGrupo", relatorio)
      }
}

interface ITabTodasEntradasViewModel : ITabView {
  fun setFiltro(filtro: FiltroNotaEntradaQuery)
  fun getFiltro(): FiltroNotaEntradaQuery
  fun openRelatorio(list: List<NotaEntradaQuery>)
}