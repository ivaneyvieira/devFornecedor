package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.devolucao.model.beans.FiltroRelatorio
import br.com.astrosoft.devolucao.model.beans.Loja
import br.com.astrosoft.devolucao.model.beans.NfPrecEntrada
import br.com.astrosoft.devolucao.model.beans.NfPrecEntradaGrupo
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaPrecoDif
import br.com.astrosoft.devolucao.model.reports.RelatorioNfPrecGrupo
import br.com.astrosoft.devolucao.model.reports.RelatorioPrecoDif
import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail

class TabPrecoPreRecViewModel(val viewModel: EntradaViewModel) {
  val subView
    get() = viewModel.view.tabPrecoPreRecViewModel

  fun openDlgRelatorio() = viewModel.exec {
    saci.queryPreRecebimento(subView.getFiltro())
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

  fun findLojas(): List<Loja> {
    return Loja.allLojas().sortedBy { it.no }
  }

  fun findNotas(filtro: FiltroRelatorio): List<NfPrecEntrada> {
    return NfPrecEntrada.findNotasPreRec(filtro)
  }
}

interface ITabPrecoPreRecViewModel : ITabView {
  fun setFiltro(filtro: FiltroRelatorio)
  fun getFiltro(): FiltroRelatorio
  fun openRelatorio()
}