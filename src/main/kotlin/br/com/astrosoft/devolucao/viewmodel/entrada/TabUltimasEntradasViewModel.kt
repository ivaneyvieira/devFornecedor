package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.devolucao.model.beans.FiltroUltimaNotaEntrada
import br.com.astrosoft.devolucao.model.beans.Loja
import br.com.astrosoft.devolucao.model.beans.UltimaNotaEntrada
import br.com.astrosoft.devolucao.model.beans.UltimaNotaEntradaGrupo
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaUltimaNota
import br.com.astrosoft.devolucao.model.reports.RelatorioUltimasNotas
import br.com.astrosoft.devolucao.model.reports.RelatorioUltimasNotasGrupo
import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail

class TabUltimasEntradasViewModel(val viewModel: EntradaViewModel) {
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
    val cstDifList = listNotas.filter { it.cstDif == "N" }.map { nota ->
      UltimaNotaEntradaGrupo("Diferencas de CST", nota, nota.cstn, nota.cstp)
    }
    val icmsDifList = listNotas.filter { it.icmsDif == "N" }.map { nota ->
      UltimaNotaEntradaGrupo("Diferencas de ICMS", nota, nota.icmsn.format(), nota.icmsp.format())
    }
    val ipiDifList = listNotas.filter { it.ipiDif == "N" }.map { nota ->
      UltimaNotaEntradaGrupo("Diferencas de IPI", nota, nota.ipin.format(), nota.ipip.format())
    }
    val mvaDifList = listNotas.filter { it.mvaDif == "N" }.map { nota ->
      UltimaNotaEntradaGrupo("Diferencas de MVA", nota, nota.mvan.format(), nota.mvap.format())
    }
    val ncmDifList = listNotas.filter { it.ncmDif == "N" }.map { nota ->
      UltimaNotaEntradaGrupo("Diferencas de NCM", nota, nota.ncmn, nota.ncmp)
    }
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