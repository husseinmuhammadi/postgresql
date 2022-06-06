package com.javastudio.postgresql;

import com.google.common.io.Resources;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;
import java.util.stream.Collectors;

public class TransactionManagementTest {

    Connection connection;
    static Properties properties = new Properties();

    @BeforeAll
    static void beforeAll() throws IOException {
        try (InputStream in = TransactionManagementTest.class.getClassLoader().getResourceAsStream("datasource.properties")) {
            properties.load(in);
        }
    }

    @BeforeEach
    void setUp() throws ClassNotFoundException, SQLException {
        Class.forName(properties.getProperty("driver-class-name"));
        connection = DriverManager.getConnection(
                properties.getProperty("url"),
                properties.getProperty("username"),
                properties.getProperty("password")
        );
        connection.setAutoCommit(false);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void createTable() throws SQLException, IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                this.getClass().getClassLoader().getResourceAsStream("create-table-product.sql")))) {
            String sql = reader.lines().collect(Collectors.joining());
            Statement statement = connection.createStatement();
            statement.execute(sql);
            statement.close();
        }
    }

    @Test
    void commitTransaction() throws SQLException, IOException {
        URL url = Resources.getResource("insert-product.sql");
        String sql = Resources.toString(url, StandardCharsets.UTF_8);

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, 1);
        ps.setString(2, "java 11");
        ps.executeUpdate();

        connection.commit();
    }

    @Test
    void rollbackTransaction() throws SQLException, IOException, URISyntaxException {
        String sql = Files.readString(Paths.get(
                getClass().getClassLoader().getResource("insert-product.sql").toURI()),
                StandardCharsets.UTF_8);

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, 1);
        ps.setString(2, "java 8");
        ps.executeUpdate();

        connection.rollback();
    }
}
