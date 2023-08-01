package br.com.astrosoft.framework.model

import br.com.astrosoft.framework.util.SystemUtils.readFile
import br.com.astrosoft.framework.util.customTrim
import org.simpleflatmapper.sql2o.SfmResultSetHandlerFactoryBuilder
import org.sql2o.*
import org.sql2o.converters.Converter
import org.sql2o.quirks.NoQuirks
import java.time.LocalDate
import java.time.LocalTime
import kotlin.reflect.KClass

typealias QueryHandler = Query.() -> Unit
typealias MonitorHandler = (text: String, pos: Int, total: Int) -> Unit

open class QueryDB(val database: DatabaseConfig) {
  private val sql2o: Sql2o

  init {
    Class.forName(database.driver)
    val maps = HashMap<Class<*>, Converter<*>>()
    maps[LocalDate::class.java] = LocalDateConverter()
    maps[LocalTime::class.java] = LocalSqlTimeConverter()
    maps[ByteArray::class.java] = ByteArrayConverter()
    this.sql2o = Sql2o(database.url, database.user, database.password, NoQuirks(maps))
  }

  private fun <T> ResultSetIterable<T>?.toSeq(monitor: MonitorHandler?, total: Int): List<T> {
    var pos = 0
    return sequence<T> {
      this@toSeq?.forEach {
        yield(it)
        monitor?.let { it("Lendo dados", ++pos, total) }
        print(".")
      }
      println()
    }.toList()
  }

  protected fun <T : Any> query(
    file: String,
    classes: KClass<T>,
    monitor: MonitorHandler? = null,
    lambda: QueryHandler = {}
  ): List<T> {
    return try {
      transaction { con ->
        val statements = toStratments(file)
        if (statements.isEmpty()) throw RuntimeException("Query vazia")
        val query = statements.lastOrNull() ?: throw RuntimeException("Query vazia")
        val updates = statements.dropLast(1)


        scriptSQLSeq(con = con, stratments = updates, lambda = lambda, monitor = monitor)
        println(query)
        val total = queryCount(con, query, lambda)
        println("Total: $total")
        val querySql = querySQL(con, query, classes, lambda)
        querySql.toSeq(monitor, total)
      }
    } catch (e: Sql2oException) {
      e.printStackTrace()
      throw e
    }
  }

  private fun queryCount(con: Connection, sql: String, lambda: QueryHandler = {}): Int {
    val sqlCount = "SELECT COUNT(*) FROM ($sql) AS C"
    val query = con.createQueryConfig(sqlCount)
    query.lambda()
    return query.executeScalar(Int::class.java) ?: 0
  }

  protected fun <T : Any> queryLazy(
    file: String,
    classes: KClass<T>,
    process: (bean: List<T>) -> Unit,
    monitor: MonitorHandler? = null,
    lambda: QueryHandler = {}
  ) {
    val statements = toStratments(file)
    if (statements.isEmpty()) throw RuntimeException("Query vazia")
    val query = statements.lastOrNull() ?: throw RuntimeException("Query vazia")
    val updates = statements.dropLast(1)
    transaction { con ->
      scriptSQLSeq(con, updates, lambda, monitor)
      querySQLLazy(con, query, classes, process, lambda)
    }
  }

  protected fun <R : Any> queryService(
    file: String,
    complemento: String?,
    lambda: QueryHandler = {},
    monitor: MonitorHandler? = null,
    result: (Query) -> R
  ): R {
    val statements = toStratments(file)
    val query = statements.lastOrNull() ?: throw RuntimeException("Query vazia")
    val queryComplemento = "$query\n$complemento"
    val updates = statements.dropLast(1)
    return transaction { con ->
      scriptSQLSeq(con, updates, lambda, monitor)
      val q = querySQLResult(con, queryComplemento, lambda)
      result(q)
    }
  }

  private fun Connection.createQueryConfig(sql: String?): Query {
    val query = createQuery(sql)
    query.isAutoDeriveColumnNames = true
    query.resultSetHandlerFactoryBuilder = SfmResultSetHandlerFactoryBuilder()
    return query
  }

