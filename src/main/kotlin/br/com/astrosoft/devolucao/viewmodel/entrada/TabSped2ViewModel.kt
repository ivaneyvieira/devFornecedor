package br.com.astrosoft.devolucao.viewmodel.entrada

import br.com.astrosoft.devolucao.model.beans.*
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaNfPrec
import br.com.astrosoft.devolucao.model.reports.RelatorioNfPrec
import br.com.astrosoft.devolucao.model.reports.RelatorioNfPrecGrupo
import br.com.astrosoft.devolucao.model.saci
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail

class TabSped2ViewModel(val viewModel: EntradaViewModel) {
  val subView
    get() = viewModel.view.tabSped2ViewModel

  fun openDlgRelatorio() = viewModel.exec {
    FornecedorNdd.updateNotas()
    saci.queryNfPrec(subView.getFiltro())
    subView.openRelatorio()
  }

  fun imprimeRelatorio(listNotas: List<NfPrecEntrada>) = viewModel.exec {
    if (listNotas.isEmpty()) fail("Nenhuma nota selecionada")
    val relatorio = RelatorioNfPrec.processaRelatorio(listNotas, true)
    viewModel.showReport("nfPrecificacao", relatorio)
  }

  fun imprimeRelatorioResumo(listNotas: List<NfPrecEntrada>) {
    val cfopDifList = listNotas.filter { it.cfopDifxp == "N" }.map { nota ->
      NfPrecEntradaGrupo(
        nomeGrupo = "Diferenças de CFOP",
        nota = nota,
        pedidoCompra = nota.pedidoCompra ?: 0,
        valorNota = nota.cfop ?: "",
        valorPrecificacao = nota.cfopx ?: ""
      )
    }

    val cstDifnpList = listNotas.filter { it.cstDifnp == "N" }.map { nota ->
      NfPrecEntradaGrupo(
        nomeGrupo = "Diferenças de CST Nota Cadastro",
        nota = nota,
        pedidoCompra = nota.pedidoCompra ?: 0,
        valorNota = nota.cstIcms ?: "",
        valorPrecificacao = nota.cstp ?: ""
      )
    }

    val cstDifxnList = listNotas.filter { it.cstDifxn == "N" }.map { nota ->
      NfPrecEntradaGrupo(
        nomeGrupo = "Diferenças de CST XML Nota",
        nota = nota,
        pedidoCompra = nota.pedidoCompra ?: 0,
        valorNota = nota.cstIcms ?: "",
        valorPrecificacao = nota.cstx ?: ""
      )
    }

    val mvaDifnpList = listNotas.filter { it.mvaDifnp == "N" }.map { nota ->
      NfPrecEntradaGrupo(
        nomeGrupo = "Diferenças de MVA Nota Cadastro",
        nota = nota,
        pedidoCompra = nota.pedidoCompra ?: 0,
        valorNota = nota.mvan.format(),
        valorPrecificacao = nota.mvap.format()
      )
    }

    val mvaDifxnList = listNotas.filter { it.mvaDifxn == "N" }.map { nota ->
      NfPrecEntradaGrupo(
        nomeGrupo = "Diferenças de MVA Nota XML",
        nota = nota,
        pedidoCompra = nota.pedidoCompra ?: 0,
        valorNota = nota.mvan.format(),
        valorPrecificacao = nota.mvax.format()
      )
    }

    val icmsDifxnList = listNotas.filter { it.icmsDifxn == "N" }.map { nota ->
      NfPrecEntradaGrupo(
        nomeGrupo = "Diferenças de ICMS Nota XML",
        nota = nota,
        pedidoCompra = nota.pedidoCompra ?: 0,
        valorNota = nota.vlIcmsx.format(),
        valorPrecificacao = nota.vlIcms.format()
      )
    }

    val ipiDifxnList = listNotas.filter { it.ipiDifxn == "N" }.map { nota ->
      NfPrecEntradaGrupo(
        nomeGrupo = "Diferenças de IPI Nota XML",
        nota = nota,
        pedidoCompra = nota.pedidoCompra ?: 0,
        valorNota = nota.vlIpix.format(),
        valorPrecificacao = nota.vlIpi.format()
      )
    }

    val baseSubstxnList = listNotas.filter { it.baseSubstxn == "N" }.map { nota ->
      NfPrecEntradaGrupo(
        nomeGrupo = "Diferenças de BASE ST",
        nota = nota,
        pedidoCompra = nota.pedidoCompra ?: 0,
        valorNota = nota.baseSubstx.format(),
        valorPrecificacao = nota.baseSubst.format()
      )
    }

    val vlIcmsSubstxnList = listNotas.filter { it.vlIcmsSubstxn == "N" }.map { nota ->
      NfPrecEntradaGrupo(
        nomeGrupo = "Diferenças de Valor ST",
        nota = nota,
        pedidoCompra = nota.pedidoCompra ?: 0,
        valorNota = nota.vlIcmsSubstx.format(),
        valorPrecificacao = nota.vlIcmsSubst.format()
      )
    }

    val vlTotalxnList = listNotas.filter { it.vlTotalxn == "N" }.map { nota ->
      NfPrecEntradaGrupo(
        nomeGrupo = "Diferenças de Valor Total",
        nota = nota,
        pedidoCompra = nota.pedidoCompra ?: 0,
        valorNota = nota.vlTotalx.format(),
        valorPrecificacao = nota.vlTotal.format()
      )
    }

    val listaRelatorio = cfopDifList + cstDifnpList + cstDifxnList + mvaDifnpList + mvaDifxnList + icmsDifxnList +
        ipiDifxnList + baseSubstxnList + vlIcmsSubstxnList + vlTotalxnList
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

interface ITabSped2ViewModel : ITabView {
  fun setFiltro(filtro: FiltroRelatorio)
  fun getFiltro(): FiltroRelatorio
  fun openRelatorio()
}