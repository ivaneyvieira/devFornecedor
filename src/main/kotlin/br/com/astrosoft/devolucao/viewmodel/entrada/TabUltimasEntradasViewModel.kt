package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.devolucao.model.beans.FiltroUltimaNotaEntrada
import br.com.astrosoft.devolucao.model.beans.Loja
import br.com.astrosoft.devolucao.model.beans.UltimaNotaEntrada
import br.com.astrosoft.devolucao.model.beans.UltimaNotaEntradaGrupo
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaUltimaNota
import br.com.astrosoft.devolucao.model.reports.RelatorioUltimasNotas
import br.com.astrosoft.devolucao.model.reports.RelatorioUltimasNotasGrupo
import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail

class TabUltimasEntradasViewModel(val viewModel: EntradaViewModel) {
  private var filtro: FiltroUltimaNotaEntrada? = null
  val subView
    get() = viewModel.view.tabUltimasEntradasViewModel

  fun openDlgRelatorio() = viewModel.exec {
    saci.queryUltimaNota(subView.getFiltro())
    subView.openRelatorio()
  }

  fun imprimeRelatorio(listNotas: List<UltimaNotaEntrada>) = viewModel.exec {
    if (listNotas.isEmpty()) fail("Nenhuma nota selecionada")
    val relatorio = RelatorioUltimasNotas.processaRelatorio(listNotas)
    viewModel.showReport("nfPrecificacao", relatorio)
  }

  fun imprimeRelatorioResumo(listNotas: List<UltimaNotaEntrada>) {
    val cstDifList = listNotas.filter { it.cstDif == "N" }.map { UltimaNotaEntradaGrupo("Diferencas de CST", it) }
    val icmsDifList = listNotas.filter { it.icmsDif == "N" }.map { UltimaNotaEntradaGrupo("Diferencas de ICMS", it) }
    val ipiDifList = listNotas.filter { it.ipiDif == "N" }.map { UltimaNotaEntradaGrupo("Diferencas de IPI", it) }
    val mvaDifList = listNotas.filter { it.mvaDif == "N" }.map { UltimaNotaEntradaGrupo("Diferencas de MVA", it) }
    val ncmDifList = listNotas.filter { it.ncmDif == "N" }.map { UltimaNotaEntradaGrupo("Diferencas de NCM", it) }
    val listaRelatorio = icmsDifList + ipiDifList + cstDifList + mvaDifList + ncmDifList
    val relatorio = RelatorioUltimasNotasGrupo.processaRelatorio(listaRelatorio)
    viewModel.showReport("nfPrecificacaoGrupo", relatorio)
  }

  fun geraPlanilha(notas: List<UltimaNotaEntrada>): ByteArray {
    val planilha = PlanilhaUltimaNota()
    return planilha.grava(notas)
  }

  fun findLojas(): List<Loja> {
    return Loja.allLojas().sortedBy { it.no }
  }

  fun findNotas(filtro: FiltroUltimaNotaEntrada): List<UltimaNotaEntrada> {
    return UltimaNotaEntrada.findNotas(filtro)
  }
}

interface ITabUltimasEntradasViewModel : ITabView {
  fun setFIltro(filtro: FiltroUltimaNotaEntrada)
  fun getFiltro(): FiltroUltimaNotaEntrada
  fun openRelatorio()
}