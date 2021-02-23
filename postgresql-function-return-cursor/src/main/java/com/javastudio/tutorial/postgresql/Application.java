package com.javastudio.tutorial.postgresql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.StringJoiner;

public class Application {
    public static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        PostgresqlDatabase database = new PostgresqlDatabase();
        database.withdrawal();
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

    public void withdrawal() {
        String sql = "{call sp_ins_cms_account_withdraw" + parameterSequence(8) + "}";
        System.out.println(sql);
        try (Connection conn = this.connect()) {
            conn.setAutoCommit(false);
            try (CallableStatement func = conn.prepareCall(sql)) {
                func.setInt(1, 1);
                func.setInt(2, 1);
                func.setLong(3, 1);
                func.setLong(4, 1);
                func.setInt(5, 1);
                func.setString(6, "A");
                func.registerOutParameter(7, Types.INTEGER);
                func.registerOutParameter(8, Types.INTEGER);
                func.execute();
                System.out.println(String.format("result1: %d", func.getInt(7)));
                System.out.println(String.format("result2: %d", func.getInt(8)));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String parameterSequence(int n) {
        StringJoiner joiner = new StringJoiner(",", "(", ")");
        for (int i = 0; i < n; i++) {
            joiner.add("?");
        }
        return joiner.toString();
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
