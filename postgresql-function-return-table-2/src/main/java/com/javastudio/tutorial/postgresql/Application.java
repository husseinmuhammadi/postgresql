package com.javastudio.tutorial.postgresql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class Application {
    public static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        PostgresqlDatabase database = new PostgresqlDatabase();
        database.query();
//        database.cursor(Timestamp.valueOf("2018-01-01 00:00:00.0"));
        database.accountTransactions(
                10,
                Timestamp.valueOf("2020-06-01 00:00:00.0"),
                Timestamp.valueOf("2021-01-01 00:00:00.0"),
                10,
                null
        );
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

    public void query() {
        try (Connection conn = this.connect()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("select * from test12(?)")) {
                preparedStatement.setTimestamp(1, Timestamp.valueOf("2021-01-01 00:00:00.0"));
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    System.out.println(String.format("%d", resultSet.getObject("result")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cursor(Timestamp date) {
        try (Connection conn = this.connect()) {
            conn.setAutoCommit(false);
            try (CallableStatement cursor = conn.prepareCall("{call test_cursor_04(?, ?, ?)}")) {
                cursor.setTimestamp(1, date);
                cursor.registerOutParameter(2, Types.INTEGER);
                cursor.registerOutParameter(3, Types.OTHER);
                cursor.execute();
                System.out.println(cursor.getInt(2));
                ResultSet resultSet = (ResultSet) cursor.getObject(3);
                while (resultSet.next()) {
                    System.out.println("---------------------------");
                    System.out.println("Column 1: " + resultSet.getObject(1));
                    System.out.println("Column 2: " + resultSet.getObject(2));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void accountTransactions(int accountId, Timestamp start, Timestamp end, int count, Long lastTransactionId) {
        try (Connection conn = this.connect()) {
            conn.setAutoCommit(false);
            try (CallableStatement cursor = conn.prepareCall("{call cms.sp_cms_account_transactions(?,?,?,?,?,?,?,?)}")) {
                cursor.setInt(1, accountId);

                if (start != null)
                    cursor.setTimestamp(2, start);
                else
                    cursor.setNull(2, Types.TIMESTAMP);

                if (end != null)
                    cursor.setTimestamp(3, end);
                else
                    cursor.setNull(3, Types.TIMESTAMP);

                cursor.setInt(4, count);

                if (lastTransactionId != null)
                    cursor.setLong(5, lastTransactionId);
                else
                    cursor.setNull(5, Types.BIGINT);

                cursor.registerOutParameter(6, Types.INTEGER);
                cursor.registerOutParameter(7, Types.VARCHAR);
                cursor.registerOutParameter(8, Types.OTHER);

                cursor.execute();
                System.out.println(cursor.getInt(6));
                System.out.println(cursor.getString(7));
                ResultSet resultSet = (ResultSet) cursor.getObject(8);
                int index = 0;
                while (resultSet != null && resultSet.next()) {
                    index++;
                    System.out.println("--------------------------");
                    System.out.println("Column 1: " + resultSet.getObject(1));
                    System.out.println("Column 2: " + resultSet.getObject(2));
                }
                if (index == 0)
                    System.out.println("No record found!!!");
                System.out.println("------------END------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}