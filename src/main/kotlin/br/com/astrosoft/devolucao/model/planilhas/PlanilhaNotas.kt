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
      CampoInt("Loja") {loja},
      CampoString("Nota") {nota?.nota ?: ""},
      CampoString("Data") {nota?.dataNota.format()},
      CampoString("Pedido") {nota?.pedido?.toString() ?: ""},
      CampoString("Data") {nota?.dataPedido.format()},
      CampoString("Código") {codigo},
      CampoString("Ref. Fabricante") {refFor},
      CampoString("Descrição") {descricao},
      CampoString("Unidare") {un},
      CampoString("Grade") {grade},
      CampoInt("Quantidade") {qtde},
      CampoString("Código de Barras") {barcode},
      CampoNumber("R$ Unit") {valorUnitario},
      CampoNumber("R$ Total") {valorTotal},
      CampoInt("NI") {invno},
      CampoInt("Quant") {quantInv},
      CampoString("Nota") {notaInv},
      CampoString("Data") {dateInv.format()},
      CampoNumber("R$ Unit") {valorUnitInv},
      CampoNumber("R$ Total") {valorTotalInv},
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