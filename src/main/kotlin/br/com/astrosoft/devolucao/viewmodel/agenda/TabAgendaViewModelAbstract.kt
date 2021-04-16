package br.com.astrosoft.devolucao.viewmodel.agenda

import br.com.astrosoft.AppConfig
import br.com.astrosoft.devolucao.model.beans.Agenda
import br.com.astrosoft.devolucao.model.beans.AgendaUpdate
import br.com.astrosoft.devolucao.model.beans.UserSaci
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.IViewModelUpdate
import br.com.astrosoft.framework.viewmodel.fail
import java.time.LocalDate
import java.time.LocalTime

abstract class TabAgendaViewModelAbstract(val viewModel: AgendaViewModel) : IViewModelUpdate {
  protected abstract val subView: ITabAgenda

  override fun updateView() = viewModel.exec {
    val user = AppConfig.user as? UserSaci ?: fail("Usuário não logado")
    val loja = if (user.admin) 0 else user.storeno
    val filtro = subView.filtro()
    subView.updateGrid(listAgenda(subView.agendado, subView.recebido, filtro, loja))
  }

  private fun listAgenda(agendado: Boolean, recebido: Boolean, filtro: String, loja: Int) =
          Agenda.listaAgenda(agendado, recebido, filtro, loja)

  fun salvaAgendamento(bean: AgendaUpdate?) = viewModel.exec {
    bean ?: fail("Agendamento inválido")
    val newbean = if (bean.dataRecbedor == null && !bean.recebedor.isNullOrEmpty()) bean.copy(dataRecbedor = LocalDate.now(),
                                                                                              horaRecebedor = LocalTime.now()
                                                                                                      .format())
    else bean
    newbean.save()
    updateView()
  }
}

interface ITabAgenda : ITabView {
  fun updateGrid(itens: List<Agenda>)
  fun filtro(): String
  val agendado: Boolean
  val recebido: Boolean
}