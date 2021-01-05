package br.com.astrosoft.devolucao.view.reports

import net.sf.dynamicreports.report.builder.DynamicReports.stl
import net.sf.dynamicreports.report.builder.DynamicReports.template
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.CENTER
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.LEFT
import net.sf.dynamicreports.report.constant.PageOrientation.LANDSCAPE
import net.sf.dynamicreports.report.constant.PageType.A4
import net.sf.dynamicreports.report.constant.VerticalTextAlignment.MIDDLE
import java.awt.Color

object Templates {
  val rootStyle = stl.style()
    .setPadding(1)
  val boldStyle = stl.style(rootStyle)
    .bold()
  val italicStyle = stl.style(rootStyle)
    .italic()
  val boldCenteredStyle = stl.style(boldStyle)
    .setTextAlignment(CENTER, MIDDLE)
  val bold9CenteredStyle = stl.style(boldCenteredStyle)
    .setFontSize(9)
  val columnStyle = stl.style(rootStyle)
    .setFontSize(8)
  val columnTitleStyle = stl.style(columnStyle)
    .setBorder(stl.pen1Point())
    .setHorizontalTextAlignment(CENTER)
    .setBackgroundColor(Color.LIGHT_GRAY)
    .bold()
  val groupStyle =
    stl.style(boldStyle)
      .setHorizontalTextAlignment(LEFT)
  val subtotalStyle =
    stl.style(boldStyle)
  val reportTemplate =
    template()
      .setPageFormat(A4, LANDSCAPE)
      .setColumnStyle(columnStyle)
      .setColumnTitleStyle(columnTitleStyle)
      .setGroupStyle(groupStyle)
      .setGroupTitleStyle(groupStyle)
      .setSubtotalStyle(subtotalStyle)
      .setDetailStyle(stl.style(rootStyle)
                        .setFontSize(8))
  val fieldFontTitle = stl.style(rootStyle)
    .setFontSize(4)
  val fieldFont = stl.style(rootStyle)
    .setFontSize(6)
  val fieldBorder = stl.style(fieldFont)
    .setBorder(stl.penThin())
    .setRadius(10)
  val fieldFontGrande = stl.style(rootStyle)
    .setFontSize(14)
}