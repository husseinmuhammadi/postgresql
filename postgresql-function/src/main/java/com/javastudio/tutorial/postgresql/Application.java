package com.javastudio.tutorial.postgresql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class Application {
    public static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        PostgresqlDatabase database = new PostgresqlDatabase();
        for (int i = 0; i < 10; i++) {
            System.out.println(database.increase(i));
        }
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

    public int increase(int i) {
        int result = 0;
        try (Connection conn = this.connect();
             CallableStatement properCase = conn.prepareCall("{ ? = call cms.inc( ? ) }")) {
            properCase.registerOutParameter(1, Types.INTEGER);
            properCase.setInt(2, i);
            properCase.execute();
            result = properCase.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
}