  private fun querySQLResult(con: Connection, sql: String?, lambda: QueryHandler = {}): Query {
    val query = con.createQueryConfig(sql)
    query.lambda()
    return query
  }

  private fun <T : Any> querySQL(
    con: Connection, sql: String?,
    classes: KClass<T>,
    lambda: QueryHandler = {}
  ): ResultSetIterable<T> {
    val query = con.createQueryConfig(sql)
    query.lambda()
    return query.executeAndFetchLazy(classes.java)
  }

  private fun <T : Any> querySQLLazy(
    con: Connection,
    sql: String?, classes: KClass<T>,
    process: (bean: List<T>) -> Unit,
    lambda: QueryHandler = {}
  ) {
    val query = con.createQueryConfig(sql)
    query.lambda()
    val batch = mutableListOf<T>()
    query.executeAndFetchLazy(classes.java).use { beans ->
      beans.forEach { bean ->
        if (batch.size == BATCHSIZE) {
          process(batch)
          batch.clear()
        }
        batch.add(bean)
      }
    }

    if (batch.isNotEmpty()) {
      process(batch)
      batch.clear()
    }
  }

  protected fun script(file: String, monitor: MonitorHandler? = null, lambda: QueryHandler = {}) {
    script(file = file, monitor = monitor, lambda = listOf(lambda))
  }

  protected fun script(file: String, monitor: MonitorHandler? = null, lambda: List<QueryHandler>) {
    val updates = toStratments(file)
    transaction { con ->
      scriptSQLSeq(con = con, stratments = updates, lambda = lambda, monitor = monitor)
    }
  }

  private fun toStratments(file: String): List<String> {
    val sql = if (file.startsWith("/")) readFile(file)
    else file
    return sql.split(";").map { it.customTrim() }.filter { it.isNotEmpty() }
  }

  private fun scriptSQLSeq(
    con: Connection,
    stratments: List<String>,
    lambda: QueryHandler = {},
    monitor: MonitorHandler?
  ) {
    scriptSQLSeq(con, stratments, listOf(lambda), monitor)
  }

  private fun scriptSQLSeq(
    con: Connection,
    stratments: List<String>,
    lambda: List<QueryHandler>,
    monitor: MonitorHandler?
  ) {
    val listQuery = stratments.map { sql ->
      println(sql)
      ScripyUpdate(query = con.createQueryConfig(sql), queryText = sql)
    }
    val total = listQuery.size * lambda.size
    var pos = 0
    lambda.forEach { lamb ->
      listQuery.forEach { scriptUpdate ->
        val query = scriptUpdate.query
        query.lamb()
        monitor?.let { it("Script", ++pos, total) } ?: true
        query.executeUpdate()
        print(".")
      }
    }
    println()
  }

  fun Query.addOptionalParameter(name: String, value: String?): Query {
    if (this.paramNameToIdxMap.containsKey(name)) this.addParameter(name, value)
    return this
  }

  fun Query.addOptionalParameter(name: String, value: LocalDate?): Query {
    if (this.paramNameToIdxMap.containsKey(name)) this.addParameter(name, value)
    return this
  }

  fun Query.addOptionalParameter(name: String, value: ByteArray?): Query {
    if (this.paramNameToIdxMap.containsKey(name)) this.addParameter(name, value)
    return this
  }

  fun Query.addOptionalParameter(name: String, value: Int): Query {
    if (this.paramNameToIdxMap.containsKey(name)) this.addParameter(name, value)
    return this
  }

  fun Query.addOptionalParameter(name: String, value: Long): Query {
    if (this.paramNameToIdxMap.containsKey(name)) this.addParameter(name, value)
    return this
  }

  fun Query.addOptionalParameter(name: String, value: Double): Query {
    if (this.paramNameToIdxMap.containsKey(name)) this.addParameter(name, value)
    return this
  }

  protected fun <T> transaction(block: (Connection) -> T): T {
    return sql2o.beginTransaction().use { con ->
      val ret = block(con)
      con.commit()
      ret
    }
  }

  companion object {
    private const val BATCHSIZE = 1500
  }
}

data class ScripyUpdate(val query: Query, val queryText: String)
data class DatabaseConfig(val url: String, val user: String, val password: String, val driver: String)