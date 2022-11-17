package br.com.astrosoft.framework.planilhaUtil

import java.text.Normalizer
import java.util.regex.Pattern

class DataRow(private val tipo: ETipoFindCol) {
  private val map = mutableMapOf<String, String>()

  val isEmpty
    get() = map.values.all { it == "" }

  fun get(coluna: String) = map[coluna] ?: ""

  operator fun set(coluna: String, value: String?) = map.set(coluna, value ?: "")

  private fun deAccent(str: String): String {
    val nfdNormalizedString: String = Normalizer.normalize(str, Normalizer.Form.NFD)
    val pattern: Pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
    return pattern.matcher(nfdNormalizedString).replaceAll("")
  }

  private fun getString(colname: String, opcional: Boolean): String? {
    val key = map.keys.firstOrNull { k ->
      val dea = deAccent(k)
      when (tipo) {
        ETipoFindCol.EXATO      -> dea.uppercase().equals(colname, ignoreCase = true) || k == colname
        ETipoFindCol.APROXIMADO -> dea.uppercase().contains(colname, ignoreCase = true) || k == colname
      }
    }

    return if (key == null) {
      if (!opcional) {
        println("Coluna '$colname' não encontrada")
      }
      null
    }
    else {
      val value = map[key]
      value
    }
  }

  fun getStringDefault(colname: String, def: String = ""): String {
    return getString(colname, opcional = false) ?: def
  }

  fun getStringOpcional(colname: String, def: String = ""): String {
    return getString(colname, opcional = true) ?: def
  }

  private fun getInt(colname: String, opcional: Boolean): Int? {
    val strNum = getString(colname, opcional)?.trim() ?: return null
    return strNum.toDoubleOrNull()?.toInt() ?: 0
  }

  fun getIntDefault(colname: String, def: Int = 0): Int {
    val strNum = getString(colname, opcional = false)?.trim() ?: return def
    return strNum.toDoubleOrNull()?.toInt() ?: def
  }

  fun getIntOpcional(colname: String, def: Int = 0): Int {
    val strNum = getString(colname, opcional = true)?.trim() ?: return def
    return strNum.toDoubleOrNull()?.toInt() ?: def
  }
}

enum class ETipoFindCol {
  APROXIMADO, EXATO
}