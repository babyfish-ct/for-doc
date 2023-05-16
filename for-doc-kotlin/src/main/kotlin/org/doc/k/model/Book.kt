package org.doc.k.model

import org.babyfish.jimmer.sql.*

@Entity
interface Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long

    val name: String

    val edition: Int

    @ManyToOne
    val store: BookStore?

    @ManyToMany
    val authors: List<Author>
}