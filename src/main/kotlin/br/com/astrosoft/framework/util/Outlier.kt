package br.com.astrosoft.framework.util

import org.nield.kotlinstatistics.mode
import org.nield.kotlinstatistics.percentile

fun main(args: Array<String>) {
  System.out.printf("Hola\n")
  val list = ArrayList<Double>()
  list.add(945.45)
  list.add(102.02)
  list.add(3028.0)
  list.add(14400.0)
  list.add(1500.1)
  list.add(63.63)

  val mode = list.mode();

  val outliers = list.notOutliers()
  System.out.printf("%d outliers\n", outliers.size)
  if (outliers.isNotEmpty()) {
    for (x in outliers) {
      System.out.printf("  -> %f\n", x)
    }
  }
}

fun List<Double>.outliers(): List<Double> {
  val q1 = this.percentile(25.00)
  val q3 = this.percentile(75.00)
  val iqr = q3 - q1
  val ls = q3 + iqr * 0.5
  val li = q1 - iqr * 0.5
  val seq = sequence<Double> {
    for (x in this@outliers) {
      if (x > ls || x < li) {
        yield(x)
      }
    }
  }
  return seq.toList()
}

fun List<Double>.notOutliers(): List<Double> {
  val outlier = this.outliers()
  return (this - outlier.toSet()).sorted()
}