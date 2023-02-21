package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.devolucao.model.beans.FiltroRelatorio
import br.com.astrosoft.devolucao.model.beans.Loja
import br.com.astrosoft.devolucao.model.beans.NfPrecEntrada
import br.com.astrosoft.devolucao.model.beans.NfPrecEntradaGrupo
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaNfPrec
import br.com.astrosoft.devolucao.model.reports.RelatorioNfPrec
import br.com.astrosoft.devolucao.model.reports.RelatorioNfPrecGrupo
import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail

class TabNfPrecFiscalViewModel(val viewModel: EntradaViewModel) {
  val subView
    get() = viewModel.view.tabNfPrecFiscalViewModel

  fun openDlgRelatorio() = viewModel.exec {
    saci.queryNfPrec(subView.getFiltro())
    subView.openRelatorio()
  }

  fun imprimeRelatorio(listNotas: List<NfPrecEntrada>) = viewModel.exec {
    if (listNotas.isEmpty()) fail("Nenhuma nota selecionada")
    val relatorio = RelatorioNfPrec.processaRelatorio(listNotas, true)
    viewModel.showReport("nfPrecificacao", relatorio)
  }

  fun imprimeRelatorioResumo(listNotas: List<NfPrecEntrada>) {
    val cstDifList = listNotas.filter { it.cstDif != "S" }.map { nota ->
      NfPrecEntradaGrupo("Diferenças de CST", nota, nota.pedidoCompra ?: 0, nota.cstn, nota.cstp)
    }
    val freteDifList = listNotas.filter { it.freteDif != "S" }.map { nota ->
      NfPrecEntradaGrupo("Diferenças de Frete",
                         nota,
                         nota.pedidoCompra ?: 0,
                         nota.freten.format(),
                         nota.fretep.format())
    }
    val icmsDifList = listNotas.filter { it.icmsDif != "S" }.map { nota ->
      NfPrecEntradaGrupo("Diferenças de ICMS", nota, nota.pedidoCompra ?: 0, nota.icmsRN.format(), nota.icmsp.format())
    }
    val ipiDifList = listNotas.filter { it.ipiDif != "S" }.map { nota ->
      NfPrecEntradaGrupo("Diferenças de IPI", nota, nota.pedidoCompra ?: 0, nota.ipin.format(), nota.ipip.format())
    }
    val mvaDifList = listNotas.filter { it.mvaDif != "S" }.map { nota ->
      NfPrecEntradaGrupo("Diferenças de MVA", nota, nota.pedidoCompra ?: 0, nota.mvanApŕox.format(), nota.mvap.format())
    }
    val listaRelatorio = freteDifList + icmsDifList + ipiDifList + cstDifList + mvaDifList
    val relatorio = RelatorioNfPrecGrupo.processaRelatorio(listaRelatorio, fiscal = true)
    viewModel.showReport("nfPrecificacaoGrupo", relatorio)
  }

  fun geraPlanilha(notas: List<NfPrecEntrada>): ByteArray {
    val planilha = PlanilhaNfPrec(true)
    return planilha.grava(notas)
  }

  fun findLojas(): List<Loja> {
    return Loja.allLojas().sortedBy { it.no }
  }

  fun findNotas(filtro: FiltroRelatorio): List<NfPrecEntrada> {
    return NfPrecEntrada.findNotas(filtro)
  }
}

interface ITabNfPrecFiscalViewModel : ITabView {
  fun setFiltro(filtro: FiltroRelatorio)
  fun getFiltro(): FiltroRelatorio
  fun openRelatorio()
}