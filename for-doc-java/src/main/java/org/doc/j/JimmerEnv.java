package org.doc.j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.babyfish.jimmer.jackson.ImmutableModule;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.runtime.ConnectionManager;
import org.babyfish.jimmer.sql.runtime.DatabaseValidationMode;

import java.io.*;
import java.sql.*;
import java.util.function.Function;

public abstract class JimmerEnv implements AutoCloseable {

    private static final ObjectMapper MAPPER =
            new ObjectMapper()
                    .registerModule(new ImmutableModule());

    private Connection con;

    protected final JSqlClient sqlClient;

    public JimmerEnv() {
        try {
            try {
                this.con = DriverManager.getConnection("jdbc:h2:mem:j");
                initH2();
            } catch (RuntimeException | Error | SQLException ex) {
                close();
                throw ex;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        sqlClient = JSqlClient
                .newBuilder()
                .setConnectionManager(
                        new ConnectionManager() {
                            @Override
                            public <R> R execute(Function<Connection, R> block) {
                                return block.apply(con);
                            }
                        }
                )
                .setDatabaseValidationMode(DatabaseValidationMode.ERROR)
                .build();
    }

    protected static String toJson(Object value) {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void close() {
        Connection c = con;
        if (c != null) {
            con = null;
            try {
                c.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void initH2() {
        InputStream stream = JimmerEnv.class.getClassLoader().getResourceAsStream("h2.sql");
        if (stream == null) {
            throw new RuntimeException("No `h2.sql`");
        }
        StringBuilder builder = new StringBuilder();
        char[] buf = new char[1024];
        try (Reader reader = new InputStreamReader(stream)) {
            int len;
            while ((len = reader.read(buf)) != -1) {
                builder.append(buf, 0, len);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(builder.toString());
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
}
