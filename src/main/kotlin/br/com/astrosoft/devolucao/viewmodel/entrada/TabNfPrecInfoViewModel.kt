package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.devolucao.model.beans.FiltroNfPrecEntrada
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

class TabNfPrecInfoViewModel(val viewModel: EntradaViewModel) {
  val subView
    get() = viewModel.view.tabNfPrecInfoViewModel

  fun openDlgRelatorio() = viewModel.exec {
    saci.queryNfPrec(subView.getFiltro())
    subView.openRelatorio()
  }

  fun imprimeRelatorio(listNotas: List<NfPrecEntrada>) = viewModel.exec {
    if (listNotas.isEmpty()) fail("Nenhuma nota selecionada")
    val relatorio = RelatorioNfPrec.processaRelatorio(listNotas, false)
    viewModel.showReport("nfPrecificacao", relatorio)
  }

  fun imprimeRelatorioResumo(listNotas: List<NfPrecEntrada>) {
    val refPrdDifList = listNotas.filter { it.refPrdDif == "N" }.map { nota ->
      NfPrecEntradaGrupo("Diferenças de Ref", nota, nota.refPrdn, nota.refPrdp)
    }
    val barCodeDifList = listNotas.filter { it.barcodeDif == "N" }.map { nota ->
      NfPrecEntradaGrupo("Diferenças de Barras", nota, nota.barcoden, nota.barcodep)
    }
    val ncmDifList = listNotas.filter { it.ncmDif == "N" }.map { nota ->
      NfPrecEntradaGrupo("Diferenças de NCM", nota, nota.ncmn, nota.ncmp)
    }
    val listaRelatorio = refPrdDifList + barCodeDifList + ncmDifList
    val relatorio = RelatorioNfPrecGrupo.processaRelatorio(listaRelatorio)
    viewModel.showReport("nfPrecificacaoGrupo", relatorio)
  }

  fun geraPlanilha(notas: List<NfPrecEntrada>): ByteArray {
    val planilha = PlanilhaNfPrec(false)
    return planilha.grava(notas)
  }

  fun findLojas(): List<Loja> {
    return Loja.allLojas().sortedBy { it.no }
  }

  fun findNotas(filtro: FiltroNfPrecEntrada): List<NfPrecEntrada> {
    return NfPrecEntrada.findNotas(filtro)
  }
}

interface ITabNfPrecInfoViewModel : ITabView {
  fun setFiltro(filtro: FiltroNfPrecEntrada)
  fun getFiltro(): FiltroNfPrecEntrada
  fun openRelatorio()
}