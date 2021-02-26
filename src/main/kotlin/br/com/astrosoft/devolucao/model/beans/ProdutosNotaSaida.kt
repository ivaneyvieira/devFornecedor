package br.com.astrosoft.devolucao.model.beans

import br.com.astrosoft.framework.util.toDate
import java.time.LocalDate

class ProdutosNotaSaida(
        val loja: Int,
        val pdv: Int,
        val transacao: Int,
        val codigo: String,
        val refFor: String,
        val descricao: String,
        val grade: String,
        val qtde: Int,
        val barcode: String,
        val un: String,
        val st: String,
        val valorUnitario: Double,
        val valorTotal: Double,
        val invno: Int,
        val quantInv: Int,
        val notaInv: String,
        val dateInv: LocalDate?,
        val valorUnitInv: Double,
        val valorTotalInv: Double,
        val ipi: Double,
        val vst: Double,
        val valorTotalIpi: Double,
        val chaveUlt: String?
                       ) {
    var item: Int = 0
    val dateInvDate
        get() = dateInv?.toDate()
    var nota: NotaSaida? = null
    fun ultimasNotas(): List<UltimasNotas> {
        val list =
            UltimasNotas.ultimasNotas(codigo, grade).sortedWith(compareBy({ it.data }, { it.ni }))
                    .reversed()
        return sequence {
            var saldo = 0
            list.forEach { nota ->
                if (saldo < qtde) yield(nota)
                saldo += nota.qttd
            }
        }.toList().sortedWith(compareBy({ it.data }, { it.ni })).reversed()
    }

    val ni get() = ultimasNotas().firstOrNull()?.ni
    val numeroNota get() = ultimasNotas().firstOrNull()?.nfNumero
    val dataNota get() = ultimasNotas().firstOrNull()?.data
    val qttdNota get() = ultimasNotas().firstOrNull()?.qttd
}