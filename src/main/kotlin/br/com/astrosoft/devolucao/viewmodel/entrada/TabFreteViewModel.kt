package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.devolucao.model.beans.FiltroRelatorio
import br.com.astrosoft.devolucao.model.beans.Loja
import br.com.astrosoft.devolucao.model.beans.NfPrecEntrada
import br.com.astrosoft.devolucao.model.beans.NfPrecEntradaGrupo
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaFreteDif
import br.com.astrosoft.devolucao.model.reports.RelatorioFreteDif
import br.com.astrosoft.devolucao.model.reports.RelatorioNfPrecGrupo
import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail

class TabFreteViewModel(val viewModel: EntradaViewModel) {
  val subView
    get() = viewModel.view.tabFreteViewModel

  fun openDlgRelatorio() = viewModel.exec {
    saci.queryNfPrec(subView.getFiltro())
    subView.openRelatorio()
  }

  fun imprimeRelatorio(listNotas: List<NfPrecEntrada>) = viewModel.exec {
    if (listNotas.isEmpty()) fail("Nenhuma nota selecionada")
    val relatorio = RelatorioFreteDif.processaRelatorio(listNotas)
    viewModel.showReport("nfPrecificacao", relatorio)
  }

  fun imprimeRelatorioResumo(listNotas: List<NfPrecEntrada>) {
    val listaRelatorio = listNotas.filter { it.freteDif != "S" }.map { nota ->
      NfPrecEntradaGrupo("Diferenças de Frete", nota, nota.freten.format(), nota.fretep.format())
    }
    val relatorio = RelatorioNfPrecGrupo.processaRelatorio(listaRelatorio, fiscal = true)
    viewModel.showReport("nfPrecificacaoGrupo", relatorio)
  }

  fun geraPlanilha(notas: List<NfPrecEntrada>): ByteArray {
    val planilha = PlanilhaFreteDif()
    return planilha.grava(notas)
  }

  fun findLojas(): List<Loja> {
    return Loja.allLojas().sortedBy { it.no }
  }

  fun findNotas(filtro: FiltroRelatorio): List<NfPrecEntrada> {
    return NfPrecEntrada.findNotas(filtro).filter {
      it.frete != 0.00 && it.fretep != 0.00 && it.freten != 0.00
    }
  }
}

interface ITabFreteViewModel : ITabView {
  fun setFiltro(filtro: FiltroRelatorio)
  fun getFiltro(): FiltroRelatorio
  fun openRelatorio()
}