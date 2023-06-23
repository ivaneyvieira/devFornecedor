package br.com.astrosoft.framework.view

import com.vaadin.flow.component.upload.UploadI18N
import com.vaadin.flow.component.upload.UploadI18N.Uploading.RemainingTime

class UploadPtBr : UploadI18N() {
  init {
    dropFiles = DropFiles().setOne("Arraste o arquivo aqui").setMany("Arraste os arquivos aqui")
    addFiles = AddFiles().setOne("Escolha um arquivo...").setMany("Selecionar arquivos...")
    error =
        Error()
            .setTooManyFiles("Muitos arquivos.")
            .setFileIsTooBig("O arquivo é muito grande.")
            .setIncorrectFileType("Formato de arquivo incorreto.")
    uploading =
        Uploading()
            .setStatus(
                Uploading
                    .Status()
                    .setConnecting("Conectando...")
                    .setStalled("Parou")
                    .setProcessing("Processando arquivo...")
                    .setHeld("Em arquivo único")
            )
            .setRemainingTime(RemainingTime().setPrefix("tempo restante: ").setUnknown("tempo restante não disponível"))
            .setError(
                Uploading
                    .Error()
                    .setServerUnavailable("Servidor não responde\n")
                    .setUnexpectedServerError("Erro de servidor\n")
                    .setForbidden("Negado")
            )
    units = Units().setSize(listOf("B", "kB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"))
  }
}