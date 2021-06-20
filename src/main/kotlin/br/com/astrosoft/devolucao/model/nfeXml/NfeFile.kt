package br.com.astrosoft.devolucao.model.nfeXml

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper

class NfeFile(val xmlContent: String) {
  fun emitente(): Emitente? {
    val index1 = xmlContent.indexOf("<emit>") //<emit.*>((.|\n)*)<\/emit>
    val index2 = xmlContent.indexOf("</emit>") + "</emit>".length
    val xmlEmitente = xmlContent.substring(index1, index2)

    val xmlMapper = XmlMapper(JacksonXmlModule().apply { setDefaultUseWrapper(false) }).apply {
      enable(SerializationFeature.INDENT_OUTPUT)
    }

    val emitente: Emitente = xmlMapper.readValue(xmlEmitente, Emitente::class.java)
    return emitente
  }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class Emitente(

  val CNPJ: String,

  val xNome: String,

  val xFant: String,
                   )
