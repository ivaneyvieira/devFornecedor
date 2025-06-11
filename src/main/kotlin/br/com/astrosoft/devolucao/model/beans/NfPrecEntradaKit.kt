package br.com.astrosoft.devolucao.model.beans

data class NfPrecEntradaKit(
  val prdnoKit: String,
  val descricaoKit: String,
  val quantKit: Int,
  val valorUnitarioKit: Double,
  val prdno: String,
  val descricao: String,
  val quant: Int,
  val valorUnitario: Double
) {
  val valorTotalKit: Double
    get() = quantKit * valorUnitarioKit
  val valorTotal: Double
    get() = quant * valorUnitario
  val codigoKit
    get() = prdnoKit.trim().toIntOrNull() ?: 0
  val codigo
    get() = prdno.trim().toIntOrNull() ?: 0
  val tipo: Int
    get() = if (codigoKit == 0) 2 else 1
}

fun NfPrecEntrada.explodeKits(): List<NfPrecEntradaKit> {
  val prdno = this.prod
  val kits = ProdutoKit.find(prdno)
  val list = kits.map { kit ->
    NfPrecEntradaKit(
      prdnoKit = this.prod,
      descricaoKit = this.descricao ?: "",
      quantKit = this.quant ?: 0,
      valorUnitarioKit = kit.custoK ?: 0.00,
      prdno = kit.prdno ?: "",
      descricao = kit.descricao ?: "",
      quant = (kit.quant ?: 0) * (this.quant ?: 0),
      valorUnitario = kit.custo ?: 0.0
    )
  }

  val maxUnit = list.maxOfOrNull { it.valorUnitario } ?: 0.0
  val somaOutros = list.filter { it.valorUnitario < maxUnit }
    .sumOf { it.valorUnitario }

  val listCopy = list.map {
    it.copy(
      valorUnitario = if (it.valorUnitario == maxUnit) it.valorUnitarioKit - somaOutros else it.valorUnitario
    )
  }

  return listCopy
}

fun List<NfPrecEntradaKit>.agrupaCodigo(): List<NfPrecEntradaKit> {
  val listGroup = this.groupBy { it.prdno }
  val listMap = listGroup.mapNotNull { ent ->
    val listMap = ent.value
    if (listMap.size == 1) {
      listMap.firstOrNull()
    } else {
      NfPrecEntradaKit(
        prdnoKit = "",
        descricaoKit = "",
        quantKit = listMap.sumOf { it.quantKit },
        valorUnitarioKit = 0.00,
        prdno = listMap.firstOrNull()?.prdno ?: "",
        descricao = listMap.firstOrNull()?.descricao ?: "",
        quant = listMap.sumOf { it.quant },
        valorUnitario = listMap.firstOrNull()?.valorUnitario ?: 0.0,
      )
    }
  }
  return listMap.sortedWith(compareBy({ it.tipo }, { it.codigo }, { -it.valorUnitario }))
}