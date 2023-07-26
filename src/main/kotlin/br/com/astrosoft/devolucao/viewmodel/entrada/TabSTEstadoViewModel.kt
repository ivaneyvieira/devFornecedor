package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.devolucao.model.beans.*
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaNfPrec
import br.com.astrosoft.devolucao.model.reports.RelatorioNfPrec
import br.com.astrosoft.devolucao.model.reports.RelatorioNfPrecGrupo
import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail

class TabSTEstadoViewModel(val viewModel: EntradaViewModel) {
  val subView
    get() = viewModel.view.tabSTEstadoViewModel

  fun openDlgRelatorio() = viewModel.exec {
    FornecedorNdd.updateNotas()
    val filtro = subView.getFiltro()
    saci.queryNfPrec(filtro)
    subView.openRelatorio()
  }

  fun imprimeRelatorio(listNotas: List<NfPrecEntrada>) = viewModel.exec {
    if (listNotas.isEmpty()) fail("Nenhuma nota selecionada")
    val relatorio = RelatorioNfPrec.processaRelatorio(listNotas, true)
    viewModel.showReport("nfPrecificacao", relatorio)
  }

  fun imprimeRelatorioResumo(listNotas: List<NfPrecEntrada>) {
    val cstDifnpList = listNotas.filter { it.cstDifnp == "N" }.map { nota ->
      NfPrecEntradaGrupo(
        nomeGrupo = "Diferenças de CST Nota x Cadastro",
        nota = nota,
        pedidoCompra = nota.pedidoCompra ?: 0,
        valorNota = nota.cstIcms ?: "",
        valorPrecificacao = nota.cstp ?: ""
      )
    }

    val cstDifxnList = listNotas.filter { it.cstDifxn == "N" }.map { nota ->
      NfPrecEntradaGrupo(
        nomeGrupo = "Diferenças de CST Nota x XML",
        nota = nota,
        pedidoCompra = nota.pedidoCompra ?: 0,
        valorNota = nota.cstIcms ?: "",
        valorPrecificacao = nota.cstx ?: ""
      )
    }
    val listaRelatorio = cstDifnpList + cstDifxnList
    val relatorio = RelatorioNfPrecGrupo.processaRelatorio(listaRelatorio, fiscal = false)
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

interface ITabSTEstadoViewModel : ITabView {
  fun setFiltro(filtro: FiltroRelatorio)
  fun getFiltro(): FiltroRelatorio
  fun openRelatorio()
}