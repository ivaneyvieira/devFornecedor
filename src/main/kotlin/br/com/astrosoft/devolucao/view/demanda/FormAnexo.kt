package br.com.astrosoft.devolucao.view.demanda

import br.com.astrosoft.devolucao.model.beans.AgendaDemanda
import br.com.astrosoft.devolucao.model.beans.NFFile
import br.com.astrosoft.framework.view.*
import com.github.mvysny.karibudsl.v10.grid
import com.github.mvysny.karibudsl.v10.isExpand
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.upload.FileRejectedEvent
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MemoryBuffer

class FormAnexo(val demanda: AgendaDemanda, private val isReadOnly: Boolean, val updateAnexo: () -> Unit) :
        VerticalLayout() {
  private var gridFile: Grid<NFFile>? = null

  init {
    width = "600px"
    height = "400px"
    if (!isReadOnly) {
      val (buffer, upload) = uploadFile()
      upload.addSucceededListener {
        val fileName = it.fileName
        val bytes = buffer.inputStream.readBytes()
        demanda.addAnexo(fileName, bytes)
        updateItens()
      }
    }
    gridFile = grid {
      this.isExpand = true
      /*
      addColumnDownloadButton(iconButton = VaadinIcon.SEARCH, header = "Abrir", fileName = { b: NFFile ->
        b.nome
      }) { b: NFFile ->
        b.file
      }
       */
      addColumnButton(VaadinIcon.SEARCH, "Abrir", "Abrir"){ bean ->
        val filename = bean.nome
        SubWindowView(filename, bean.file).open()
      }
      if (!isReadOnly) {
        addColumnButton(VaadinIcon.TRASH, "Remove", "Remove") { bean ->
          demanda.delAnexo(bean)
          updateItens()
        }
      }
      addColumnString(NFFile::nome) {
        setHeader("Nome")
      }
      addColumnLocalDate(NFFile::date) {
        setHeader("Data")
      }

      addItemClickListener {
        print("Teste")
      }
    }
    updateItens()
  }

  private fun updateItens() {
    gridFile?.setItems(demanda.findAnexos())
    updateAnexo()
  }

  private fun HasComponents.uploadFile(): Pair<MemoryBuffer, Upload> {
    val buffer = MemoryBuffer()
    val upload = Upload(buffer)
    upload.isDropAllowed = false

    val uploadButton = Button("Adicionar")
    uploadButton.icon = VaadinIcon.PLUS.create()
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