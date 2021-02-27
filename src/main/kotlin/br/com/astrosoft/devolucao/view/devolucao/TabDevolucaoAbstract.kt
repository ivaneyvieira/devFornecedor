package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.*
import br.com.astrosoft.devolucao.view.devolucao.columns.EmailDBViewColumns.emailAssunto
import br.com.astrosoft.devolucao.view.devolucao.columns.EmailDBViewColumns.emailData
import br.com.astrosoft.devolucao.view.devolucao.columns.EmailDBViewColumns.emailEmail
import br.com.astrosoft.devolucao.view.devolucao.columns.EmailDBViewColumns.emailHora
import br.com.astrosoft.devolucao.view.devolucao.columns.EmailDBViewColumns.emailTipo
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorCliente
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorCodigo
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorNome
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorViewColumns.fornecedorUltimaData
import br.com.astrosoft.devolucao.view.devolucao.columns.NFFileViewColumns.nfFileData
import br.com.astrosoft.devolucao.view.devolucao.columns.NFFileViewColumns.nfFileDescricao
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaDataNota
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaDataPedido
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaFatura
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaLoja
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaNota
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaPedido
import br.com.astrosoft.devolucao.view.devolucao.columns.NotaSaidaViewColumns.notaValor
import br.com.astrosoft.devolucao.view.devolucao.columns.RepresentanteViewColumns.notaCelular
import br.com.astrosoft.devolucao.view.devolucao.columns.RepresentanteViewColumns.notaEmail
import br.com.astrosoft.devolucao.view.devolucao.columns.RepresentanteViewColumns.notaRepresentante
import br.com.astrosoft.devolucao.view.devolucao.columns.RepresentanteViewColumns.notaTelefone
import br.com.astrosoft.devolucao.view.reports.RelatorioNotaDevolucao
import br.com.astrosoft.devolucao.viewmodel.devolucao.IEmailView
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabNota
import br.com.astrosoft.devolucao.viewmodel.devolucao.Serie
import br.com.astrosoft.devolucao.viewmodel.devolucao.Serie.*
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabDevolucaoViewModelAbstract
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.util.htmlToText
import br.com.astrosoft.framework.view.*
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome.Solid.FILE_EXCEL
import com.github.mvysny.karibudsl.v10.*
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
import com.vaadin.flow.component.icon.VaadinIcon.*
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

abstract class TabDevolucaoAbstract(val viewModel: TabDevolucaoViewModelAbstract) : TabPanelGrid<Fornecedor>(
  Fornecedor::class
                                                                                                            ),
                                                                                    ITabNota {
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
    addColumnButton(FILE_TABLE, "Notas", "Notas") { fornecedor ->
      DlgNota(viewModel).showDialogNota(fornecedor, serie)
    }
    addColumnButton(EDIT, "Editor", "Edt", ::configIconEdt) { fornecedor ->
      viewModel.editRmkVend(fornecedor)
    }
    addColumnButton(PHONE_LANDLINE, "Representantes", "Rep") { fornecedor ->
      DlgFornecedor().showDialogRepresentante(fornecedor)
    }
    fornecedorUltimaData()
    if (serie != ENT) fornecedorCodigo()
    fornecedorCliente()
    fornecedorNome()
  }

  override fun editRmkVend(fornecedor: Fornecedor, save: (Fornecedor) -> Unit) {
    DlgEditRmkVend().editRmk(fornecedor, save)
  }

  private fun configIconEdt(icon: Icon, fornecedor: Fornecedor) {
    if (fornecedor.obs.isNotBlank()) icon.color = "DarkGreen"
    else icon.color = ""
  }

  override fun filtro() = edtFiltro.value ?: ""

  override fun setFiltro(txt: String) {
    edtFiltro.value = txt
  }

  override fun imprimeSelecionados(notas: List<NotaSaida>, resumida: Boolean) {
    val report = RelatorioNotaDevolucao.processaRelatorio(notas, resumida)
    val chave = "DevReport"
    SubWindowPDF(chave, report).open()
  }

  override fun editRmk(nota: NotaSaida, save: (NotaSaida) -> Unit) {
    DlgEditRmk().editRmk(nota, save)
  }

  override fun editFile(nota: NotaSaida, insert: (NFFile) -> Unit) {
    DlgEditFile(viewModel).editFile(nota, insert)
  }

  override fun updateComponent() {
    viewModel.updateView()
  }

  override fun enviaEmail(notas: List<NotaSaida>) {
    DlgEnviaEmail(viewModel).enviaEmail(notas)
  }

  override fun selecionaEmail(nota: NotaSaida, emails: List<EmailDB>) {
    DlgSelecionaEmail(viewModel).selecionaEmail(nota, emails)
  }
}

