package com.javastudio.tutorial.postgresql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.util.StringJoiner;

public class Application {
    public static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        PostgresqlDatabase database = new PostgresqlDatabase();
        database.test19();
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

    public void test16() {
        String sql;
        sql = "{? = call test16()}";

        try (Connection conn = this.connect()) {
            conn.setAutoCommit(false);
            try (CallableStatement func = conn.prepareCall(sql)) {
                func.registerOutParameter(1, Types.INTEGER);
                func.execute();
                System.out.println(func.getInt(1));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void test17() {
        String sql;
        sql = "{? = call test17()}";

        try (Connection conn = this.connect()) {
            conn.setAutoCommit(false);
            try (CallableStatement func = conn.prepareCall(sql)) {
                func.registerOutParameter(1, Types.INTEGER);
                func.execute();
                System.out.println(func.getInt(1));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void test17_2() {
        String sql;
        sql = "{call test17(?)}";

        try (Connection conn = this.connect()) {
            conn.setAutoCommit(false);
            try (CallableStatement func = conn.prepareCall(sql)) {
                func.registerOutParameter(1, Types.INTEGER);
                func.execute();
                System.out.println(func.getInt(1));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void test18() {
        String sql;
        sql = "{call test18(?, ?)}";

        try (Connection conn = this.connect()) {
            conn.setAutoCommit(false);
            try (CallableStatement func = conn.prepareCall(sql)) {
                func.registerOutParameter(1, Types.INTEGER);
                func.registerOutParameter(2, Types.INTEGER);
                func.execute();
                System.out.println(func.getInt(1));
                System.out.println(func.getInt(2));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void test19() {
        String sql;
        sql = "{call test19(?, ?)}";

        try (Connection conn = this.connect()) {
            conn.setAutoCommit(false);
            try (CallableStatement func = conn.prepareCall(sql)) {
                func.registerOutParameter(1, Types.INTEGER);
                func.registerOutParameter(2, Types.INTEGER);
                func.execute();
                System.out.println(func.getInt(1));
                System.out.println(func.getInt(2));
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
