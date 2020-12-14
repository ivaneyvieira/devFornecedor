package br.com.astrosoft.devolucao.viewmodel.devolucao

import br.com.astrosoft.devolucao.model.beans.Fornecedor
import br.com.astrosoft.devolucao.model.beans.NFFile
import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.model.planilhas.PlanilhaNotas
import br.com.astrosoft.framework.viewmodel.fail
import java.io.ByteArrayInputStream

abstract class NotaSerieViewModel(val viewModel: DevFornecedorViewModel) {
  protected abstract val subView: INota
  
  fun imprimirNotaDevolucao(notas: List<NotaSaida>) = viewModel.exec {
    notas.ifEmpty {
      fail("Não nenhuma nota selecionada")
    }
    subView.imprimeSelecionados(notas)
  }
  
  fun updateGridNota() = viewModel.exec {
    subView.updateGrid(listNotaDevolucao())
  }
  
  private fun listNotaDevolucao(): List<Fornecedor> {
    subView.setFiltro("")
    NotaSaida.updateNotasDevolucao(subView.serie)
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
  
  fun List<Fornecedor>.filtro(txt: String): List<Fornecedor> {
    return this.filter {
      val filtroNum = txt.toIntOrNull() ?: 0
      it.custno == filtroNum || it.vendno == filtroNum || it.fornecedor.startsWith(txt, ignoreCase = true)
    }
  }
  
  fun geraPlanilha(notas: List<NotaSaida>): ByteArrayInputStream {
    val planilha = PlanilhaNotas()
    val bytes = planilha.grava(notas)
    
    return ByteArrayInputStream(bytes)
  }
  
  fun listEmail(fornecedor: Fornecedor?): List<String> {
    return fornecedor?.listEmail().orEmpty()
  }
  
  fun enviarEmail(notas: List<NotaSaida>) = viewModel.exec {
    if(notas.isEmpty())
      fail("Nenhuma nota selecionada")
    subView.enviaEmail(notas)
  }
}

interface INota {
  val serie: String
  
  fun updateGrid(itens: List<Fornecedor>)
  fun itensSelecionados(): List<Fornecedor>
  fun imprimeSelecionados(itens: List<NotaSaida>)
  fun editRmk(nota: NotaSaida, save: (NotaSaida) -> Unit)
  fun editFile(nota: NotaSaida, insert: (NFFile) -> Unit)
  fun filtro(): String
  fun setFiltro(txt: String)
  fun enviaEmail(notas: List<NotaSaida>)
}