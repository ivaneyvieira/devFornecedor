package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.devolucao.model.beans.*
import br.com.astrosoft.devolucao.model.knfe.parseNotaFiscal
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaNfPrec
import br.com.astrosoft.devolucao.model.reports.RelatorioNfPrec
import br.com.astrosoft.devolucao.model.reports.RelatorioNfPrecGrupo
import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.model.MonitorHandler
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail

class TabXmlTribViewModel(val viewModel: EntradaViewModel) {
  val subView
    get() = viewModel.view.tabXmlTribViewModel

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
        valorNota = nota.barcodep ?: "",
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
    val planilha = PlanilhaNfPrec(false)
    return planilha.grava(notas)
  }

  fun findLojas(): List<Loja> {
    return Loja.allLojas().sortedBy { it.no }
  }

  fun findNotas(filtro: FiltroRelatorio): List<NotaXML> {
    return NfPrecEntrada.findNotas(filtro).toNotaXml()
  }
}

private fun List<NfPrecEntrada>.toNotaXml(): List<NotaXML> {
  return this.groupBy { it.ni }.flatMap { entry ->
    val nf = entry.value.firstOrNull() ?: return@flatMap emptyList()
    val nddXml = saci.findXmlNfe(nf.ni, nf.lj, nf.nfe, nf.serie)
    val nfe = parseNotaFiscal(nddXml?.xml) ?: return@flatMap emptyList()

    nfe.infNFe.detalhes.map { det ->
      NotaXML(
        lj = nf.lj,
        ni = nf.ni,
        data = nf.data,
        dataEmissao = nf.dataEmissao,
        nfe = nf.nfe,
        serie = nf.serie,
        fornCad = nf.fornCad,
        fornNota = nf.fornNota,
        refPrdx = det.prod?.cProd,
        descricaox = det.prod?.xProd,
        cstx = det.imposto?.icms?.let { "${it.orig}${it.cst}" } ?: "",
        mvax = det.imposto?.icms?.mvaST ?: 0.00,
        barcodex = det.prod?.cEANTrib,
        cfopx = det.prod?.cfop,
        alIcmsx = det.imposto?.icms?.pICMS ?: 0.00,
        alPisx = det.imposto?.pis?.pPIS ?: 0.00,
        alCofinsx = det.imposto?.cofins?.pCOFINS ?: 0.00,
        unidadex = det.prod?.uTrib,
        alIpix = det.imposto?.ipi?.pIPI ?: 0.00,
      )
    }
  }
}

interface ITabXmlTribViewModel : ITabView {
  fun setFiltro(filtro: FiltroRelatorio)
  fun getFiltro(): FiltroRelatorio
  fun openRelatorio()
  fun selectItens(): List<NotaXML>
  fun updateGrid()
}