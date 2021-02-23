package com.javastudio.tutorial.postgresql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class Application {
    public static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        PostgresqlDatabase database = new PostgresqlDatabase();
        // database.savePerson(25, "Hossein", "Mohammadi");
        // database.savePersonUsingFunc(102, "Hossein", "Mohammadi");
        database.saveProduct("Pencil");
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

    public void savePerson(long id, String firstName, String lastName) {
        try (Connection connection = this.connect()) {
//            connection.setAutoCommit(false);
            try (CallableStatement procedure = connection.prepareCall("call sp_save_person( ?, ?, ? )")) {
                procedure.setLong(1, id);
                procedure.setString(2, firstName);
                procedure.setString(3, lastName);
                procedure.execute();
            }
//             connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void saveProduct(String name) {
        try (Connection connection = this.connect()) {
            connection.setAutoCommit(false);
            saveProduct(connection, name);
            saveProduct(connection, name);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveProduct(Connection connection, String name) throws SQLException {
        try (CallableStatement procedure = connection.prepareCall("call sp_save_product( ?, ?, ? )")) {
            procedure.setString(1, name);
            procedure.setLong(2, 14);
            procedure.registerOutParameter(2, Types.BIGINT);
            procedure.registerOutParameter(3, Types.INTEGER);
            procedure.execute();
            System.out.println(procedure.getWarnings().getMessage());
            System.out.println(procedure.getObject(2));
            System.out.println(procedure.getObject(3));
        }
    }

    public void savePersonUsingFunc(long id, String firstName, String lastName) {
        try (Connection connection = this.connect()) {
//            connection.setAutoCommit(false);
            try (CallableStatement function = connection.prepareCall("{? = call fnc_save_person( ?, ?, ? )}")) {
                function.registerOutParameter(1, Types.INTEGER);
                function.setLong(2, id);
                function.setString(3, firstName);
                function.setString(4, lastName);
                function.execute();
                System.out.println(function.getObject(1));
            }
//            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}