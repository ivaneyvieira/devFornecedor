package br.com.astrosoft.devolucao.view.devolucao

import br.com.astrosoft.devolucao.model.beans.FornecedorSap
import br.com.astrosoft.devolucao.model.beans.NotaDevolucaoSap
import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.devolucao.model.reports.RelatorioFornecedorSap
import br.com.astrosoft.devolucao.model.reports.RelatorioNotaFornecedor
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorSapViewColumns.fornecedorCodigoSaci
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorSapViewColumns.fornecedorCodigoSap
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorSapViewColumns.fornecedorNome
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorSapViewColumns.fornecedorUltimaData
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorSapViewColumns.fornecedorValorDiferenca
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorSapViewColumns.fornecedorValorSaci
import br.com.astrosoft.devolucao.view.devolucao.columns.FornecedorSapViewColumns.fornecedorValorSap
import br.com.astrosoft.devolucao.viewmodel.devolucao.ITabConferenciaSap
import br.com.astrosoft.devolucao.viewmodel.devolucao.TabConferenciaSapViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.SubWindowPDF
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnButton
import com.github.mvysny.karibudsl.v10.textField
import com.github.mvysny.kaributools.getColumnBy
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.icon.VaadinIcon.FILE_TABLE
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.upload.FileRejectedEvent
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer
import com.vaadin.flow.data.provider.SortDirection
import com.vaadin.flow.data.value.ValueChangeMode
import java.io.File

class TabConferenciaSap(val viewModel: TabConferenciaSapViewModel) : TabPanelGrid<FornecedorSap>(FornecedorSap::class),
  ITabConferenciaSap {
  private lateinit var edtFiltro: TextField

  override fun HorizontalLayout.toolBarConfig() {
    edtFiltro = textField("Filtro") {
      width = "400px"
      valueChangeMode = ValueChangeMode.LAZY
      valueChangeTimeout = 2000
      addValueChangeListener {
        viewModel.updateView()
      }
    }
    val (buffer, upload) = uploadFileXls()
    upload.addSucceededListener {
      val fileName = "/tmp/${it.fileName}"
      val bytes = buffer.getInputStream(it.fileName).readBytes()
      val file = File(fileName)
      file.writeBytes(bytes)
      viewModel.readExcel(fileName)
    }
  }

  private fun HasComponents.uploadFileXls(): Pair<MultiFileMemoryBuffer, Upload> {
    val buffer = MultiFileMemoryBuffer()
    val upload =
      Upload(buffer) //upload.setAcceptedFileTypes("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    val uploadButton = Button("Planilha SAP")
    upload.uploadButton = uploadButton
    upload.isAutoUpload = true
    upload.isDropAllowed = false
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

  override fun Grid<FornecedorSap>.gridPanel() {
    addColumnButton(FILE_TABLE, "Notas", "Notas") { fornecedor ->
      DlgNotaSapSaci(viewModel).showDialogNota(fornecedor)
    }

    fornecedorCodigoSaci()
    fornecedorCodigoSap()
    fornecedorNome()
    fornecedorUltimaData()
    fornecedorValorSap()
    fornecedorValorSaci()
    fornecedorValorDiferenca()

    sort(listOf(GridSortOrder(getColumnBy(FornecedorSap::nome), SortDirection.ASCENDING)))
  }

  override fun filtro(): String {
    return if (this::edtFiltro.isInitialized) edtFiltro.value ?: ""
    else ""
  }

  override fun imprimeRelatorio(notas: List<NotaSaida>, labelTitle: String) {
    val report = RelatorioNotaFornecedor.processaRelatorio(notas)
    val chave = "DevFornecedor"
    SubWindowPDF(chave, report).open()
  }

  override fun imprimeRelatorioSap(notas: List<NotaDevolucaoSap>, labelTitle: String) {
    val report = RelatorioFornecedorSap.processaRelatorioNota(notas)
    val chave = "DevFornecedorSap"
    SubWindowPDF(chave, report).open()
  }

  override fun isAuthorized(user: IUser): Boolean {
    val username = user as? UserSaci
    return username?.conferenciaSap == true
  }

  override val label: String
    get() = "Sap x Saci"

  override fun updateComponent() {
    viewModel.updateView()
  }
}

