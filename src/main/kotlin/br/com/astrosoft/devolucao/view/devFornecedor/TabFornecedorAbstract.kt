package br.com.astrosoft.devolucao.view.devFornecedor

import br.com.astrosoft.devolucao.model.beans.EmailDB
import br.com.astrosoft.devolucao.model.beans.EmailGmail
import br.com.astrosoft.devolucao.model.beans.Fornecedor
import br.com.astrosoft.devolucao.model.beans.NFFile
import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.model.beans.ProdutosNotaSaida
import br.com.astrosoft.devolucao.model.beans.Representante
import br.com.astrosoft.devolucao.view.emailAssunto
import br.com.astrosoft.devolucao.view.emailData
import br.com.astrosoft.devolucao.view.emailEmail
import br.com.astrosoft.devolucao.view.emailHora
import br.com.astrosoft.devolucao.view.emailTipo
import br.com.astrosoft.devolucao.view.fornecedorCliente
import br.com.astrosoft.devolucao.view.fornecedorCodigo
import br.com.astrosoft.devolucao.view.fornecedorNome
import br.com.astrosoft.devolucao.view.fornecedorUltimaData
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
import br.com.astrosoft.devolucao.viewmodel.devolucao.AbstractNotaSerieViewModel
import br.com.astrosoft.devolucao.viewmodel.devolucao.INota
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.util.htmlToText
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.SubWindowPDF
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnButton
import br.com.astrosoft.framework.view.showOutput
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome.Solid.FILE_EXCEL
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.checkBox
import com.github.mvysny.karibudsl.v10.comboBox
import com.github.mvysny.karibudsl.v10.getColumnBy
import com.github.mvysny.karibudsl.v10.horizontalLayout
import com.github.mvysny.karibudsl.v10.isExpand
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode.MULTI
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.grid.GridVariant.LUMO_COMPACT
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon.CHECK
import com.vaadin.flow.component.icon.VaadinIcon.EDIT
import com.vaadin.flow.component.icon.VaadinIcon.ENVELOPE_O
import com.vaadin.flow.component.icon.VaadinIcon.EYE
import com.vaadin.flow.component.icon.VaadinIcon.FILE_PICTURE
import com.vaadin.flow.component.icon.VaadinIcon.FILE_TABLE
import com.vaadin.flow.component.icon.VaadinIcon.PHONE_LANDLINE
import com.vaadin.flow.component.icon.VaadinIcon.PRINT
import com.vaadin.flow.component.icon.VaadinIcon.TRASH
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.upload.FileRejectedEvent
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer
import com.vaadin.flow.data.provider.SortDirection
import com.vaadin.flow.data.value.ValueChangeMode.TIMEOUT
import org.vaadin.stefan.LazyDownloadButton
import java.io.ByteArrayInputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

