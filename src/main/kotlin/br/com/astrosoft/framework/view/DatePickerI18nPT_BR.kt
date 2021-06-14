package br.com.astrosoft.framework.view

import com.vaadin.flow.component.datepicker.DatePicker

class DatePickerI18nPT_BR : DatePicker.DatePickerI18n() {
  init {
    monthNames =
            listOf("Janeiro",
                   "Fevereiro",
                   "Março",
                   "Abril",
                   "Maio",
                   "Junho",
                   "Julho",
                   "Agosto",
                   "Setembro",
                   "Outubro",
                   "Novembro",
                   "Dezembro")
    weekdays = listOf("Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado")
    weekdaysShort = listOf("Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb")
    firstDayOfWeek = 0
    week = "Semana"
    calendar = "Calendário"
    clear = "Limpa"
    today = "Hoje"
    cancel = "Cancela"
  }
}