package br.com.astrosoft.devolucao.view.devFornecedor

import br.com.astrosoft.devolucao.model.beans.Fornecedor
import br.com.astrosoft.devolucao.model.beans.NFFile
import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.model.beans.ProdutosNotaSaida
import br.com.astrosoft.devolucao.model.beans.Representante
import br.com.astrosoft.devolucao.view.fornecedorCliente
import br.com.astrosoft.devolucao.view.fornecedorCodigo
import br.com.astrosoft.devolucao.view.fornecedorNome
import br.com.astrosoft.devolucao.view.nfFileDescricao
import br.com.astrosoft.devolucao.view.notaCelular
import br.com.astrosoft.devolucao.view.notaDataNota
import br.com.astrosoft.devolucao.view.notaDataPedido
import br.com.astrosoft.devolucao.view.notaEmail
import br.com.astrosoft.devolucao.view.notaFatura
import br.com.astrosoft.devolucao.view.notaLoja
import br.com.astrosoft.devolucao.view.notaNota
import br.com.astrosoft.devolucao.view.notaPedido
import br.com.astrosoft.devolucao.view.notaRepresentante
import br.com.astrosoft.devolucao.view.notaTelefone
import br.com.astrosoft.devolucao.view.notaValor
import br.com.astrosoft.devolucao.view.produtoCodigo
import br.com.astrosoft.devolucao.view.produtoDescricao
import br.com.astrosoft.devolucao.view.produtoGrade
import br.com.astrosoft.devolucao.view.produtoQtde
import br.com.astrosoft.devolucao.view.reports.RelatorioNotaDevolucao
import br.com.astrosoft.devolucao.viewmodel.devolucao.INota
import br.com.astrosoft.devolucao.viewmodel.devolucao.NotaSerieViewModel
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.SubWindowPDF
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnButton
import br.com.astrosoft.framework.view.showOutput
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.isExpand
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant.LUMO_COMPACT
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.VaadinIcon.CHECK
import com.vaadin.flow.component.icon.VaadinIcon.EDIT
import com.vaadin.flow.component.icon.VaadinIcon.EYE
import com.vaadin.flow.component.icon.VaadinIcon.FILE_PICTURE
import com.vaadin.flow.component.icon.VaadinIcon.FILE_TABLE
import com.vaadin.flow.component.icon.VaadinIcon.PHONE_LANDLINE
import com.vaadin.flow.component.icon.VaadinIcon.PRINT
import com.vaadin.flow.component.icon.VaadinIcon.TRASH
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.upload.FileRejectedEvent
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.data.value.ValueChangeMode.TIMEOUT
import java.time.LocalDate

