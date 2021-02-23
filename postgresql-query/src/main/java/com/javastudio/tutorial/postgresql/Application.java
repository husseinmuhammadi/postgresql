package com.javastudio.tutorial.postgresql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Properties;

public class Application {
    public static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {

        String url = "jdbc:postgresql://172.25.25.117:5432/cmsdb";
        Properties props = new Properties();
        props.setProperty("user", "cms_usr");
        props.setProperty("password", "SjIDt3cV3GEaxZse");
//            props.setProperty("ssl", "true");
        try (Connection connection = DriverManager.getConnection(url, props);
             PreparedStatement preparedStatement = connection.prepareStatement("select * from cms.CMSCard")) {
//            String url = "jdbc:postgresql://localhost/test?user=fred&password=secret&ssl=true";
//            Connection conn = DriverManager.getConnection(url);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                LOGGER.info("{}", resultSet.getLong("cardNo"));
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
