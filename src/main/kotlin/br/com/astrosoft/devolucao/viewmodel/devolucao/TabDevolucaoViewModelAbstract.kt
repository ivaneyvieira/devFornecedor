package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.devolucao.model.beans.*
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaFornecedorResumo
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaNotas
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaNotasPedidos
import br.com.astrosoft.devolucao.model.reports.RelatorioNotaDevolucao
import br.com.astrosoft.devolucao.viewmodel.devolucao.Serie.PED
import br.com.astrosoft.framework.model.FileAttach
import br.com.astrosoft.framework.model.MailGMail
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail

abstract class TabDevolucaoViewModelAbstract<T : IDevolucaoAbstractView>(val viewModel: DevolucaoAbstractViewModel<T>) :
        IEmailView {
  protected abstract val subView: ITabNota

  fun imprimirNotaDevolucao(notas: List<NotaSaida>, resumida: Boolean = false) = viewModel.exec {
    notas.ifEmpty {
      fail("Nenhuma item foi selecionado")
    }
    subView.imprimeSelecionados(notas, resumida)
  }

  fun imprimirRelatorioFornecedor(notas: List<NotaSaida>) = viewModel.exec {
    notas.ifEmpty {
      fail("Nenhuma item foi selecionado")
    }
    subView.imprimirRelatorioFornecedor(notas)
  }

  fun imprimirRelatorio(notas: List<NotaSaida>) = viewModel.exec {
    notas.ifEmpty {
      fail("Nenhuma item foi selecionado")
    }
    subView.imprimirRelatorio(notas)
  }

  override fun updateView() = viewModel.exec {
    subView.updateGrid(listFornecedores())
  }

  private fun listFornecedores(): List<Fornecedor> {
    subView.setFiltro("")
    NotaSaida.updateNotasDevolucao(subView)
    return NotaSaida.findFornecedores()
  }

  fun editRmk(nota: NotaSaida) {
    subView.editRmk(nota) { notaSaida ->
      notaSaida.saveRmk()
    }
  }

  fun editFile(nota: NotaSaida) = viewModel.exec {
    subView.editFile(nota) { nfFile ->
      nfFile.saveFile()
    }
  }

  fun updateFiltro() {
    val filtro: String = subView.filtro()
    val resultList = NotaSaida.findFornecedores().filtro(filtro)

    subView.updateGrid(resultList)
  }

  fun deleteFile(file: NFFile?) = viewModel.exec {
    file?.apply {
      this.delete()
    }
  }

  private fun List<Fornecedor>.filtro(txt: String): List<Fornecedor> {
    return this.filter {
      val filtroNum = txt.toIntOrNull() ?: 0
      it.custno == filtroNum || it.vendno == filtroNum || it.fornecedor.startsWith(txt, ignoreCase = true)
    }
  }

  fun geraPlanilha(notas: List<NotaSaida>, serie: Serie): ByteArray {
    return if (serie == PED) {
      val planilha = PlanilhaNotasPedidos()
      planilha.grava(notas)
    }
    else {
      val planilha = PlanilhaNotas()
      planilha.grava(notas)
    }
  }

  override fun listEmail(fornecedor: Fornecedor?): List<String> {
    return fornecedor?.listEmail().orEmpty()
  }

  fun enviarEmail(notas: List<NotaSaida>) = viewModel.exec {
    if (notas.isEmpty()) fail("Nenhuma nota selecionada")
    subView.enviaEmail(notas)
  }

  override fun enviaEmail(gmail: EmailGmail, notas: List<NotaSaida>) = viewModel.exec {
    val mail = MailGMail()
    val filesReport = createReports(gmail, notas)
    val filesPlanilha = createPlanilha(gmail, notas, subView.serie)
    val filesAnexo = createAnexos(gmail, notas)
    val enviadoComSucesso =
            mail.sendMail(gmail.email, gmail.assunto, gmail.msgHtml, filesReport + filesPlanilha + filesAnexo)
    if (enviadoComSucesso) {
      val idEmail = EmailDB.newEmailId()
      notas.forEach { nota ->
        nota.salvaEmail(gmail, idEmail)
      }
    }
    else fail("Erro ao enviar e-mail")
  }

  private fun createAnexos(gmail: EmailGmail, notas: List<NotaSaida>): List<FileAttach> {
    return when (gmail.anexos) {
      "S"  -> {
        notas.flatMap { nota ->
          nota.listFiles().map { nfile ->
            FileAttach(nfile.nome, nfile.file)
          }
        }
      }

      else -> emptyList()
    }
  }

  private fun createPlanilha(gmail: EmailGmail, notas: List<NotaSaida>, serie: Serie): List<FileAttach> {
    return when (gmail.planilha) {
      "S"  -> {
        notas.map { nota ->
          val planilha = geraPlanilha(listOf(nota), serie)
          FileAttach("Planilha da Nota ${nota.nota.replace("/", "_")}.xlsx", planilha)
        }
      }

      else -> emptyList()
    }
  }

  private fun createReports(gmail: EmailGmail, notas: List<NotaSaida>): List<FileAttach> {
    val relatoriosCompleto = when (gmail.relatorio) {
      "S"  -> {
        notas.map { nota ->
          val report = RelatorioNotaDevolucao.processaRelatorio(listOf(nota), false)
          FileAttach("Relatorio da nota ${nota.nota.replace("/", "_")}.pdf", report)
        }
      }

      else -> emptyList()
    }
    val relatoriosResumido = when (gmail.relatorioResumido) {
      "S"  -> {
        notas.map { nota ->
          val report = RelatorioNotaDevolucao.processaRelatorio(listOf(nota), true)
          FileAttach("Relatorio da nota ${nota.nota.replace("/", "_")}.pdf.pdf", report)
        }
      }

      else -> emptyList()
    }
    return relatoriosCompleto + relatoriosResumido
  }

  fun mostrarEmailNota(nota: NotaSaida?) = viewModel.exec {
    nota ?: fail("Não há nenhuma nota selecionada")
    val emails = nota.listEmailNota()
    subView.selecionaEmail(nota, emails)
  }

  fun editRmkVend(fornecedor: Fornecedor) {
    subView.editRmkVend(fornecedor) { forn ->
      forn.saveRmkVend()
    }
  }

  fun imprimirRelatorioResumido(fornecedores: List<Fornecedor>) = viewModel.exec {
    fornecedores.ifEmpty {
      fail("Nenhuma fornecedor foi selecionada")
    }
    subView.imprimirRelatorioResumido(fornecedores)
  }

  fun geraPlanilhaResumo(fornecedores: List<Fornecedor>): ByteArray? {
    val planilha = PlanilhaFornecedorResumo()
    return planilha.grava(fornecedores)
  }
}

