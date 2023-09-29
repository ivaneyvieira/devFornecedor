package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.devolucao.model.beans.*
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaRefFiscal
import br.com.astrosoft.devolucao.model.reports.RelatorioNfPrec
import br.com.astrosoft.devolucao.model.reports.RelatorioNfPrecGrupo
import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.model.MonitorHandler
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail

class TabRefFiscalViewModel(val viewModel: EntradaViewModel) {
  val subView
    get() = viewModel.view.tabRefFiscalViewModel

  fun openDlgRelatorio(monitor: MonitorHandler? = null) = viewModel.exec {
    saci.queryNfPrec(subView.getFiltro(), monitor)
    subView.openRelatorio()
  }

  fun imprimeRelatorio(listNotas: List<NfPrecEntrada>) = viewModel.exec {
    if (listNotas.isEmpty()) fail("Nenhuma nota selecionada")
    val relatorio = RelatorioNfPrec.processaRelatorio(listNotas, false)
    viewModel.showReport("nfPrecificacao", relatorio)
  }

  fun imprimeRelatorioResumo(listNotas: List<NfPrecEntrada>) {
    val refPrdDifxList = listNotas.filter { it.refPrdDifx == "N" }.map { nota ->
      NfPrecEntradaGrupo(
        nomeGrupo = "Diferenças de REFERÊNCIA XML x Cadastro",
        nota = nota,
        pedidoCompra = nota.pedidoCompra ?: 0,
        valorNota = nota.refPrdx ?: "",
        valorPrecificacao = nota.refPrdp ?: ""
      )
    }
    val barcodeDifxpList = listNotas.filter { it.barcodeDifcp == "N" }.map { nota ->
      NfPrecEntradaGrupo(
        nomeGrupo = "Diferenças de BARRAS XML x Cadastro",
        nota = nota,
        pedidoCompra = nota.pedidoCompra ?: 0,
        valorNota = nota.barcodex ?: "",
        valorPrecificacao = nota.barcodep ?: ""
      )
    }
    val barcodeDifpcList = listNotas.filter { it.barcodeDifcx == "N" }.map { nota ->
      NfPrecEntradaGrupo(
        nomeGrupo = "Diferenças de BARRAS GITN x Cadastro",
        nota = nota,
        pedidoCompra = nota.pedidoCompra ?: 0,
        valorNota = nota.barcodex ?: "",
        valorPrecificacao = nota.barcodec ?: ""
      )
    }
    val ncmDifxList = listNotas.filter { it.ncmDifx == "N" }.map { nota ->
      NfPrecEntradaGrupo(
        nomeGrupo = "Diferenças de NCM XML x Cadastro",
        nota = nota,
        pedidoCompra = nota.pedidoCompra ?: 0,
        valorNota = nota.ncmx ?: "",
        valorPrecificacao = nota.ncmx ?: ""
      )
    }
    val listaRelatorio = refPrdDifxList + barcodeDifxpList + barcodeDifpcList + ncmDifxList
    val relatorio = RelatorioNfPrecGrupo.processaRelatorio(listaRelatorio, false)
    viewModel.showReport("nfPrecificacaoGrupo", relatorio)
  }

  fun geraPlanilha(notas: List<NfPrecEntrada>): ByteArray {
    val planilha = PlanilhaRefFiscal()
    return planilha.grava(notas)
  }

  fun findLojas(): List<Loja> {
    return Loja.allLojas().sortedBy { it.no }
  }

  fun findNotas(filtro: FiltroRelatorio): List<NfPrecEntrada> {
    return NfPrecEntrada.findNotas(filtro).toList()
  }

  fun refXml(monitor: MonitorHandler? = null) {
    val itens = subView.selectItens()
    if (itens.isEmpty()) fail("Nenhum item selecionado")

    viewModel.showQuestion("Confirma a atualização de REFERÊNCIA para os itens selecionados?") {
      val list = itens.map { nf ->
        PrdRef(
          prdno = nf.prod,
          grade = nf.grade,
          prdrefname = nf.refPrdx ?: "",
          prdrefno = nf.refPrdx ?: ""
        )
      }.distinct()
      PrdRef.add(list)
      saci.queryNfPrec(subView.getFiltro(), monitor)
      subView.updateGrid()
    }
  }

  fun barrasXml(monitor: MonitorHandler? = null) {
    val itens = subView.selectItens()
    if (itens.isEmpty()) fail("Nenhum item selecionado")

    viewModel.showQuestion("Confirma a atualização de CÓD BARRAS paras os itens selecionados?") {
      val list = itens.map { nf ->
        PrdBar(
          prdno = nf.prod,
          grade = nf.grade,
          barcode = nf.barcodex ?: ""
        )
      }.distinct()
      PrdBar.add(list)
      saci.queryNfPrec(subView.getFiltro(), monitor)
      subView.updateGrid()
    }
  }

  fun ncmXml(monitor: MonitorHandler? = null) {
    val itens = subView.selectItens()
    if (itens.isEmpty()) fail("Nenhum item selecionado")

    viewModel.showQuestion("Confirma a atualização do NCM para os itens selecionados?") {
      itens.forEach { nf ->
        val ncm = nf.ncmx
        if (!ncm.isNullOrEmpty())
          saci.addNCM(nf.prod, ncm)
      }
      saci.queryNfPrec(subView.getFiltro(), monitor)
      subView.updateGrid()
    }
  }
}

interface ITabRefFiscalViewModel : ITabView {
  fun setFiltro(filtro: FiltroRelatorio)
  fun getFiltro(): FiltroRelatorio
  fun openRelatorio()

  fun selectItens(): List<NfPrecEntrada>
  fun updateGrid()
}