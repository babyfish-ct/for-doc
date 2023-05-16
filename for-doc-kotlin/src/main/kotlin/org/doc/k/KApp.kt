package org.doc.k

import org.babyfish.jimmer.sql.kt.ast.expression.desc
import org.doc.k.model.*

class KApp : JimmerEnv() {

    fun run() {
        val books = sqlClient
            .createQuery(Book::class) {
                orderBy(table.name)
                orderBy(table.edition.desc())
                select(
                    table.fetchBy {
                        allScalarFields()
                        store {
                            allScalarFields()
                        }
                        authors {
                            allScalarFields()
                        }
                    }
                )
            }
            .execute()
        println(toJson(books))

        val rootNodes = sqlClient
            .createQuery(TreeNode::class) {
                orderBy(table.name)
                select(
                    table.fetchBy {
                        allScalarFields()
                        childNodes({
                            recursive()
                        }) {
                            allScalarFields()
                        }
                    }
                )
            }
            .execute()
        println(toJson(rootNodes))
    }
}

fun main(args: Array<String>) {
    KApp().use {
        it.run()
    }
}