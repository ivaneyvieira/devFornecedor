package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.devolucao.model.beans.*
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
    val refPrdDifList = listNotas.filter { it.refPrdDif != "S" }.map { nota ->
      NfPrecEntradaGrupo("Diferenças de Ref", nota, nota.pedidoCompra ?: 0, nota.refPrdn ?: "", nota.refPrdp ?: "")
    }
    val freteDifList = listNotas.filter { it.freteDif != "S" }.map { nota ->
      NfPrecEntradaGrupo(
        "Diferenças de Frete",
        nota,
        nota.pedidoCompra ?: 0,
        nota.freten.format(),
        nota.fretep.format()
      )
    }
    val barCodeDifList = listNotas.filter { it.barcodeDif != "S" }.map { nota ->
      NfPrecEntradaGrupo("Diferenças de Barras", nota, nota.pedidoCompra ?: 0, nota.barcoden ?: "", nota.barcodep ?: "")
    }
    val ncmDifList = listNotas.filter { it.ncmDif != "S" }.map { nota ->
      NfPrecEntradaGrupo("Diferenças de NCM", nota, nota.pedidoCompra ?: 0, nota.ncmn ?: "", nota.ncmp ?: "")
    }
    val listaRelatorio = freteDifList + refPrdDifList + barCodeDifList + ncmDifList
    val relatorio = RelatorioNfPrecGrupo.processaRelatorio(listaRelatorio, false)
    viewModel.showReport("nfPrecificacaoGrupo", relatorio)
  }

  fun geraPlanilha(notas: List<NfPrecEntrada>): ByteArray {
    val planilha = PlanilhaNfPrec(false)
    return planilha.grava(notas)
  }

  fun findLojas(): List<Loja> {
    return Loja.allLojas().sortedBy { it.no }
  }

  fun findNotas(filtro: FiltroRelatorio): List<NfPrecEntrada> {
    return NfPrecEntrada.findNotas(filtro)
  }

  fun refXml() {
    val itens = subView.selectItens()
    if (itens.isEmpty()) fail("Nenhum item selecionado")

    viewModel.showQuestion("Confirma a atualização dos itens selecionados?") {
      val list = itens.map { nf ->
        PrdRef(
          prdno = nf.prod,
          grade = nf.grade,
          prdrefname = nf.refPrdx ?: "",
          prdrefno = nf.refPrdx ?: ""
        )
      }.distinct()
      PrdRef.add(list)
      saci.queryNfPrec(subView.getFiltro())
      subView.updateGrid()
    }
  }


  fun barrasXml() {
    val itens = subView.selectItens()
    if (itens.isEmpty()) fail("Nenhum item selecionado")

    viewModel.showQuestion("Confirma a atualização dos itens selecionados?") {
      val list = itens.map { nf ->
        PrdBar(
          prdno = nf.prod,
          grade = nf.grade,
          barcode = nf.barcodex ?: ""
        )
      }.distinct()
      PrdBar.add(list)
      saci.queryNfPrec(subView.getFiltro())
      subView.updateGrid()
    }
  }
}

interface ITabNfPrecInfoViewModel : ITabView {
  fun setFiltro(filtro: FiltroRelatorio)
  fun getFiltro(): FiltroRelatorio
  fun openRelatorio()

  fun selectItens(): List<NfPrecEntrada>
  fun updateGrid()
}