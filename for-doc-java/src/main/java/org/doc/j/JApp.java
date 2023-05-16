package org.doc.j;

import org.babyfish.jimmer.sql.fetcher.RecursiveListFieldConfig;
import org.doc.j.model.*;

import java.util.List;

public class JApp extends JimmerEnv {

    public static void main(String[] args) {
        try (JApp app = new JApp()) {
            app.run();
        }
    }

    private void run() {

        printBooks();
        printRootNodes();
    }

    private void printBooks() {
        BookTable table = BookTable.$;
        List<Book> books = sqlClient
                .createQuery(table)
                .orderBy(table.name())
                .orderBy(table.edition().desc())
                .select(
                        table.fetch(
                                BookFetcher.$
                                        .allScalarFields()
                                        .store(
                                                BookStoreFetcher.$
                                                        .allScalarFields()
                                        )
                                        .authors(
                                                AuthorFetcher.$
                                                        .allScalarFields()
                                        )
                        )
                )
                .execute();

        System.out.println(toJson(books));
    }

    private void printRootNodes() {
        TreeNodeTable table = TreeNodeTable.$;
        List<TreeNode> rootNodes = sqlClient
                .createQuery(table)
                .where(table.parent().isNull())
                .orderBy(table.name().asc())
                .select(
                        table.fetch(
                                TreeNodeFetcher.$
                                        .allScalarFields()
                                        .childNodes(
                                                TreeNodeFetcher.$.allScalarFields(),
                                                RecursiveListFieldConfig::recursive
                                        )
                        )
                )
                .execute();

        System.out.println(toJson(rootNodes));
    }
}
