package br.com.astrosoft.devolucao.model.beans

import io.github.rushuat.ocell.annotation.ClassName
import io.github.rushuat.ocell.annotation.FieldExclude
import io.github.rushuat.ocell.annotation.FieldName

class PedidoExcel(
  @FieldName("ITEM")
  val item: String,
  @FieldName("REFERÊNCIA")
  val referencia: String,
  @FieldName("DESCRIÇÃO")
  val descricao: String,
  @FieldName("QUANTIDADE")
  val quantidade: Int,
  @FieldName("VALOR UNITÁRIO")
  val valorUnitario: Double,
  @FieldName("VALOR TOTAL")
  val valorTotal: Double,
                 ){
  @FieldExclude
  var linha: Int = 0
}


