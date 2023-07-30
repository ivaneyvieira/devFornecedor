package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.devolucao.model.beans.*
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaCte
import br.com.astrosoft.devolucao.model.reports.RelatorioCte
import br.com.astrosoft.devolucao.model.reports.RelatorioNfCte
import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail

class TabCteViewModel(val viewModel: EntradaViewModel) {
  val subView
    get() = viewModel.view.tabCteViewModel

  fun openDlgRelatorio() = viewModel.exec {
    saci.queryCte(subView.getFiltro())
    subView.openRelatorio()
  }

  fun findLojas(): List<Loja> {
    return Loja.allLojas().sortedBy { it.no }.toList()
  }

  fun findNotas(filtro: FiltroDialog): List<NfEntradaFrete> {
    return NfEntradaFrete.findNotas(filtro).toList()
  }

  fun findTabName(carrno: Int?, codTab: Int?): String {
    codTab ?: return ""
    carrno ?: return ""
    return saci.findTabName(carrno, codTab)?.nome ?: "Não Encontrada"
  }

  fun imprimeRelatorio(listNotas: List<NfEntradaFrete>) {
    if (listNotas.isEmpty()) fail("Nenhuma nota selecionada")
    val relatorio = RelatorioCte.processaRelatorio(listNotas)
    viewModel.showReport("notacte", relatorio)
  }

  fun imprimeRelatorioResumo(listNotas: List<NfEntradaFrete>) {
    val listaRelatorioFrete = listNotas.filter { it.freteDif }.map { nota ->
      NfFreteGrupo(
        nomeGrupo = "Diferenças de Frete",
        nota = nota,
        cte = nota.cte,
        valorNota = nota.valorCte.format(),
        valorCalculado = nota.totalFrete.format(),
      )
    }

    val listaRelatorio = listaRelatorioFrete
    val relatorio = RelatorioNfCte.processaRelatorio(listaRelatorio)
    viewModel.showReport("notaCte", relatorio)
  }

  fun geraPlanilha(notas: List<NfEntradaFrete>): ByteArray {
    val planilha = PlanilhaCte()
    return planilha.grava(notas)
  }
}

interface ITabCteViewModel : ITabView {
  fun setFiltro(filtro: FiltroNFEntradaFrete)
  fun getFiltro(): FiltroNFEntradaFrete
  fun openRelatorio()
}