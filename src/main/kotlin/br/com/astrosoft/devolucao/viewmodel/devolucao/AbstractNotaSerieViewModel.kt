package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.devolucao.model.beans.EmailBean
import br.com.astrosoft.devolucao.model.beans.EmailEnviado
import br.com.astrosoft.devolucao.model.beans.Fornecedor
import br.com.astrosoft.devolucao.model.beans.NFFile
import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaNotas
import br.com.astrosoft.devolucao.view.reports.RelatorioNotaDevolucao
import br.com.astrosoft.framework.model.FileAttach
import br.com.astrosoft.framework.model.MailGMail
import br.com.astrosoft.framework.viewmodel.fail

abstract class AbstractNotaSerieViewModel(val viewModel: DevFornecedorViewModel) {
  protected abstract val subView: INota
  
  fun imprimirNotaDevolucao(notas: List<NotaSaida>, resumida: Boolean = false) = viewModel.exec {
    notas.ifEmpty {
      fail("Não nenhuma nota selecionada")
    }
    subView.imprimeSelecionados(notas, resumida)
  }
  
  fun updateGridNota() = viewModel.exec {
    subView.updateGrid(listNotaDevolucao())
  }
  
  private fun listNotaDevolucao(): List<Fornecedor> {
    subView.setFiltro("")
    NotaSaida.updateNotasDevolucao(subView.serie, subView.pago66)
    return NotaSaida.findFornecedores()
  }
  
  fun editRmk(nota: NotaSaida) {
    subView.editRmk(nota) {notaSaida ->
      notaSaida.saveRmk()
    }
  }
  
  fun editFile(nota: NotaSaida) = viewModel.exec {
    subView.editFile(nota) {nfFile ->
      nfFile.saveFile()
    }
  }
  
  fun updateFiltro() {
    val filtro: String = subView.filtro()
    val resultList =
      NotaSaida.findFornecedores()
        .filtro(filtro)
    
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
  
  fun geraPlanilha(notas: List<NotaSaida>): ByteArray {
    val planilha = PlanilhaNotas()
    
    return planilha.grava(notas)
  }
  
  fun listEmail(fornecedor: Fornecedor?): List<String> {
    return fornecedor?.listEmail()
      .orEmpty()
  }
  
  fun enviarEmail(notas: List<NotaSaida>) = viewModel.exec {
    if(notas.isEmpty())
      fail("Nenhuma nota selecionada")
    subView.enviaEmail(notas)
  }
  
  fun enviaEmail(bean: EmailBean, notas: List<NotaSaida>) = viewModel.exec {
    val mail = MailGMail()
    val filesReport = createReports(bean, notas)
    val filesPlanilha = createPlanilha(bean, notas)
    val filesAnexo = createAnexos(bean, notas)
    val enviadoComSucesso = mail.sendMail(bean.email,
                                          bean.assunto,
                                          bean.msgHtml,
                                          filesReport + filesPlanilha + filesAnexo)
    if(enviadoComSucesso) {
      val idEmail = EmailEnviado.newEmailId()
      notas.forEach {nota ->
        nota.salvaEmail(bean, idEmail)
      }
    }
    else fail("Erro ao enviar e-mail")
  }
  
  private fun createAnexos(bean: EmailBean,
                           notas: List<NotaSaida>): List<FileAttach> {
    return when(bean.anexos) {
      "S"  -> {
        notas.flatMap {nota ->
          nota.listFiles()
            .map {nfile ->
              FileAttach(nfile.nome, nfile.file)
            }
        }
      }
      else -> emptyList()
    }
  }
  
  private fun createPlanilha(bean: EmailBean,
                             notas: List<NotaSaida>): List<FileAttach> {
    return when(bean.planilha) {
      "S"  -> {
        notas.map {_ ->
          val planilha = geraPlanilha(notas)
          FileAttach("Planilha de Notas.xlsx", planilha)
        }
      }
      else -> emptyList()
    }
  }
  
  private fun createReports(bean: EmailBean,
                            notas: List<NotaSaida>): List<FileAttach> {
    return when(bean.relatorio) {
      "S"  -> {
        notas.map {_ ->
          val report = RelatorioNotaDevolucao.processaRelatorio(notas)
          FileAttach("Relatorio de notas.pdf", report)
        }
      }
      else -> emptyList()
    }
  }
  
  fun mostrarEmailNota(nota: NotaSaida?) = viewModel.exec {
    nota ?: fail("Não há nenhuma nota selecionada")
    val emails = nota.listEmailNota()
    subView.selecionaEmail(nota, emails)
  }
}

interface INota {
  val serie: String
  val pago66: String
  
  fun updateGrid(itens: List<Fornecedor>)
  fun itensSelecionados(): List<Fornecedor>
  fun imprimeSelecionados(notas: List<NotaSaida>, resumida: Boolean)
  fun editRmk(nota: NotaSaida, save: (NotaSaida) -> Unit)
  fun editFile(nota: NotaSaida, insert: (NFFile) -> Unit)
  fun filtro(): String
  fun setFiltro(txt: String)
  fun enviaEmail(notas: List<NotaSaida>)
  fun selecionaEmail(nota: NotaSaida, emails: List<EmailEnviado>)
}

