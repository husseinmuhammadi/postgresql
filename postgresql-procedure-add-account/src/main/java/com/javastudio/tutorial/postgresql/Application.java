package com.javastudio.tutorial.postgresql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Consumer;

public class Application {
    public static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        PostgresqlDatabase database = new PostgresqlDatabase();
        database.transfer();
    }
}

class PostgresqlDatabase {

    public static final Logger LOGGER = LoggerFactory.getLogger(PostgresqlDatabase.class);

    final String url = "jdbc:postgresql://172.25.25.117:5432/cmsdb";
    final String username = "cms_usr";
    final String password = "SjIDt3cV3GEaxZse";

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     * @throws java.sql.SQLException
     */
    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public void transfer() {
        String sql = "{call cms.sp_cms_add_account(?,?,?,?,?,?,?)}";

        System.out.println(sql);
        try (Connection conn = this.connect()) {
//            conn.setAutoCommit(false);
            try (CallableStatement func = conn.prepareCall(sql)) {
                func.setLong(1, 1000001);
                func.setInt(2, 100);
                func.setBigDecimal(3, BigDecimal.valueOf(10));
                func.setShort(4, (short) 10);
                func.setDate(5, Date.valueOf("2019-1-8"));
                func.setInt(6, 0);
                func.registerOutParameter(7, Types.INTEGER);
                func.execute();

                Optional.of(func.getInt(7)).ifPresentOrElse(
                        appliedAmount -> System.out.println("return value: " + appliedAmount.longValue()),
                        () -> System.out.println("return value is null")
                );
            }
//            conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String sqlQueryParams(int n) {
        StringJoiner joiner = new StringJoiner(",", "(", ")");
        for (int i = 0; i < n; i++) {
            joiner.add("?");
        }
        return joiner.toString();
    }

    public static <T> void ifPresentOrElse(Optional<T> optional,
                                           Consumer<? super T> action, Runnable emptyAction) {
        if (optional.isPresent()) {
            action.accept(optional.get());
        } else {
            emptyAction.run();
        }
    }

}

class AutoRollback implements AutoCloseable {
    private Connection conn;
    private boolean committed;

    public AutoRollback(Connection conn) throws SQLException {
        this.conn = conn;
    }

    public void commit() throws SQLException {
        conn.commit();
        committed = true;
    }

    @Override
    public void close() throws SQLException {
        if (!committed) {
            conn.rollback();
        }
    }
}
