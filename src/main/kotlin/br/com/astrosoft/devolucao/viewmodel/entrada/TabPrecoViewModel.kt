package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.devolucao.model.beans.*
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaPrecoDif
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaPrecoKit
import br.com.astrosoft.devolucao.model.reports.RelatorioNfPrecGrupo
import br.com.astrosoft.devolucao.model.reports.RelatorioPrecoDif
import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.model.MonitorHandler
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail

class TabPrecoViewModel(val viewModel: EntradaViewModel) {
  val subView
    get() = viewModel.view.tabPrecoViewModel

  fun openDlgRelatorio(monitor: MonitorHandler? = null) = viewModel.exec {
    saci.queryNfPrec(subView.getFiltro(), monitor)
    subView.openRelatorio()
  }

  fun imprimeRelatorio(listNotas: List<NfPrecEntrada>) = viewModel.exec {
    if (listNotas.isEmpty()) fail("Nenhuma nota selecionada")
    val relatorio = RelatorioPrecoDif.processaRelatorio(listNotas)
    viewModel.showReport("nfPrecificacao", relatorio)
  }

  fun imprimeRelatorioResumo(listNotas: List<NfPrecEntrada>) {
    val listaRelatorioPed = listNotas.filter { it.precoDif != "S" }.map { nota ->
      NfPrecEntradaGrupo(
        nomeGrupo = "Diferenças de Preço Ped",
        nota = nota,
        pedidoCompra = nota.pedidoCompra ?: 0,
        valorNota = nota.precon.format(),
        valorPrecificacao = nota.precop.format()
      )
    }
    val listaRelatorioPrec = listNotas.filter { it.precon.format() != it.precopc.format() }.map { nota ->
      NfPrecEntradaGrupo(
        nomeGrupo = "Diferenças de Preço Prec",
        nota = nota,
        pedidoCompra = nota.pedidoCompra ?: 0,
        valorNota = nota.precon.format(),
        valorPrecificacao = nota.precopc.format()
      )
    }
    val listaRelatorio = listaRelatorioPed + listaRelatorioPrec
    val relatorio = RelatorioNfPrecGrupo.processaRelatorio(listaRelatorio, fiscal = false)
    viewModel.showReport("nfPrecificacaoGrupo", relatorio)
  }

  fun geraPlanilha(notas: List<NfPrecEntrada>): ByteArray {
    val planilha = PlanilhaPrecoDif()
    return planilha.grava(notas)
  }

  fun geraPlanilhaKit(notas: List<NfPrecEntrada>): ByteArray {
    val planilha = PlanilhaPrecoKit()
    val produtoKits = notas.flatMap { it.explodeKits() }.agrupaCodigo()
    return planilha.grava(produtoKits)
  }

  fun findLojas(): List<Loja> {
    return Loja.allLojas().sortedBy { it.no }
  }

  fun findNotas(filtro: FiltroRelatorio): List<NfPrecEntrada> {
    return NfPrecEntrada.findNotas(filtro).toList()
  }
}

interface ITabPrecoViewModel : ITabView {
  fun setFiltro(filtro: FiltroRelatorio)
  fun getFiltro(): FiltroRelatorio
  fun openRelatorio()
}