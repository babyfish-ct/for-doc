package org.doc.k.model

import org.babyfish.jimmer.sql.*

@Entity
interface Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long

    val firstName: String

    val lastName: String

    val gender: Gender

    @ManyToMany(mappedBy = "authors")
    val books: List<Book>
}