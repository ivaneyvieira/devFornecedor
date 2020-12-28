package br.com.astrosoft.devolucao.model.beans

data class EmailGmail(val email: String,
                      val assunto: String,
                      val msg: String,
                      val msgHtml: String,
                      val planilha: String,
                      val relatorio: String,
                      val anexos: String)