abstract class TabFornecedorAbstract(val viewModel: NotaSerieViewModel):
  TabPanelGrid<Fornecedor>(Fornecedor::class), INota {
  private lateinit var edtFiltro: TextField
  
  override fun HorizontalLayout.toolBarConfig() {
    edtFiltro = textField("Filtro") {
      width = "400px"
      valueChangeMode = TIMEOUT
      addValueChangeListener {
        viewModel.updateFiltro()
      }
    }
  }
  
  override fun Grid<Fornecedor>.gridPanel() {
    addColumnButton(FILE_TABLE, "Notas", "Notas") {fornecedor ->
      showDialogNota(fornecedor)
    }
    addColumnButton(PHONE_LANDLINE, "Representantes", "Rep") {fornecedor ->
      showDialogRepresentante(fornecedor)
    }
    fornecedorCodigo()
    fornecedorCliente()
    fornecedorNome()
  }
  
  override fun filtro() = edtFiltro.value ?: ""
  override fun setFiltro(txt: String) {
    edtFiltro.value = txt
  }
  
  override fun itensSelecionados(): List<Fornecedor> {
    return itensSelecionado()
  }
  
  override fun imprimeSelecionados(itens: List<NotaSaida>) {
    val report = RelatorioNotaDevolucao.processaRelatorio(itens)
    val chave = "DevReport"
    SubWindowPDF(chave, report).open()
  }
  
  override fun editRmk(nota: NotaSaida, save: (NotaSaida) -> Unit) {
    val form = SubWindowForm("PROCESSO INTERNO: ${nota.nota}|DEV FORNECEDOR: ${nota.fornecedor}",
                             toolBar = {window ->
                               button("Salva") {
                                 icon = CHECK.create()
                                 onLeftClick {
                                   save(nota)
                                   window.close()
                                 }
                               }
                             }) {
      createFormEditRmk(nota)
    }
    form.open()
  }
  
  override fun editFile(nota: NotaSaida, insert: (NFFile) -> Unit) {
    val grid = createFormEditFile(nota)
    val form = SubWindowForm("PROCESSO INTERNO: ${nota.nota}|DEV FORNECEDOR: ${nota.fornecedor}",
                             toolBar = {window ->
                               val (buffer, upload) = uploadFile()
                               upload.addSucceededListener {
                                 val fileName = it.fileName
                                 val bytes =
                                   buffer.getInputStream(fileName)
                                     .readBytes()
                                 val nfFile =
                                   NFFile(storeno = nota.loja,
                                          pdvno = nota.pdv,
                                          xano = nota.transacao,
                                          date = LocalDate.now(),
                                          nome = fileName,
                                          file = bytes)
                                 insert(nfFile)
                                 grid.setItems(nota.listFiles())
                               }
                             }) {
      grid
    }
    form.open()
  }
  
  private fun HasComponents.uploadFile(): Pair<MultiFileMemoryBuffer, Upload> {
    val buffer = MultiFileMemoryBuffer()
    val upload = Upload(buffer)
    upload.setAcceptedFileTypes("image/jpeg", "image/png", "application/pdf", "text/plain")
    val uploadButton = Button("Arquivo Nota")
    upload.uploadButton = uploadButton
    upload.isAutoUpload = true
    upload.maxFileSize = 1024 * 1024 * 1024
    upload.addFileRejectedListener {event: FileRejectedEvent ->
      println(event.errorMessage)
    }
    upload.addFailedListener {event ->
      println(event.reason.message)
    }
    add(upload)
    return Pair(buffer, upload)
  }
  
  override fun updateComponent() {
    viewModel.updateGridNota()
  }
  
  private fun createFormEditRmk(nota: NotaSaida): Component {
    return TextArea().apply {
      this.style.set("overflow-y", "auto");
      this.isExpand = true
      this.focus()
      this.value = nota.rmk
      valueChangeMode = ValueChangeMode.TIMEOUT
      addValueChangeListener {
        val text = it.value
        nota.rmk = text
      }
    }
  }
  
  private fun createFormEditFile(nota: NotaSaida): Grid<NFFile> {
    val gridDetail = Grid(NFFile::class.java, false)
    return gridDetail.apply {
      addThemeVariants(LUMO_COMPACT)
      isMultiSort = false
      setItems(nota.listFiles())
      //
      addColumnButton(EYE, "Visualizar", "Ver") {file ->
        val form = SubWindowForm(file.nome,
                                 toolBar = { }) {
          val div = Div()
          div.showOutput(file.nome, file.file)
          div
        }
        form.open()
      }
      addColumnButton(TRASH, "Remover arquivo", "Rem") {file ->
        viewModel.deleteFile(file)
        setItems(nota.listFiles())
      }
      nfFileDescricao()
    }
  }
  
  private fun showDialogRepresentante(fornecedor: Fornecedor?) {
    fornecedor ?: return
    val listRepresentantes = fornecedor.listRepresentantes()
    val form = SubWindowForm("DEV FORNECEDOR: ${fornecedor.custno} ${fornecedor.fornecedor} (${fornecedor.vendno})") {
      createGridRepresentantes(listRepresentantes)
    }
    form.open()
  }
  
  private fun showDialogNota(fornecedor: Fornecedor?) {
    fornecedor ?: return
    val listNotas = fornecedor.notas
    val form = SubWindowForm("DEV FORNECEDOR: ${fornecedor.custno} ${fornecedor.fornecedor} (${fornecedor.vendno})") {
      createGridNotas(listNotas)
    }
    form.open()
  }
  
  private fun showDialogImpressao(nota: NotaSaida?) {
    nota ?: return
    val listProdutos = nota.listaProdutos()
    val form = SubWindowForm("PROCESSO INTERNO: ${nota.nota}|DEV FORNECEDOR: ${nota.fornecedor}") {
      createGridImpressao(listProdutos)
    }
    form.open()
  }
  
  private fun createGridRepresentantes(listRepresentantes: List<Representante>): Grid<Representante> {
    val gridDetail = Grid(Representante::class.java, false)
    return gridDetail.apply {
      addThemeVariants(LUMO_COMPACT)
      isMultiSort = false
      setItems(listRepresentantes)
      //
      notaRepresentante()
      notaTelefone()
      notaCelular()
      notaEmail()
    }
  }
  
  private fun createGridNotas(listNotas: List<NotaSaida>): Grid<NotaSaida> {
    val gridDetail = Grid(NotaSaida::class.java, false)
    return gridDetail.apply {
      addThemeVariants(LUMO_COMPACT)
      isMultiSort = false
      setItems(listNotas)
      //
      addColumnButton(PRINT, "Impressão", "Imp") {nota ->
        //showDialogImpressao(nota)
        viewModel.imprimirNotaDevolucao(nota)
      }
      addColumnButton(FILE_PICTURE, "Arquivos", "Arq") {nota ->
        viewModel.editFile(nota)
      }
      addColumnButton(EDIT, "Editor", "Edt") {nota ->
        viewModel.editRmk(nota)
      }
      
      notaLoja()
      notaDataPedido()
      notaPedido()
      notaDataNota()
      notaNota()
      notaFatura()
      notaValor().apply {
        val totalPedido =
          listNotas.sumByDouble {it.valor}
            .format()
        setFooter(Html("<b><font size=4>Total R$ &nbsp;&nbsp;&nbsp;&nbsp; ${totalPedido}</font></b>"))
      }
    }
  }
  
  private fun createGridImpressao(listProdutos: List<ProdutosNotaSaida>): Grid<ProdutosNotaSaida> {
    val gridDetail = Grid(ProdutosNotaSaida::class.java, false)
    return gridDetail.apply {
      addThemeVariants(LUMO_COMPACT)
      isMultiSort = false
      setItems(listProdutos)
      //
      produtoCodigo()
      produtoDescricao()
      produtoGrade()
      produtoQtde()
    }
  }
}