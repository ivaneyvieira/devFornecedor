package br.com.astrosoft.devolucao.model.planilhas

import br.com.astrosoft.devolucao.model.beans.NotaSaida
import br.com.astrosoft.devolucao.model.beans.ProdutosNotaSaida
import br.com.astrosoft.framework.model.Campo
import br.com.astrosoft.framework.model.CampoInt
import br.com.astrosoft.framework.model.CampoNumber
import br.com.astrosoft.framework.model.CampoString
import br.com.astrosoft.framework.util.format
import com.github.nwillc.poink.workbook
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.VerticalAlignment
import java.io.ByteArrayOutputStream

class PlanilhaNotas {
  private val campos: List<Campo<*, ProdutosNotaSaida>> =
    listOf(
      CampoString("Emissão") {nota?.dataNota.format()},
      CampoInt("NI") {invno},
      CampoInt("Qnt NI") {quantInv},
      CampoInt("Qnt Dev") {qtde},
      CampoString("Código") {codigo},
      CampoString("Descrição") {descricao},
      CampoString("Grade") {grade},
      CampoNumber("R$ Unit") {valorUnitario},
      CampoNumber("R$ IPI") {ipi},
      CampoNumber("R$ ST") {vst},
      CampoNumber("R$ Total") {valorTotalIpi},
      CampoString("Chave") {nota?.chave?.substring(1, 6) ?: ""},
          )
  
  fun grava(listaNotas: List<NotaSaida>): ByteArray {
    val wb = workbook {
      val headerStyle = cellStyle("Header") {
        fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
        fillPattern = FillPatternType.SOLID_FOREGROUND
        this.verticalAlignment = VerticalAlignment.TOP
      }
      val rowStyle = cellStyle("Row") {
        this.verticalAlignment = VerticalAlignment.TOP
      }
      val stNotas = sheet("Produtos") {
        val headers = campos.map {it.header}
        row(headers, headerStyle)
        listaNotas.flatMap {it.listaProdutos()}.sortedBy {it.loja}.forEach {produto ->
          val valores = campos.map {it.produceVakue(produto)}
          row(valores, rowStyle)
        }
      }
      
      campos.forEachIndexed {index, _ ->
        stNotas.autoSizeColumn(index)
      }
    }
    val outBytes = ByteArrayOutputStream()
    wb.write(outBytes)
    return outBytes.toByteArray()
  }
}