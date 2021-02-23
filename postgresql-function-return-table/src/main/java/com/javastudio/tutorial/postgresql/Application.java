package com.javastudio.tutorial.postgresql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class Application {
    public static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        PostgresqlDatabase database = new PostgresqlDatabase();
        // database.showCardInfo("9832551217745378");
        database.getSettlementDate();
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

    public void showCardInfo(String cardNo) {
        try (Connection conn = this.connect();
             PreparedStatement preparedStatement = conn.prepareStatement("select * from cms.spcmsgetactivecard( ? )")) {
            preparedStatement.setLong(1, Long.parseLong(cardNo));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println(String.format("%s", resultSet.getString("cardNo")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getSettlementDate() {
        try (Connection conn = this.connect();
             CallableStatement callableStatement = conn.prepareCall("select * from cms.spgettaxday()")) {
            ResultSet resultSet = callableStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println(String.format("%s", resultSet.getString("TaxDay")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}