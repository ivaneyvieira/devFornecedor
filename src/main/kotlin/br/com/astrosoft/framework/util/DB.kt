package br.com.astrosoft.framework.util

import java.io.File
import java.io.FileReader
import java.util.*

class DB(banco: String) {
  val driver = prop?.getProperty("datasource.$banco.databaseDriver") ?: ""
  val url = prop?.getProperty("datasource.$banco.databaseUrl") ?: ""
  val username = prop?.getProperty("datasource.$banco.username") ?: ""
  val password = prop?.getProperty("datasource.$banco.password") ?: ""
  
  companion object {
    private val propertieFile = System.getProperty("ebean.props.file")
    
    private fun properties(): Properties? {
      val properties = Properties()
      val file = File(propertieFile)
      
      properties.load(FileReader(file))
      return properties
    }
    
    private val prop = properties()
    val test = prop?.getProperty("test") == "true"
    val gmailUser = prop?.getProperty("gmailUser")
    val gmailPass = prop?.getProperty("gmailPass")
    val gmailName = prop?.getProperty("gmailName")
  }
}