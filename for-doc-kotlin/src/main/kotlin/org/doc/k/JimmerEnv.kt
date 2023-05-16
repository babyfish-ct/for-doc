package org.doc.k

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.babyfish.jimmer.jackson.ImmutableModule
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.newKSqlClient
import org.babyfish.jimmer.sql.runtime.DatabaseValidationMode
import java.io.InputStreamReader
import java.sql.Connection
import java.sql.DriverManager

abstract class JimmerEnv : AutoCloseable {

    private val con: Connection =
        DriverManager.getConnection("jdbc:h2:mem:k").also {
            initH2(it)
        }

    override fun close() {
        con.close()
    }

    protected val sqlClient: KSqlClient =
        newKSqlClient {
            setConnectionManager {
                proceed(con)
            }
            setDatabaseValidationMode(DatabaseValidationMode.ERROR)
        }

    companion object {

        private val MAPPER = jacksonObjectMapper().registerModule(ImmutableModule())

        init {
            Class.forName("org.h2.Driver")
        }

        private fun initH2(con: Connection) {
            val stream = JimmerEnv::class.java.classLoader.getResourceAsStream("h2.sql")
                ?: error("No `h2.sql`")
            val sql = stream.use {
                InputStreamReader(it).readText()
            }
            con.createStatement().use {
                it.executeUpdate(sql)
            }
        }

        @JvmStatic
        protected fun toJson(value: Any?): String =
            MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(value)
    }
}