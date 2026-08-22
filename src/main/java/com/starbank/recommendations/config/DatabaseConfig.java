package com.starbank.recommendations.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    private String pgUrl;
    @Value("${spring.datasource.username}")
    private String pgUser;
    @Value("${spring.datasource.password}")
    private String pgPassword;

    @Value("${h2.datasource.url}")
    private String h2Url;
    @Value("${h2.datasource.username}")
    private String h2User;
    @Value("${h2.datasource.password:#{null}}")
    private String h2Password;

    @Primary
    @Bean
    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(pgUrl);
        ds.setUsername(pgUser);
        ds.setPassword(pgPassword);
        return ds;
    }

    @Bean("h2DataSource")
    public DataSource h2DataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(h2Url);
        ds.setUsername(h2User);
        if (h2Password != null && !h2Password.isEmpty()) {
            ds.setPassword(h2Password);
        }
        return ds;
    }
}