enum class SimNao(val value: String) {
  SIM("S"), NAO("N"), NONE("")
}

enum class Serie(val value: String) {
  Serie01("1"), Serie66("66"), PED("PED"), ENT("ENT"), AJT("AJT"), FIN("FIN"), VAZIO("")
}

interface IFiltro {
  val serie: Serie
  val pago66: SimNao
  val pago01: SimNao
  val coleta01: SimNao
  val remessaConserto: SimNao
}

interface ITabNota : ITabView, IFiltro {
  fun updateGrid(itens: List<Fornecedor>)
  fun itensSelecionados(): List<Fornecedor>
  fun imprimeSelecionados(notas: List<NotaSaida>, resumida: Boolean)
  fun imprimirRelatorioFornecedor(notas: List<NotaSaida>)
  fun imprimirRelatorio(notas: List<NotaSaida>)
  fun imprimirRelatorioResumido(fornecedores: List<Fornecedor>)
  fun editRmk(nota: NotaSaida, save: (NotaSaida) -> Unit)
  fun editFile(nota: NotaSaida, insert: (NFFile) -> Unit)
  fun filtro(): String
  fun setFiltro(txt: String)
  fun enviaEmail(notas: List<NotaSaida>)
  fun selecionaEmail(nota: NotaSaida, emails: List<EmailDB>)
  fun editRmkVend(fornecedor: Fornecedor, save: (Fornecedor) -> Unit)
}

