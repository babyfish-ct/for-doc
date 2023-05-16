package org.doc.j.model;

import org.babyfish.jimmer.sql.*;

import java.util.List;

@Entity
public interface Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id();

    String firstName();

    String lastName();

    Gender gender();

    @ManyToMany(mappedBy = "authors")
    List<Book> books();
}
