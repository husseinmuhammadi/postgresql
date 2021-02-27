package com.javastudio.postgresql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSourceProvider {

//    private static final String url = "jdbc:postgresql://172.25.25.117:5432/cmsdb";
//    private static final String username = "cms_usr";
//    private static final String password = "SjIDt3cV3GEaxZse";

    private static final String jdbcUrl = "jdbc:postgresql://172.25.25.117:5432/dev_sp_cms?defaultSchema=cms&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    //    private static final String url = "jdbc:postgresql://172.25.25.117:5432/dev_sp_cms?defaultSchema=cms";
    private static final String username = "dev_sp_cms_usr";
    private static final String password = "mcqg2tJbdU";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, username, password);
    }

    private static String poolName = "transactional";
    private static int maximumPoolSize = 2;
    private static int minimumIdle = 1;

    public static HikariDataSource getConnectionPool() {
        HikariConfig jdbcConfig = new HikariConfig();
        jdbcConfig.setPoolName(poolName);
        jdbcConfig.setMaximumPoolSize(maximumPoolSize);
        jdbcConfig.setMinimumIdle(minimumIdle);
        jdbcConfig.setJdbcUrl(jdbcUrl);
        jdbcConfig.setDriverClassName("org.postgresql.Driver");
        // jdbcConfig.setConnectionTestQuery("SELECT GETDATE()");
        jdbcConfig.setUsername(username);
        jdbcConfig.setPassword(password);

        jdbcConfig.addDataSourceProperty("cachePrepStmts", true);
        jdbcConfig.addDataSourceProperty("prepStmtCacheSize", 256);
        jdbcConfig.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        jdbcConfig.addDataSourceProperty("useServerPrepStmts", true);
        jdbcConfig.addDataSourceProperty("connectionTimeout", 3000);

        // Add HealthCheck
        // jdbcConfig.setHealthCheckRegistry(healthCheckRegistry);

        // Add Metrics
        // jdbcConfig.setMetricRegistry(metricRegistry);

        return new HikariDataSource(jdbcConfig);
    }
}
