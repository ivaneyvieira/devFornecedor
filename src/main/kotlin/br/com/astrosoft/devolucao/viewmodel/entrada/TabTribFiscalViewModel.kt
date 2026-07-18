package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.devolucao.model.beans.EDiferencaStr.T
import br.com.astrosoft.devolucao.model.beans.FiltroRelatorio
import br.com.astrosoft.devolucao.model.beans.Loja
import br.com.astrosoft.devolucao.model.beans.NfPrecEntrada
import br.com.astrosoft.devolucao.model.beans.NfPrecEntradaGrupo
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaNfPrec
import br.com.astrosoft.devolucao.model.reports.RelatorioNfPrec
import br.com.astrosoft.devolucao.model.reports.RelatorioNfPrecGrupo
import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.model.MonitorHandler
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail

class TabTribFiscalViewModel(val viewModel: EntradaViewModel) {
  val subView
    get() = viewModel.view.tabTribFiscalViewModel

  fun openDlgRelatorio(monitor: MonitorHandler? = null) = viewModel.exec {
    saci.queryNfPrec(subView.getFiltro(), monitor)
    subView.openRelatorio()
  }

  fun imprimeRelatorio(listNotas: List<NfPrecEntrada>) = viewModel.exec {
    if (listNotas.isEmpty()) fail("Nenhuma nota selecionada")
    val relatorio = RelatorioNfPrec.processaRelatorio(listNotas, true)
    viewModel.showReport("nfPrecificacao", relatorio)
  }

  fun imprimeRelatorioResumo(listNotas: List<NfPrecEntrada>) {
    val cstDifList = listNotas.filter { it.cstDif != "S" }.map { nota ->
      NfPrecEntradaGrupo(
        nomeGrupo = "Diferenças de CST",
        nota = nota,
        pedidoCompra = nota.pedidoCompra ?: 0,
        valorNota = nota.cstn ?: "",
        valorPrecificacao = nota.cstp ?: "",
        valNFO = ""
      )
    }
    val freteDifList = listNotas.filter { it.freteDif != "S" }.map { nota ->
      NfPrecEntradaGrupo(
        nomeGrupo = "Diferenças de Frete",
        nota = nota,
        pedidoCompra = nota.pedidoCompra ?: 0,
        valorNota = nota.freten.format(),
        valorPrecificacao = nota.fretep.format(),
        valNFO = ""
      )
    }
    val icmsDifList = listNotas.filter { it.icmsDif != "S" }.map { nota ->
      NfPrecEntradaGrupo(
        nomeGrupo = "Diferenças de ICMS",
        nota = nota,
        pedidoCompra = nota.pedidoCompra ?: 0,
        valorNota = nota.icmsRN.format(),
        valorPrecificacao = nota.icmsp.format(),
        valNFO = nota.precon.format()
      )
    }
    val ipiDifList = listNotas.filter { it.ipiDif != "S" }.map { nota ->
      NfPrecEntradaGrupo(
        nomeGrupo = "Diferenças de IPI",
        nota = nota,
        pedidoCompra = nota.pedidoCompra ?: 0,
        valorNota = nota.ipin.format(),
        valorPrecificacao = nota.ipip.format(),
        valNFO = nota.precon.format()
      )
    }
    val mvaDifList = listNotas.filter { it.mvaDif != "S" }.map { nota ->
      NfPrecEntradaGrupo(
        nomeGrupo = "Diferenças de MVA",
        nota = nota,
        pedidoCompra = nota.pedidoCompra ?: 0,
        valorNota = nota.mvanAprox.format(),
        valorPrecificacao = nota.mvap.format(),
        valNFO = ""
      )
    }
    val listaRelatorio = freteDifList + icmsDifList + ipiDifList + cstDifList + mvaDifList
    val relatorio = RelatorioNfPrecGrupo.processaRelatorio(notas = listaRelatorio, fiscal = true)
    viewModel.showReport("nfPrecificacaoGrupo", relatorio)
  }

  fun geraPlanilha(notas: List<NfPrecEntrada>): ByteArray {
    val planilha = PlanilhaNfPrec(true)
    return planilha.grava(notas)
  }

  fun findLojas(): List<Loja> {
    return Loja.allLojas().sortedBy { it.no }
  }

  fun findNotas(filtro: FiltroRelatorio, monitor: MonitorHandler? = null): List<NfPrecEntrada> {
    val cst = filtro.cst
    filtro.cst = T
    return NfPrecEntrada.findNotas(filtro, monitor).filter { nota ->
      (cst == T) || (cst.str == nota.cstDifxn) || (cst.str == nota.cstDifnp)
    }.toList()
  }
}

interface ITabTribFiscalViewModel : ITabView {
  fun setFiltro(filtro: FiltroRelatorio)
  fun getFiltro(): FiltroRelatorio
  fun openRelatorio()
}