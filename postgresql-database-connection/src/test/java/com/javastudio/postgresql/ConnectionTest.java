package com.javastudio.postgresql;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class ConnectionTest {

    static Properties properties = new Properties();

    @BeforeAll
    static void beforeAll() throws IOException {
        try (InputStream in = ConnectionTest.class.getClassLoader().getResourceAsStream("datasource.properties")) {
            properties.load(in);
        }
    }

    @Test
    void shouldLoadPostgresqlDriverClass() throws ClassNotFoundException {
        Class<?> driver = Class.forName(properties.getProperty("driver-class-name"));
        Assertions.assertEquals("org.postgresql.Driver", driver.getName());
    }

    @Test
    void shouldConnectAndRunQuery() throws SQLException, ClassNotFoundException {

        Class.forName(properties.getProperty("driver-class-name"));

        try (Connection connection = DriverManager.getConnection(
                properties.getProperty("url"),
                properties.getProperty("username"),
                properties.getProperty("password")
        )) {
            assertTrue(connection.getAutoCommit());

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select 1");
            assertTrue(resultSet.next());
            assertEquals(1, resultSet.getInt(1));
        }
    }
}
