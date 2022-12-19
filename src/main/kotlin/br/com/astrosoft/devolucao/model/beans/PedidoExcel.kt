package br.com.astrosoft.devolucao.model.beans

import io.github.rushuat.ocell.annotation.FieldExclude
import io.github.rushuat.ocell.annotation.FieldName
import io.github.rushuat.ocell.annotation.FieldOrder

class PedidoExcel(
  @FieldName("ITEM")
  @FieldOrder(0)
  val item: String?,
  @FieldName("REFERÊNCIA")
  @FieldOrder(1)
  val referencia: String?,
  @FieldName("DESCRIÇÃO")
  @FieldOrder(2)
  val descricao: String?,
  @FieldName("QUANTIDADE")
  @FieldOrder(3)
  val quantidade: Int?,
  @FieldName("VALOR UNITÁRIO")
  @FieldOrder(4)
  val valorUnitario: Double?,
  @FieldName("VALOR TOTAL")
  @FieldOrder(5)
  val valorTotal: Double?,
                 ){
  @FieldExclude
  var linha: Int = 0
}


