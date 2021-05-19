package br.com.astrosoft.devolucao.model.beans

import java.time.LocalDate

class Pedido(val loja: Int, val pedido: Int, val data: LocalDate, val total: Double)