class DlgEditRmkVend {
  fun editRmk(fornecedor: Fornecedor, save: (Fornecedor) -> Unit) {
    val form = SubWindowForm("DEV FORNECEDOR: ${fornecedor.fornecedor}", toolBar = { window ->
      button("Salva") {
        icon = CHECK.create()
        onLeftClick {
          save(fornecedor)
          window.close()
        }
      }
    }) {
      createFormEditRmk(fornecedor)
    }
    form.open()
  }

  private fun createFormEditRmk(fornecedor: Fornecedor): Component {
    return TextArea().apply {
      this.style.set("overflow-y", "auto")
      this.isExpand = true
      this.focus()
      this.value = fornecedor.obs
      valueChangeMode = TIMEOUT
      addValueChangeListener {
        val text = it.value
        fornecedor.obs = text
      }
    }
  }
}

class DlgEditRmk {
  fun editRmk(nota: NotaSaida, save: (NotaSaida) -> Unit) {
    val form = SubWindowForm("PROCESSO INTERNO: ${nota.nota}|DEV FORNECEDOR: ${nota.fornecedor}",
      toolBar = { window ->
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
}

class DlgSelecionaEmail(val viewModel: TabDevolucaoViewModelAbstract) {
  fun selecionaEmail(nota: NotaSaida, emails: List<EmailDB>) {
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
      isMultiSort = false // setSelectionMode(MULTI)
      setItems(lista.sortedWith(compareByDescending<EmailDB> { it.data }.thenByDescending { it.hora }))

      addColumnButton(EDIT, "Edita e-mail", "Edt") { emailEnviado ->
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
}

class DlgEnviaEmail(val viewModel: TabDevolucaoViewModelAbstract) {
  fun enviaEmail(notas: List<NotaSaida>) {
    val nota = notas.firstOrNull() ?: return
    val form = SubWindowForm("DEV FORNECEDOR: ${nota.fornecedor}") {
      FormEmail(viewModel, notas)
    }
    form.open()
  }
}

class DlgEditFile(val viewModel: TabDevolucaoViewModelAbstract) {
  fun editFile(nota: NotaSaida, insert: (NFFile) -> Unit) {
    val grid = createFormEditFile(nota)
    val form = SubWindowForm("PROCESSO INTERNO: ${nota.nota}|DEV FORNECEDOR: ${nota.fornecedor}",
      toolBar = { _ ->
        val (buffer, upload) = uploadFile()
        upload.addSucceededListener {
          val fileName = it.fileName
          val bytes = buffer.getInputStream(fileName).readBytes()
          val nfFile = NFFile.new(nota, fileName, bytes)
          insert(nfFile)
          grid.setItems(nota.listFiles())
        }
      }) {
      grid
    }
    form.open()
  }

  private fun createFormEditFile(nota: NotaSaida): Grid<NFFile> {
    val gridDetail = Grid(NFFile::class.java, false)
    return gridDetail.apply {
      addThemeVariants(LUMO_COMPACT)
      isMultiSort = false
      setItems(nota.listFiles()) //
      addColumnButton(EYE, "Visualizar", "Ver") { file ->
        val form = SubWindowForm(file.nome, toolBar = { }) {
          val div = Div()
          div.showOutput(file.nome, file.file)
          div
        }
        form.open()
      }
      addColumnButton(TRASH, "Remover arquivo", "Rem") { file ->
        viewModel.deleteFile(file)
        setItems(nota.listFiles())
      }
      nfFileDescricao()
      nfFileData()
    }
  }

  private fun HasComponents.uploadFile(): Pair<MultiFileMemoryBuffer, Upload> {
    val buffer = MultiFileMemoryBuffer()
    val upload = Upload(buffer)
    upload.setAcceptedFileTypes("image/jpeg", "image/png", "application/pdf", "text/plain")
    val uploadButton = Button("Arquivo Nota")
    upload.uploadButton = uploadButton
    upload.isAutoUpload = true
    upload.maxFileSize = 1024 * 1024 * 1024
    upload.addFileRejectedListener { event: FileRejectedEvent ->
      println(event.errorMessage)
    }
    upload.addFailedListener { event ->
      println(event.reason.message)
    }
    add(upload)
    return Pair(buffer, upload)
  }
}

class DlgFornecedor {
  fun showDialogRepresentante(fornecedor: Fornecedor?) {
    fornecedor ?: return
    val listRepresentantes = fornecedor.listRepresentantes()
    val form =
      SubWindowForm("DEV FORNECEDOR: ${fornecedor.custno} ${fornecedor.fornecedor} (${fornecedor.vendno})") {
        createGridRepresentantes(listRepresentantes)
      }
    form.open()
  }

  private fun createGridRepresentantes(listRepresentantes: List<Representante>): Grid<Representante> {
    val gridDetail = Grid(Representante::class.java, false)
    return gridDetail.apply {
      addThemeVariants(LUMO_COMPACT)
      isMultiSort = false
      setItems(listRepresentantes) //
      notaRepresentante()
      notaTelefone()
      notaCelular()
      notaEmail()
    }
  }
}

class FormEmail(val viewModel: IEmailView, notas: List<NotaSaida>, emailEnviado: EmailDB? = null) :
  VerticalLayout() {
  private lateinit var chkPlanilha: Checkbox
  private lateinit var edtAssunto: TextField
  private var rteMessage: TextArea
  private lateinit var chkAnexos: Checkbox
  private lateinit var chkRelatorio: Checkbox
  private lateinit var chkRelatorioResumido: Checkbox
  private lateinit var cmbEmail: ComboBox<String>
  private var gmail: EmailGmail
    get() = EmailGmail(
      email = cmbEmail.value ?: "",
      assunto = edtAssunto.value ?: "",
      msg = { rteMessage.value ?: "" },
      msgHtml = rteMessage.value ?: "",
      planilha = if (chkPlanilha.value) "S" else "N",
      relatorio = if (chkRelatorio.value) "S" else "N",
      relatorioResumido = if (chkRelatorioResumido.value) "S" else "N",
      anexos = if (chkAnexos.value) "S" else "N",
      messageID = ""
                      )
    set(value) {
      cmbEmail.value = value.email
      edtAssunto.value = value.assunto
      rteMessage.value =
        htmlToText(value.msg()) //rteMessage.sanitizeHtml(value.msg.htmlFormat(), SanitizeType.none)
      chkPlanilha.value = value.planilha == "S"
      chkRelatorio.value = value.relatorio == "S"
      chkRelatorioResumido.value = value.relatorioResumido == "S"
      chkAnexos.value = value.anexos == "S"
    }

  init {
    val fornecedor = NotaSaida.findFornecedores().firstOrNull { it.notas.containsAll(notas) }
    rteMessage = richEditor()
    setSizeFull()
    horizontalLayout {
      setWidthFull()
      cmbEmail = comboBox("E-Mail") {
        this.width = "400px"
        this.isAllowCustomValue = true
        setItems(viewModel.listEmail(fornecedor))
        addCustomValueSetListener { event ->
          this.value = event.detail
        }
      }
      edtAssunto = textField("Assunto") {
        this.isExpand = true
      }
      chkRelatorio = checkBox("Relatório")
      chkRelatorioResumido = checkBox("Resumido")
      chkPlanilha = checkBox("Planilha")
      chkAnexos = checkBox("Anexos")

      button("Enviar") { // val numerosNota = notas.joinToString(separator = " ") {it.nota}
        onLeftClick {
          viewModel.enviaEmail(gmail, notas)
        }
      }
    }
    rteMessage.width = "100%"

    addAndExpand(rteMessage)
    emailEnviado?.let { email ->
      gmail = email.emailBean()
    }
  }

  private fun richEditor(): TextArea {
    return TextArea()
  }
}

class DlgNota(val viewModel: TabDevolucaoViewModelAbstract) {
  fun showDialogNota(fornecedor: Fornecedor?, serie: Serie) {
    fornecedor ?: return
    lateinit var gridNota: Grid<NotaSaida>
    val listNotas = fornecedor.notas
    val form =
      SubWindowForm("DEV FORNECEDOR: ${fornecedor.custno} ${fornecedor.fornecedor} (${fornecedor.vendno})",
        toolBar = {
          val captionImpressoa = if (serie == Serie66 || serie == PED) "Impressão Completa"
          else "Impressão"
          button(captionImpressoa) {
            icon = PRINT.create()
            onLeftClick {
              val notas = gridNota.asMultiSelect().selectedItems.toList()
              viewModel.imprimirNotaDevolucao(notas)
            }
          }
          if (serie == Serie66 || serie == PED) {
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
        gridNota = createGridNotas(listNotas, serie)
        gridNota
      }
    form.open()
  }

  private fun buttonPlanilha(notas: () -> List<NotaSaida>): LazyDownloadButton {
    return LazyDownloadButton("Planilha", FILE_EXCEL.create(), ::filename) {
      ByteArrayInputStream(viewModel.geraPlanilha(notas()))
    }
  }

  private fun filename(): String {
    val sdf = DateTimeFormatter.ofPattern("yyMMddHHmmss")
    val textTime = LocalDateTime.now().format(sdf)
    return "notas$textTime.xlsx"
  }

  private fun createGridNotas(listNotas: List<NotaSaida>, serie: Serie): Grid<NotaSaida> {
    val gridDetail = Grid(NotaSaida::class.java, false)
    return gridDetail.apply {
      addThemeVariants(LUMO_COMPACT)
      isMultiSort = false
      setSelectionMode(MULTI)
      setItems(listNotas) //
      addColumnButton(FILE_PICTURE, "Arquivos", "Arq", ::configIconArq) { nota ->
        viewModel.editFile(nota)
      }
      addColumnButton(EDIT, "Editor", "Edt", ::configIconEdt) { nota ->
        viewModel.editRmk(nota)
      }
      addColumnButton(ENVELOPE_O, "Editor", "Email", ::configMostraEmail) { nota ->
        viewModel.mostrarEmailNota(nota)
      }

      notaLoja()
      notaDataPedido()
      notaPedido()
      notaDataNota()
      notaNota()
      notaFatura()
      notaValor().apply {
        val totalPedido = listNotas.sumByDouble { it.valorNota }.format()
        setFooter(Html("<b><font size=4>Total R$ &nbsp;&nbsp;&nbsp;&nbsp; ${totalPedido}</font></b>"))
      }
      if (serie == PED) sort(
        listOf(
          GridSortOrder(
            getColumnBy(NotaSaida::dataPedido), SortDirection.ASCENDING
                       )
              )
                            )
      else sort(
        listOf(
          GridSortOrder(
            getColumnBy(NotaSaida::dataNota), SortDirection.ASCENDING
                       )
              )
               )
    }
  }

  private fun configIconEdt(icon: Icon, nota: NotaSaida) {
    if (nota.rmk.isNotBlank()) icon.color = "DarkGreen"
    else icon.color = ""
  }

  private fun configMostraEmail(icon: Icon, nota: NotaSaida) {
    if (nota.listEmailNota().isNotEmpty()) icon.color = "DarkGreen"
    else icon.color = ""
  }

  private fun configIconArq(icon: Icon, nota: NotaSaida) {
    if (nota.listFiles().isNotEmpty()) icon.color = "DarkGreen"
    else icon.color = ""
  }
}