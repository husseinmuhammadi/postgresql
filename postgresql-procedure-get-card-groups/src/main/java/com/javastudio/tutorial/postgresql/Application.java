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
        database.withdrawal1();
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

        String sql = "select * from master.sp_cms_get_card_groups(?)";
        try (Connection conn = this.connect()) {
            
            try (CallableStatement func = conn.prepareCall(sql)) {
                func.setInt(1, 5);
                ResultSet resultSet = func.executeQuery();
                while(resultSet.next()){
                    System.out.println(resultSet.getObject(1));
                    System.out.println(resultSet.getObject(2));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void withdrawal1() {

        String sql = "select * from master.sp_cms_get_card_groups()";
        try (Connection conn = this.connect()) {

            try (CallableStatement func = conn.prepareCall(sql)) {
                ResultSet resultSet = func.executeQuery();
                while(resultSet.next()){
                    System.out.println(resultSet.getObject(1));
                    System.out.println(resultSet.getObject(2));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
