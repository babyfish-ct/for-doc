package org.doc.j.model;

import org.babyfish.jimmer.sql.*;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;

@Entity
public interface Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id();

    String name();

    int edition();

    BigDecimal price();

    @Nullable
    @ManyToOne
    BookStore store();

    @ManyToMany
    List<Author> authors();
}
