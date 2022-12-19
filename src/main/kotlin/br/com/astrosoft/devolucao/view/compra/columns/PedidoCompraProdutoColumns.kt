package br.com.astrosoft.devolucao.view.compra.columns

import br.com.astrosoft.devolucao.model.beans.PedidoCompraProduto
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.github.mvysny.karibudsl.v10.isExpand
import com.github.mvysny.kaributools.sortProperty
import com.vaadin.flow.component.grid.Grid

object PedidoCompraProdutoColumns {
  fun Grid<PedidoCompraProduto>.colItem() = addColumnString(PedidoCompraProduto::item) {
    this.setHeader("Item")
    this.sortProperty = PedidoCompraProduto::linha
  }

  fun Grid<PedidoCompraProduto>.colCodigo() = addColumnString(PedidoCompraProduto::codigo) {
    this.setHeader("Código")
  }

  fun Grid<PedidoCompraProduto>.colDescricao() = addColumnString(PedidoCompraProduto::descricao) {
    this.setHeader("Descrição")
    this.isExpand = true
  }

  fun Grid<PedidoCompraProduto>.colDescNota() = addColumnString(PedidoCompraProduto::refname) {
    this.setHeader("Desc Nota")
    this.isExpand = true
  }


  fun Grid<PedidoCompraProduto>.colRefFabrica() = addColumnString(PedidoCompraProduto::refFab) {
    this.setHeader("Ref Fab")
  }

  fun Grid<PedidoCompraProduto>.colRefNota() = addColumnString(PedidoCompraProduto::refno) {
    this.setHeader("Ref Nota")
  }

  fun Grid<PedidoCompraProduto>.colGrade() = addColumnString(PedidoCompraProduto::grade) {
    this.setHeader("Grade")
  }

  fun Grid<PedidoCompraProduto>.colUnidade() = addColumnString(PedidoCompraProduto::unidade) {
    this.setHeader("UN")
  }

  fun Grid<PedidoCompraProduto>.colCusto() = addColumnDouble(PedidoCompraProduto::custoUnit) {
    this.setHeader("C.Unit")
  }

  fun Grid<PedidoCompraProduto>.colCustoCt() = addColumnDouble(PedidoCompraProduto::valorUnitarioCt) {
    this.setHeader("C.Unit Ct")
  }

  fun Grid<PedidoCompraProduto>.colQtde() = addColumnInt(PedidoCompraProduto::qtPedida) {
    this.setHeader("Qtde")
  }

  fun Grid<PedidoCompraProduto>.colQtdeCt() = addColumnInt(PedidoCompraProduto::quantidadeCt) {
    this.setHeader("Qtde Ct")
  }

  fun Grid<PedidoCompraProduto>.colVlTotal() = addColumnDouble(PedidoCompraProduto::vlPedido) {
    this.setHeader("C.Total")
  }

  fun Grid<PedidoCompraProduto>.colBarcode() = addColumnString(PedidoCompraProduto::barcode) {
    this.setHeader("Cod.Barra")
  }
}