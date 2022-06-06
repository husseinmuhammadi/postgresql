package com.javastudio.postgresql.config;

import java.util.Properties;

public class Configuration {

    static final Properties properties;

    static {
        properties = null;
    }

    public static DataSource datasource() {
        return new DataSource(properties);
    }

    public static class DataSource {
        final String url;
        final String driverClassName;
        final String username;
        final String password;

        public DataSource(Properties properties) {
            this.url = properties.getProperty("url");
            this.password = properties.getProperty("password");
            this.driverClassName = properties.getProperty("driver-class-name");
            this.username = properties.getProperty("username");
        }

        public String getUrl() {
            return url;
        }

        public String getDriverClassName() {
            return driverClassName;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }

}
