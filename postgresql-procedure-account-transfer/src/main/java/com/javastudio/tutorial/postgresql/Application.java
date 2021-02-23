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
        String sql = "{call cms.sp_cms_account_transfer_08" + sqlQueryParams(49) + "}";

        System.out.println(sql);
        try (Connection conn = this.connect()) {
//            conn.setAutoCommit(false);
            try (CallableStatement func = conn.prepareCall(sql)) {
                func.setInt(1, 10);
                func.setLong(2, 10);
                func.setLong(3, 10);
                func.setInt(4, 10);
                func.setString(5, "");
                func.setDate(6, Date.valueOf("2019-1-8"));
                func.setInt(7, 0);
                func.setLong(8, 0);
                func.setLong(9, 0);
                func.setString(10, "");
                func.setString(11, "");
                func.setString(12, "");
                func.setLong(13, 0);
                func.setShort(14, (short) 0);
                func.setInt(15, 0);
                func.setInt(16, 0);
                func.setString(17, "09122113358");
                func.setString(18, "");
                func.setTimestamp(19, Timestamp.valueOf("2019-10-12 18:15:16.1"));
                func.setLong(20, 0);
                func.setInt(21, 0);
                func.registerOutParameter(22, Types.NUMERIC);
                func.registerOutParameter(23, Types.NUMERIC);
                func.registerOutParameter(24, Types.INTEGER);
                func.registerOutParameter(25, Types.BIGINT);
                func.setInt(26, 11);
                func.setLong(27, 10000);
                func.setInt(28, 0);
                func.setString(29, "");
                func.setDate(30, Date.valueOf("2019-8-18"));
                func.setInt(31, 0);
                func.setLong(32, 0);
                func.setLong(33, 0);
                func.setString(34, "");
                func.setString(35, "");
                func.setString(36, "");
                func.setLong(37, 0);
                func.setShort(38, (short) 0);
                func.setInt(39, 0);
                func.setInt(40, 0);
                func.setTimestamp(41,Timestamp.valueOf("2019-10-12 18:15:16.1"));
                func.setString(42, "");
                func.setString(43, "");
                func.setInt(44, 0);
                func.registerOutParameter(45, Types.NUMERIC);
                func.registerOutParameter(46, Types.NUMERIC);
                func.registerOutParameter(47, Types.INTEGER);
                func.registerOutParameter(48, Types.BIGINT);
                func.registerOutParameter(49, Types.INTEGER);
                func.execute();

                Optional.ofNullable(func.getBigDecimal(22)).ifPresentOrElse(
                        appliedAmount -> System.out.println("Applied amount: " + appliedAmount.longValue()),
                        () -> System.out.println("Applied amount is null")
                );
                Optional.ofNullable(func.getBigDecimal(23)).ifPresentOrElse(
                        balance -> System.out.println("Balance: " + balance.longValue()),
                        () -> System.out.println("Balance is null")
                );
                Optional.of(func.getInt(24)).ifPresentOrElse(
                        result -> System.out.println("Balance: " + result),
                        () -> System.out.println("Balance is null")
                );
                Optional.of(func.getLong(25)).ifPresentOrElse(
                        transactionId -> System.out.println("Balance: " + transactionId),
                        () -> System.out.println("Balance is null")
                );

            } catch (SQLException e) {
                e.printStackTrace();
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
