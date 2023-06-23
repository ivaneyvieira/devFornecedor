package br.com.astrosoft.devolucao.model.planilhas

import br.com.astrosoft.devolucao.model.beans.FornecedorSap
import br.com.astrosoft.devolucao.model.beans.NotaDevolucaoSap
import br.com.astrosoft.devolucao.model.planilhas.ETipoLinha.*
import br.com.astrosoft.framework.util.mid
import com.github.nwillc.poink.workbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.usermodel.Row
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class PlanilhaDevolucaoSap(val filename: String) {
  private lateinit var linhas: List<Row>
  private var posicao = 0

  fun next(): Row = linhas[posicao++]
  fun hasNext() = posicao < linhas.size

  fun read(): List<FornecedorSap> {
    val listaFornecedore = mutableListOf<FornecedorSap>()
    workbook(filename) {
      sheet(0) {
        linhas = this.toList()
        while (hasNext()) {
          val row = next()
          val fornecedor = findFornecedor(row)
          if (fornecedor != null) listaFornecedore.add(fornecedor)
        }
      }
    }
    return listaFornecedore
  }

  private fun findFornecedor(row: Row?): FornecedorSap? {
    row ?: return null
    return if (row.tipoLinha() == FORNECEDOR) {
      val codigo = row.getCell(1).getformatValue().toInt()
      val nome = row.getCell(2).getformatValue()
      val notas = findNotas(codigo, nome)
      if (notas.isNotEmpty()) FornecedorSap(codigo = codigo, nome = nome, quantidadeNotas = notas.size, notas = notas)
      else null
    } else null
  }

  private fun findNotas(codigoFor: Int, nomeFor: String): List<NotaDevolucaoSap> {
    val listaNotas = mutableListOf<NotaDevolucaoSap>()
    loop@ while (hasNext()) {
      val row = next()
      when {
        row.tipoLinha() == NOTA_DEVOLUCAO -> {
          val numero = row.getCell(4).getformatValue()
          val dataLancamento = row.getCell(6).getDateValue()
          val dataVencimento = row.getCell(7).getDateValue()
          val saldo = row.getCell(9).getDoubleValue()
          val storeno = row.getCell(19).getformatValue().mid(0, 2).toIntOrNull() ?: 0
          val nota =
              NotaDevolucaoSap(
                  codigoFor = codigoFor,
                  nomeFor = nomeFor,
                  storeno = storeno,
                  numero = numero,
                  dataLancamento = dataLancamento,
                  dataVencimento = dataVencimento,
                  saldo = saldo
              )
          listaNotas.add(nota)
        }

        row.tipoLinha() == FORNECEDOR -> {
          posicao--
          break@loop
        }
      }
    }
    return listaNotas
  }
}

private fun Row.tipoLinha(): ETipoLinha {
  val cell01 = this.getCell(1).getformatValue()
  val cell03 = this.getCell(3).getformatValue()
  return when {
    cell01 == "" -> when (cell03) {
      "" -> VAZIO
      "DE" -> NOTA_DEVOLUCAO
      else -> NOTA_OUTRAS
    }

    cell03 == "" -> FORNECEDOR
    else -> CABECALHO
  }
}

private fun Cell?.getformatValue(): String {
  this ?: return ""
  val formatter = DataFormatter()
  return formatter.formatCellValue(this)
}

private fun Cell?.getDateValue(): LocalDate? {
  this ?: return null
  return try {
    this.localDateTimeCellValue?.toLocalDate()
  } catch (e: Exception) {
    val formatter = DataFormatter()
    val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val strValue = formatter.formatCellValue(this) ?: return null
    return try {
      LocalDate.parse(strValue, dateFormat)
    } catch (e: DateTimeParseException) {
      null
    }
  }
}

private fun Cell?.getDoubleValue(): Double {
  this ?: return 0.00
  return try {
    this.numericCellValue
  } catch (e: Exception) {
    val formatter = DataFormatter()
    val strValue = formatter.formatCellValue(this) ?: return 0.00
    val strDouble = strValue.replace(",", "#").replace(".", "").replace("#", ".")

    return strDouble.toDoubleOrNull() ?: 0.00
  }
}

enum class ETipoLinha {
  FORNECEDOR, NOTA_DEVOLUCAO, NOTA_OUTRAS, CABECALHO, VAZIO
}