abstract class TabFornecedorAbstract(val viewModel: AbstractNotaSerieViewModel):
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
    fornecedorUltimaData()
  }
  
  override fun filtro() = edtFiltro.value ?: ""
  
  override fun setFiltro(txt: String) {
    edtFiltro.value = txt
  }
  
  override fun itensSelecionados(): List<Fornecedor> {
    return itensSelecionado()
  }
  
  override fun imprimeSelecionados(notas: List<NotaSaida>, resumida: Boolean) {
    val report = RelatorioNotaDevolucao.processaRelatorio(notas, resumida)
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
                             toolBar = {_ ->
                               val (buffer, upload) = uploadFile()
                               upload.addSucceededListener {
                                 val fileName = it.fileName
                                 val bytes =
                                   buffer.getInputStream(fileName)
                                     .readBytes()
                                 val nfFile =
                                   NFFile.new(nota, fileName, bytes)
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
      this.style.set("overflow-y", "auto")
      this.isExpand = true
      this.focus()
      this.value = nota.rmk
      valueChangeMode = TIMEOUT
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
    lateinit var gridNota: Grid<NotaSaida>
    val listNotas = fornecedor.notas
    val form =
      SubWindowForm("DEV FORNECEDOR: ${fornecedor.custno} ${fornecedor.fornecedor} (${fornecedor.vendno})", toolBar = {
        val captionImpressoa = if(serie == "66" || serie == "PED") "Impressão Completa" else "Impressão"
        button(captionImpressoa) {
          icon = PRINT.create()
          onLeftClick {
            val notas = gridNota.asMultiSelect().selectedItems.toList()
            viewModel.imprimirNotaDevolucao(notas)
          }
        }
        if(serie == "66" || serie == "PED") {
          button("Impressão Resumida") {
            icon = PRINT.create()
            onLeftClick {
              val notas = gridNota.asMultiSelect().selectedItems.toList()
              viewModel.imprimirNotaDevolucao(notas, resumida = true)
            }
          }
        }
        this.add(buttonPlanilha {
          gridNota.asMultiSelect().selectedItems.toList()
        })
        button("Email") {
          icon = ENVELOPE_O.create()
          onLeftClick {
            val notas = gridNota.asMultiSelect().selectedItems.toList()
            viewModel.enviarEmail(notas)
          }
        }
      }) {
        gridNota = createGridNotas(listNotas)
        gridNota
      }
    form.open()
  }
  
  override fun enviaEmail(notas: List<NotaSaida>) {
    val nota = notas.firstOrNull() ?: return
    val form = SubWindowForm("DEV FORNECEDOR: ${nota.fornecedor}") {
      FormEmail(viewModel, notas)
    }
    form.open()
  }
  
  private fun filename(): String {
    val sdf = DateTimeFormatter.ofPattern("yyMMddHHmmss")
    val textTime =
      LocalDateTime.now()
        .format(sdf)
    return "notas$textTime.xlsx"
  }
  
  private fun buttonPlanilha(notas: () -> List<NotaSaida>): LazyDownloadButton {
    return LazyDownloadButton("Planilha", FILE_EXCEL.create(), ::filename) {
      ByteArrayInputStream(viewModel.geraPlanilha(notas()))
    }
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
      setSelectionMode(MULTI)
      setItems(listNotas)
      //
      addColumnButton(FILE_PICTURE, "Arquivos", "Arq", ::configIconArq) {nota ->
        viewModel.editFile(nota)
      }
      addColumnButton(EDIT, "Editor", "Edt", ::configIconEdt) {nota ->
        viewModel.editRmk(nota)
      }
      addColumnButton(ENVELOPE_O, "Editor", "Email", ::configMostraEmail) {nota ->
        viewModel.mostrarEmailNota(nota)
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
      if(serie == "PED")
        sort(listOf(
          GridSortOrder(getColumnBy(NotaSaida::dataPedido), SortDirection.ASCENDING))
            )
      else
        sort(listOf(
          GridSortOrder(getColumnBy(NotaSaida::dataNota), SortDirection.ASCENDING))
            )
    }
  }
  
  override fun selecionaEmail(nota: NotaSaida, emails: List<EmailDB>) {
    val form = SubWindowForm("PROCESSO INTERNO: ${nota.nota}|DEV FORNECEDOR: ${nota.fornecedor}") {
      createGridEmail(nota, emails)
    }
    form.open()
  }
  
  private fun createGridEmail(nota: NotaSaida, emails: List<EmailDB>): Grid<EmailDB> {
    val gridDetail = Grid(EmailDB::class.java, false)
    val lista = emails + nota.listaEmailRecebidoNota()
    return gridDetail.apply {
      addThemeVariants(LUMO_COMPACT)
      isMultiSort = false
      // setSelectionMode(MULTI)
      setItems(lista.sortedWith(compareByDescending<EmailDB> {it.data}.thenByDescending {it.hora}))
      
      addColumnButton(EDIT, "Edita e-mail", "Edt") {emailEnviado ->
        editEmail(nota, emailEnviado)
      }
      
      emailData()
      emailHora()
      emailAssunto()
      emailTipo()
      emailEmail()
    }
  }
  
  private fun editEmail(nota: NotaSaida, emailEnviado: EmailDB?) {
    val form = SubWindowForm("DEV FORNECEDOR: ${nota.fornecedor}") {
      FormEmail(viewModel, listOf(nota), emailEnviado)
    }
    form.open()
  }
  
  fun configIconArq(icon: Icon, nota: NotaSaida) {
    if(nota.listFiles()
        .isNotEmpty()
    )
      icon.color = "DarkGreen"
    else icon.color = ""
  }
  
  fun configMostraEmail(icon: Icon, nota: NotaSaida) {
    if(nota.listEmailNota()
        .isNotEmpty()
    )
      icon.color = "DarkGreen"
    else icon.color = ""
  }
  
  fun configIconEdt(icon: Icon, nota: NotaSaida) {
    if(nota.rmk.isNotBlank())
      icon.color = "DarkGreen"
    else icon.color = ""
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

class FormEmail(val viewModel: AbstractNotaSerieViewModel, notas: List<NotaSaida>, emailEnviado: EmailDB? = null):
  VerticalLayout() {
  private lateinit var chkPlanilha: Checkbox
  private lateinit var edtAssunto: TextField
  private var rteMessage: TextArea
  private lateinit var chkAnexos: Checkbox
  private lateinit var chkRelatorio: Checkbox
  private lateinit var cmbEmail: ComboBox<String>
  var gmail: EmailGmail
    get() = EmailGmail(
      email = cmbEmail.value ?: "",
      assunto = edtAssunto.value ?: "",
      msg = rteMessage.value ?: "",
      msgHtml = rteMessage.value ?: "",
      planilha = if(chkPlanilha.value) "S" else "N",
      relatorio = if(chkRelatorio.value) "S" else "N",
      anexos = if(chkAnexos.value) "S" else "N"
                      )
    set(value) {
      cmbEmail.value = value.email
      edtAssunto.value = value.assunto
      rteMessage.value = htmlToText(value.msg)
      //rteMessage.sanitizeHtml(value.msg.htmlFormat(), SanitizeType.none)
      chkPlanilha.value = value.planilha == "S"
      chkRelatorio.value = value.relatorio == "S"
      chkAnexos.value = value.anexos == "S"
    }
  
  init {
    val fornecedor =
      NotaSaida.findFornecedores()
        .firstOrNull {it.notas.containsAll(notas)}
    rteMessage = richEditor(emailEnviado?.msg ?: "")
    setSizeFull()
    horizontalLayout {
      setWidthFull()
      cmbEmail = comboBox("E-Mail") {
        this.width = "400px"
        this.isAllowCustomValue = true
        setItems(viewModel.listEmail(fornecedor))
        addCustomValueSetListener {event ->
          this.value = event.detail
        }
      }
      edtAssunto = textField("Assunto") {
        this.isExpand = true
      }
      chkRelatorio = checkBox("Relatório")
      chkPlanilha = checkBox("Planilha")
      chkAnexos = checkBox("Anexos")
      
      button("Enviar") {
        // val numerosNota = notas.joinToString(separator = " ") {it.nota}
        onLeftClick {
          viewModel.enviaEmail(gmail, notas)
        }
      }
    }
    rteMessage.width = "100%"
    
    addAndExpand(rteMessage)
    emailEnviado?.let {email ->
      gmail = email.emailBean()
    }
  }
  
  private fun richEditor(htmlText: String): TextArea {
    val rte = TextArea()
    
    rte.value = htmlToText(htmlText)
    
    return rte
  }
}

